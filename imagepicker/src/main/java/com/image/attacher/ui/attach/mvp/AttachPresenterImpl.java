package com.image.attacher.ui.attach.mvp;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import com.image.attacher.model.PickImageModel;
import com.image.attacher.ui.attach.mvp.AttachContract.Presenter.OnImageAttachListener;
import com.image.attacher.ui.attach.mvp.AttachContract.Presenter.OnImageSaveListener;

/**
 * Created by jinu.j on 26-12-2017.
 **/
public class AttachPresenterImpl implements AttachContract.Presenter,
        OnImageAttachListener,
        OnImageSaveListener {

    private AttachContract.View view;
    private PickImageModel pickImageModel;
    private AttachContract.Interactor interactor;


    public AttachPresenterImpl(AttachContract.View view, PickImageModel pickImageModel) {
        this.view = view;
        this.pickImageModel = pickImageModel;
        this.interactor = new AttachInteractorImpl(pickImageModel);
    }

    @Override
    public void attachImage(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                if (view.requestPermission()) {
                    addAttachment(activity);
                }
            }
        } else {
            addAttachment(activity);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data, AppCompatActivity activity) {
        interactor.onActivityResult(requestCode, resultCode, data, activity, this);
    }

    private void addAttachment(AppCompatActivity activity) {
        if (pickImageModel.isGallery) {
            startGallery(activity);
        } else {
            interactor.startCamera(activity);
        }
    }

    private void startGallery(AppCompatActivity activity) {
        if (pickImageModel.useResent) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                interactor.launchGallery(activity);
            } else {
                interactor.launchStorageApi(activity);
            }
        } else {
            interactor.launchGallery(activity);
        }
    }


    @Override
    public void onDestroy() {
        view = null;
        interactor.onDestroy();
    }


    @Override
    public void onShowImage(String path) {
        if (view != null) {
            view.dismissDialog();
            view.showImage(path);
        }
    }

    @Override
    public void exit() {
        if (view != null) {
            view.exit();
        }
    }

    @Override
    public void deleteImage(Activity activity) {
        interactor.deleteImage(this, activity);
    }

    @Override
    public void rotateImage(Activity activity) {
        interactor.rotateImage(this, activity);
    }

    @Override
    public void cropImage(Activity activity) {
        interactor.cropImage(this, activity);
    }

    @Override
    public void saveImage(Activity activity) {
        if (view != null) {
            view.showDialog();
        }
        interactor.saveImage(this, activity);
    }

    @Override
    public void onSaveImageSuccess(String path, Activity activity) {
        interactor.sendResult(path, activity);
        if (view != null) {
            view.dismissDialog();
            view.exit();
        }
    }

    @Override
    public void onSaveFailed() {
        view.showSaveError();
    }
}