package com.image.attacher.ui.zoom;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.image.attacher.R;
import com.image.attacher.model.Constants;

import java.io.File;

public class ImageZoomActivity extends AppCompatActivity {


    private static final String TAG = "ImageZoomActivity";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String fileLocation = getIntent().getExtras().getString(Constants.FILE_LOCATION);
        String title = getIntent().getExtras().getString(Constants.TITLE);
        boolean isFile = getIntent().getExtras().getBoolean(Constants.IS_FILE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setExitTransition(new Explode());
        }
        setContentView(R.layout.activity_image_zoom);
        if (fileLocation == null) {
            Log.e(TAG, "onCreate: ", new NullPointerException("fieLocationIsEmpty"));
            return;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        AppCompatImageView mTouchImageView = findViewById(R.id.image_zoom);
        mTouchImageView.setOnTouchListener(new ImageMatrixTouchHandler(this));
        Glide.with(ImageZoomActivity.this)
                .load(isFile ? new File(fileLocation) : fileLocation)
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .into(mTouchImageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
