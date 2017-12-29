package com.image.attacher.ui.crop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.image.attacher.R;
import com.image.attacher.model.Constants;
import com.image.attacher.utils.LruCacheSingleTon;
import com.isseiaoki.simplecropview.CropImageView;


/***
 * this activity will corp selected image
 *
 * Created by Jinu on 2/3/2016.
 ***/
public class CropActivity extends AppCompatActivity implements View.OnClickListener {

    private CropImageView mCropView;
    private Button mCropBtn;
    private Button mCancelBtn;

    private String mFileLocation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        initViews();
        initModel();
        initController();
        getImage();
    }

    /**
     * creates a bitmap from location
     */
    private void getImage() {
        try {
            mCropView.setImageBitmap(LruCacheSingleTon.getInstance().getImage(mFileLocation));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * initialises model object from previous class
     */
    private void initModel() {
        mFileLocation = getIntent().getExtras().getString(Constants.FILE_LOCATION);
    }

    private void initController() {

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();

        }
        mCropView.setHandleShowMode(CropImageView.ShowMode.SHOW_ON_TOUCH);
        mCropView.setGuideShowMode(CropImageView.ShowMode.SHOW_ON_TOUCH);
        mCropBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
    }

    private void initViews() {
        mCropView = findViewById(R.id.cropImageView);
        mCropBtn = findViewById(R.id.crop_btn);
        mCancelBtn = findViewById(R.id.cancel_btn);
    }

    @Override
    public void onClick(View v) {
        if (mCropBtn.getId() == v.getId()) {
            try {
                LruCacheSingleTon.getInstance().setImage(mFileLocation, mCropView.getCroppedBitmap());
                Intent intent = new Intent();
                intent.putExtra(Constants.FILE_LOCATION, mFileLocation);
                setResult(RESULT_OK, intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }


}
