package com.image.attacher.ui.attach.mvp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.image.attacher.model.PickImageModel;
import com.image.attacher.task.AdvancedImageSaveTask;
import com.image.attacher.ui.attach.mvp.AttachContract.Presenter.OnImageAttachListener;
import com.image.attacher.ui.attach.mvp.AttachContract.Presenter.OnImageSaveListener;
import com.image.attacher.utils.LruCacheSingleTon;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jinu.j on 26-12-2017.
 **/
public class AttachInteractorImpl implements AttachContract.Interactor {


    static final int RESULT_LOAD_IMAGE = 129;

    static final int READ_REQUEST_CODE = 178;
    static final int CROP_IMAGE = 1465;
    static final int CAMERA_REQUEST_NEW = 126;
    private DateFormat dateFormat = new SimpleDateFormat("DD_MM_yyyy_HH_MM", Locale.US);
    private String mFileLocation;
    private File cameraFileLocation;
    private PickImageModel pickImageModel;
    private AdvancedImageSaveTask imageResentTask;

    AttachInteractorImpl(PickImageModel pickImageModel) {
        this.pickImageModel = pickImageModel;
    }


    @Override
    public void launchGallery(AppCompatActivity activity) {
        AttachRouter.launchGallery(activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void launchStorageApi(AppCompatActivity activity) {
        AttachRouter.launchStorageApi(activity);
    }

    @Override
    public void startCamera(AppCompatActivity activity) {
        AttachRouter.startCamera(activity, createUri());
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data,
                                 AppCompatActivity activity,
                                 OnImageAttachListener attachListener) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            getFilePath(data, activity);
            try {
                LruCacheSingleTon.getInstance().setImage(mFileLocation);
                attachListener.onShowImage(mFileLocation);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == CROP_IMAGE && (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_CANCELED)) {
            attachListener.onShowImage(mFileLocation);
        } else if (requestCode == CAMERA_REQUEST_NEW && resultCode == Activity.RESULT_OK) {
            mFileLocation = cameraFileLocation.getAbsolutePath();
            try {
                LruCacheSingleTon.getInstance().setImage(mFileLocation);
            } catch (Exception e) {
                e.printStackTrace();
            }
            attachListener.onShowImage(mFileLocation);
        } else if (resultCode == Activity.RESULT_OK || requestCode == READ_REQUEST_CODE) {
            try {

                if (data != null && data.getData() != null) {
                    mFileLocation = data.getData().toString();
                    LruCacheSingleTon.getInstance().setImageRecent(mFileLocation, activity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            attachListener.onShowImage(mFileLocation);
        } else if (resultCode == Activity.RESULT_CANCELED || requestCode == RESULT_LOAD_IMAGE
                || requestCode == CAMERA_REQUEST_NEW) {
            attachListener.exit();
        }
    }

    private void getFilePath(Intent data, Activity activity) {
        Uri selectedImage = data.getData();
        assert selectedImage != null;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = activity.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        mFileLocation = picturePath;
    }

    private Uri createUri() {
        String fileName = "image_" + String.valueOf(dateFormat.format(new Date())) + ".jpg";
        cameraFileLocation = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);
        return Uri.fromFile(cameraFileLocation);
    }

    @Override
    public void deleteImage(OnImageAttachListener onImageAttachListener, Activity activity) {
        try {
            LruCacheSingleTon.getInstance().remove(mFileLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onImageAttachListener.exit();
    }

    @Override
    public void rotateImage(OnImageAttachListener onImageAttachListener, Activity activity) {
        try {
            LruCacheSingleTon.getInstance().rotateImage(mFileLocation);
            onImageAttachListener.onShowImage(mFileLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cropImage(OnImageAttachListener onImageAttachListener, Activity activity) {
        AttachRouter.cropImage(activity, pickImageModel, mFileLocation);
    }

    @Override
    public void saveImage(final OnImageSaveListener onImageAttachListener, final Activity activity) {
        imageResentTask = new AdvancedImageSaveTask(new AdvancedImageSaveTask.ImageCompressCallBack() {
            @Override
            public void onSave(String filePath, boolean success) {
                if (success) {
                    onImageAttachListener.onSaveImageSuccess(filePath, activity);
                } else {
                    onImageAttachListener.onSaveFailed();
                }
            }
        }, Uri.parse(mFileLocation), pickImageModel, mFileLocation, activity);
        imageResentTask.execute();
    }

    @Override
    public void onDestroy() {
        if (imageResentTask != null) {
            imageResentTask.cancel(true);
        }
    }

    @Override
    public void sendResult(String path, Activity activity) {
        Intent intent = new Intent();
        intent.setData(Uri.fromFile(new File(path)));
        activity.setResult(Activity.RESULT_OK, intent);
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(mFileLocation)));
    }
}