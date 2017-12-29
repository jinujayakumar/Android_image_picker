package com.image.attacher.ui.attach;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.image.attacher.R;
import com.image.attacher.dialog.CustomProgressDialog;
import com.image.attacher.model.Constants;
import com.image.attacher.model.PickImageModel;
import com.image.attacher.ui.attach.mvp.AttachContract;
import com.image.attacher.ui.attach.mvp.AttachPresenterImpl;
import com.image.attacher.utils.LruCacheSingleTon;
import com.image.attacher.utils.TouchImageView;

/**
 * Created by jinu.j on 26-12-2017.
 **/

public class PickImageActivity extends AppCompatActivity implements
        AttachContract.View, View.OnClickListener {


    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 14;

    private AttachContract.Presenter mPresenter;
    private TouchImageView mCropImageView;
    private CustomProgressDialog mDialog;
    private FloatingActionButton mSendBtn;


    private PickImageModel pickImageModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_image);
        initView();
        initModel();
        initController();
        mPresenter.attachImage(this);
    }


    private void initModel() {
        if (getIntent().getExtras() != null) {
            pickImageModel = getIntent().getExtras().getParcelable(Constants.APP_DATA);
            mPresenter = new AttachPresenterImpl(this, pickImageModel);
        }
    }

    private void initController() {
        mSendBtn.setImageResource(pickImageModel.buttonIcon);
        mSendBtn.setOnClickListener(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    private void initView() {
        mCropImageView = findViewById(R.id.cropImageView);
        mSendBtn = findViewById(R.id.send_btn);
        mDialog = new CustomProgressDialog(PickImageActivity.this);
        if (getSupportActionBar() == null) {
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbar);
        } else {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(
                    ContextCompat.getColor(this, android.R.color.black)
            ));
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }
    }


    @Override
    public boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(PickImageActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
            }
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPresenter.attachImage(this);
            } else {
                Toast.makeText(PickImageActivity.this, "Permission not available",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mSendBtn.getId()) {
            mPresenter.saveImage(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            mPresenter.deleteImage(this);
            return true;
        }
        if (id == R.id.action_rotate) {
            mPresenter.rotateImage(this);
            return true;
        }
        if (id == R.id.action_crop) {
            mPresenter.cropImage(this);
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showImage(final String path) {

        mCropImageView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mCropImageView.setImageBitmap(LruCacheSingleTon.getInstance().getImage(path));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        mPresenter.deleteImage(this);
        super.onBackPressed();
    }

    @Override
    public void showDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    @Override
    public void dismissDialog() {
        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }


    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void exit() {
        finish();
    }

    @Override
    public void showSaveError() {
        Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        finish();
    }
}
