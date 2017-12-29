package com.image.attacher.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v4.util.LruCache;

/***
 * Created by Jinu on 3/17/2016.
 ***/
public class LruCacheSingleTon {

    private LruCache<String, Bitmap> mLruCache;

    private static LruCacheSingleTon ourInstance;
    private int width = 500;
    private int height = 500;

    public static synchronized LruCacheSingleTon getInstance() {
        if (ourInstance == null) {
            ourInstance = new LruCacheSingleTon();
        }
        return ourInstance;
    }

    private LruCacheSingleTon() {

        mLruCache = new LruCache<>(getDefaultMemory());


    }

    public LruCache<String, Bitmap> getLruCache() throws Exception {
        checkNotNull();
        return mLruCache;
    }

    public void remove(String key) throws Exception {
        checkNotNull(key);
        mLruCache.remove(key);
    }

    public Bitmap getImage(String location) throws Exception {
        checkNotNull(location);
        return mLruCache.get(location);
    }


    public Bitmap getImage(@DrawableRes int res) throws Exception {
        checkNotNull(res);
        return mLruCache.get(String.valueOf(res));
    }

    public void setImage(String location) throws Exception {
        checkNotNull(location);
        mLruCache.put(location, ImageUtils.decodeBitmap(location, width, height));
    }

    public void setImage(String location, Bitmap bitmap) throws Exception {
        checkNotNull(location);
        mLruCache.put(location, bitmap);
    }

    public void setImage(String location, int width, int height) throws Exception {
        checkNotNull(location);
        width = getWidth(width);
        height = getHeight(height);
        mLruCache.put(location, ImageUtils.decodeBitmap(location, width, height));
    }

    public void setImage(@DrawableRes int res, Context context) throws Exception {
        checkNotNull(res);
        mLruCache.put(String.valueOf(res), ImageUtils.decodeResource(context.getResources(), res, width, height));
    }

    private void checkNotNull(int res) {
        if (mLruCache == null) {
            throw new NullPointerException("Cache is empty");
        }
        if (res == 0) {
            throw new NullPointerException("res is empty");
        }
    }

    private int getHeight(int height) {
        if (height == 0) {
            return this.height;
        }
        return height;
    }

    private int getWidth(int width) {
        if (width == 0) {
            return this.width;
        }
        return width;
    }


    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private void checkNotNull() {
        if (mLruCache == null) {
            throw new NullPointerException("Cache is empty");
        }
    }

    private void checkNotNull(String key) {
        if (mLruCache == null) {
            throw new NullPointerException("Cache is empty");
        }
        if (key == null) {
            throw new NullPointerException("key is empty");
        }
    }


    private int getCacheSize(int size) {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        return maxMemory / size;
    }

    private int getDefaultMemory() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        return maxMemory / 8;
    }

    public void rotateImage(String mFileLocation) throws Exception {
        Bitmap bitmap = getImage(mFileLocation);
        bitmap = rotateBitmap(bitmap);
        setImage(mFileLocation, bitmap);
    }

    private Bitmap rotateBitmap(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void setImageRecent(String mFileLocation, Context context)throws Exception {
        Bitmap bitmap = ImageUtils.decodeBitmap(Uri.parse(mFileLocation), width, height, context);
        setImage(mFileLocation, bitmap);
    }
}
