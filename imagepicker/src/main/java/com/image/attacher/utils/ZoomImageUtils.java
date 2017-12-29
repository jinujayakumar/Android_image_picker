package com.image.attacher.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.view.View;

import com.image.attacher.model.Constants;
import com.image.attacher.ui.zoom.ImageZoomActivity;

/***
 * Created by Jinu on 4/23/2016.
 ***/
public class ZoomImageUtils {

    private String url;
    private String title;
    private boolean isFile = false;

    private static ZoomImageUtils ourInstance = new ZoomImageUtils();

    public static ZoomImageUtils getInstance() {
        return ourInstance;
    }

    private ZoomImageUtils() {
    }

    /**
     * can pass url and local file path
     */
    public ZoomImageUtils setUrl(String url) {
        this.url = url;
        return ourInstance;
    }

    /**
     * currently not using
     */
    public ZoomImageUtils setTitle(String title) {
        this.title = title;
        return ourInstance;
    }

    /**
     * if local file set true
     */
    public ZoomImageUtils setFile(boolean file) {
        isFile = file;
        return ourInstance;
    }

    public void build(View view, Activity activity) {
        Intent intent = new Intent(activity, ImageZoomActivity.class);
        intent.putExtra(Constants.FILE_LOCATION, url);
        intent.putExtra(Constants.TITLE, title);
        intent.putExtra(Constants.IS_FILE, isFile);
        ActivityOptions options;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions
                    .makeSceneTransitionAnimation(activity, view, "robot");
            activity.startActivity(intent, options.toBundle());
            return;
        }
        activity.startActivity(intent);
    }
}
