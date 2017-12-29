package com.image.attacher.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;

import com.image.attacher.model.PickImageModel;
import com.image.attacher.model.Constants;
import com.image.attacher.ui.attach.PickImageActivity;

/***
 * Created by Jinu on 3/11/2016.
 ***/
@SuppressWarnings("SameParameterValue")
public class ImagePicker {

    private PickImageModel pickImageModel;


    private static ImagePicker ourInstance = new ImagePicker();

    public static ImagePicker getInstance() {
        return ourInstance;
    }

    private ImagePicker() {
        pickImageModel = new PickImageModel();
    }

    /**
     * filderName to be saved
     */
    public ImagePicker setFolderName(String folderName) {
        pickImageModel.folderName = folderName;
        return ourInstance;
    }

    /**
     * open gallery or not if yes pass true else opens camera
     */
    public ImagePicker useGallery(boolean gallery) {
        pickImageModel.isGallery = gallery;
        return ourInstance;
    }

    /**
     * Image Save width and height
     */
    public ImagePicker setCompressWidthHeight(int width, int height) {
        pickImageModel.compressWidth = width;
        pickImageModel.compressHeight = height;
        return ourInstance;
    }




    /**
     * open storage api or not false will start gallery
     * else if < 4.4 opens storage api
     */
    public ImagePicker useStorageApi(boolean status) {
        pickImageModel.useResent = status;
        return ourInstance;
    }

    public ImagePicker usePictureDir(boolean usePicDir) {
        pickImageModel.isSavePicDir = usePicDir;
        return ourInstance;
    }


    public Intent build(Context context) {
        Intent intent = new Intent(context, PickImageActivity.class);
        intent.putExtra(Constants.APP_DATA, pickImageModel);
        return intent;
    }

    /**
     * icon drawable if you want to change
     * eg. OK,SAVE,SEND
     */
    public ImagePicker setIcon(@DrawableRes int icon) {
        pickImageModel.buttonIcon = icon;
        return ourInstance;
    }
}
