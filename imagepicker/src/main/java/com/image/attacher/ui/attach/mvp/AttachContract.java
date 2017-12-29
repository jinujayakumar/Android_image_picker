package com.image.attacher.ui.attach.mvp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jinu.j on 26-12-2017.
 **/
public interface AttachContract {

    interface View {
        boolean requestPermission();

        void showImage(String path);

        void showDialog();

        void dismissDialog();

        void exit();

        void showSaveError();
    }

    interface Interactor {

        void launchGallery(AppCompatActivity activity);

        void launchStorageApi(AppCompatActivity activity);

        void startCamera(AppCompatActivity activity);

        void onActivityResult(int requestCode,
                              int resultCode,
                              Intent data,
                              AppCompatActivity activity,
                              Presenter.OnImageAttachListener onImageAttachListener);

        void deleteImage(Presenter.OnImageAttachListener onImageAttachListener,Activity activity);

        void rotateImage(Presenter.OnImageAttachListener onImageAttachListener,Activity activity);

        void cropImage(Presenter.OnImageAttachListener onImageAttachListener,Activity activity);

        void saveImage(Presenter.OnImageSaveListener onImageAttachListener,Activity activity);

        void onDestroy();

        void sendResult(String path, Activity activity);
    }

    interface Presenter {
        void attachImage(AppCompatActivity activity);

        void onActivityResult(int requestCode,
                              int resultCode,
                              Intent data,
                              AppCompatActivity activity);

        void onDestroy();

        void deleteImage(Activity activity);

        void rotateImage(Activity activity);

        void cropImage(Activity activity);

        void saveImage(Activity activity);

        interface OnImageAttachListener {

            void onShowImage(String path);

            void exit();
        }

        interface OnImageSaveListener {

            void onSaveImageSuccess(String path,Activity activity);

            void onSaveFailed();
        }
    }
}
