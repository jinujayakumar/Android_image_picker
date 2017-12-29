package com.image.attacher.dialog;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.image.attacher.R;


/**
 * Created by jinu.j on 27-09-2017.
 **/

public class CustomProgressDialog extends AppCompatDialog {

    private ImageView iv;

    public CustomProgressDialog(Context context) {
        super(context, R.style.TransparentProgressDialog);
        setContentView(R.layout.transparent_dialog);
        iv = findViewById(R.id.imageView);
    }


    @Override
    public void show() {
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(500);
        iv.setAnimation(anim);
        iv.startAnimation(anim);
        super.show();
    }

}
