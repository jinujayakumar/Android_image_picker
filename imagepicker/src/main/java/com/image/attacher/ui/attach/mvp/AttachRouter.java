package com.image.attacher.ui.attach.mvp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.image.attacher.model.PickImageModel;
import com.image.attacher.model.Constants;
import com.image.attacher.ui.crop.CropActivity;

import static com.image.attacher.ui.attach.mvp.AttachInteractorImpl.CAMERA_REQUEST_NEW;
import static com.image.attacher.ui.attach.mvp.AttachInteractorImpl.READ_REQUEST_CODE;
import static com.image.attacher.ui.attach.mvp.AttachInteractorImpl.RESULT_LOAD_IMAGE;
import static com.image.attacher.ui.attach.mvp.AttachInteractorImpl.CROP_IMAGE;

/**
 * Created by jinu.j on 27-12-2017.
 **/

class AttachRouter {


    static void launchGallery(AppCompatActivity activity) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    static void launchStorageApi(AppCompatActivity activity) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(intent, READ_REQUEST_CODE);
    }

    static void startCamera(AppCompatActivity activity, Uri uri) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(cameraIntent, CAMERA_REQUEST_NEW);
    }

    static void cropImage(Activity activity, PickImageModel pickImageModel, String mFileLocation) {
        activity.startActivityForResult(new Intent(activity, CropActivity.class)
                .putExtra(Constants.APP_DATA, pickImageModel)
                .putExtra(Constants.FILE_LOCATION, mFileLocation), CROP_IMAGE);
    }
}
