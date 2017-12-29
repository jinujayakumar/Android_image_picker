package com.image.attacher.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.image.attacher.R;

/***
 * Created by Jinu on 3/11/2016.
 ***/
public class PickImageModel implements Parcelable {

    /**
     * folderName of image that you want to upload
     */
    public String folderName = "Jinu";


    /**
     * if you want to attach from gallery set true else false
     */
    public boolean isGallery = true;


    /**
     * image save default width
     */
    public int compressWidth = 500;

    /**
     * image save default height
     */
    public int compressHeight = 500;

    /**
     * button name to be displayed
     */
    public int buttonIcon = R.drawable.ic_send_white_24dp;

    /**
     * whether use storage api or not
     */
    public boolean useResent = false;

    public boolean isSavePicDir = false;


    public PickImageModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.folderName);
        dest.writeByte(this.isGallery ? (byte) 1 : (byte) 0);
        dest.writeInt(this.compressWidth);
        dest.writeInt(this.compressHeight);
        dest.writeInt(this.buttonIcon);
        dest.writeByte(this.useResent ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSavePicDir ? (byte) 1 : (byte) 0);
    }

    protected PickImageModel(Parcel in) {
        this.folderName = in.readString();
        this.isGallery = in.readByte() != 0;
        this.compressWidth = in.readInt();
        this.compressHeight = in.readInt();
        this.buttonIcon = in.readInt();
        this.useResent = in.readByte() != 0;
        this.isSavePicDir = in.readByte() != 0;
    }

    public static final Creator<PickImageModel> CREATOR = new Creator<PickImageModel>() {
        @Override
        public PickImageModel createFromParcel(Parcel source) {
            return new PickImageModel(source);
        }

        @Override
        public PickImageModel[] newArray(int size) {
            return new PickImageModel[size];
        }
    };
}
