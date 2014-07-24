package com.jnwan.worldcuponplam.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.util.TimerTask;
import java.util.Timer;

import com.jnwan.worldcuponplam.R;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.app.Activity;



public class WelcomeActivity extends Activity

{

    protected void onCreate(final Bundle bundle) {

        super.onCreate(bundle);

        setContentView(R.layout.welcome);
        DisplayMetrics dm = getResources().getDisplayMetrics();   
        int mScreenWidth = dm.widthPixels;//фад╩©М   
        int mScreenHeight = dm.heightPixels;//фад╩╦ъ   
        Bitmap bmp = ((BitmapDrawable)getResources().getDrawable(R.drawable.welcome)).getBitmap();
        Bitmap mBitmap = Bitmap.createScaledBitmap(bmp, mScreenWidth, mScreenHeight, true);
        ImageView imageView = (ImageView) findViewById(R.id.welcome);
        imageView.setImageBitmap(mBitmap);
        new Timer().schedule(new TimerTask() {

            @Override

            public void run() {

                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));

                finish();

            }

        }, 2000);

    }

}