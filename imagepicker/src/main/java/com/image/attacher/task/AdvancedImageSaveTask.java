package com.image.attacher.task;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;

import com.image.attacher.model.PickImageModel;
import com.image.attacher.utils.ImageUtils;
import com.image.attacher.utils.LruCacheSingleTon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by jinu.j on 27-12-2017.
 **/

public class AdvancedImageSaveTask extends AsyncTask<Void, Void, Boolean> {

    private String mFilePath;
    private ImageCompressCallBack mImageCompress;
    private Uri uri;
    private static final String TAG = "SaveImageTask";
    private Bitmap mImage;
    private int reqWidth;
    private int reqHeight;
    private boolean isRecent = false;
    private final String mFileLocation;
    private boolean usePicureDir = false;
    private WeakReference<Context> context;
    private String mFolderName;


    /**
     * this interface will return image saved file location
     * <p>
     * once its saved returns true else return false
     * along with filePath
     */
    public interface ImageCompressCallBack {
        void onSave(String filePath, boolean success);
    }

    public AdvancedImageSaveTask(ImageCompressCallBack mImageCompress,
                                 Uri uri,
                                 PickImageModel pickImageModel,
                                 String mFileLocation,
                                 Context context) {
        this.mImageCompress = mImageCompress;
        this.uri = uri;
        this.reqWidth = pickImageModel.compressWidth;
        this.reqHeight = pickImageModel.compressHeight;
        this.isRecent = pickImageModel.useResent;
        this.mFolderName = pickImageModel.folderName;
        this.mFileLocation = mFileLocation;
        this.usePicureDir = pickImageModel.isSavePicDir;
        this.context = new WeakReference<>(context);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            return compressAndSave();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        mImageCompress.onSave(mFilePath, aBoolean);
    }

    /**
     * saves and compress images to desired location
     *
     * @return if data is saved successfully true will be returned else false
     * @throws IOException if no output file is missing
     */
    private boolean compressAndSave() throws IOException {
        File mFile = createDirIfNotExists(mFolderName);
        try {
            mImage = LruCacheSingleTon.getInstance().getImage(mFileLocation);
            mImage = ImageUtils.compressImage(mImage, reqWidth, reqHeight);
            LruCacheSingleTon.getInstance().remove(mFileLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isRecent) {
            mFile = createImageRecent(mFile);
        } else {
            mFile = createImage(mFile);
        }
        return writeToFile(mFile);
    }


    /**
     * BitMaps coasts large amount of memory problems in android
     * release bitmap after usage is a good practice to save memory
     */
    private void recycleImage() {
        if (mImage != null) {
            mImage.recycle();
        }
    }

    /**
     * creates a directory with given name
     *
     * @param path file location of folder
     * @return File
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File createDirIfNotExists(String path) {

        File myDir = new File(getExternalStorageDirectory(), path);
        if (!myDir.exists()) {
            myDir.mkdirs();
        } else if (myDir.exists()) {
            Log.d(TAG, "Directory already exist");
        }
        return myDir;
    }

    private File getExternalStorageDirectory() {
        if (usePicureDir) {
            return Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        } else {
            return Environment.getExternalStorageDirectory();
        }
    }

    //create image from compressed bitmap object
    private File createImageRecent(File myDir) {
        File file;
        String fname = dumpImageMetaData(uri, context.get());
        file = new File(myDir, fname);
        Log.i(TAG, "" + file);

        return file;
    }

    private File createImage(File myDir) {
        File file;
        String fname = Uri.parse(mFilePath == null ? mFileLocation : mFilePath).getLastPathSegment();
        fname = System.currentTimeMillis() + "_" + fname;
        file = new File(myDir, fname);
        Log.i(TAG, "" + file);

        return file;
    }


    /**
     * writes bitmap to file
     */
    private boolean writeToFile(File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        if (mImage != null) {
            mImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            mFilePath = file.getAbsolutePath();
            recycleImage();
        } else {
            return false;
        }
        // sleepThread();
        return true;

    }


    /**
     * Once you have the URI for a document, you gain access to its metadata.
     * This snippet grabs the metadata for a document specified by the URI, and logs it:
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private String dumpImageMetaData(Uri uri, Context context) {
        String displayName = null;
        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.
        Cursor cursor = context.getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i(TAG, "Display Name: " + displayName);


            }
        } finally {
            assert cursor != null;
            cursor.close();
        }
        return displayName;
    }
}