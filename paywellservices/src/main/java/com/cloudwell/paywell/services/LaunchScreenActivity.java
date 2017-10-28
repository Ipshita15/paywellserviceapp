package com.cloudwell.paywell.services;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.activity.AppLoadingActivity;

public class LaunchScreenActivity extends Activity {

    private static final int SPLASH_TIME_OUT = 5000;
    ImageView iv;
    TextView tv;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchscreen);

        if(getIntent().getExtras() != null){
            //do your stuff
            Log.e("amiakhane","here I am 1");
        }else{
            //do that you normally do
            //edit
        }

        StartAnimations();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(LaunchScreenActivity.this, AppLoadingActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l = (RelativeLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        iv = (ImageView)findViewById(R.id.logo);
        tv = (TextView) findViewById(R.id.link);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv.clearAnimation();
        iv.startAnimation(anim);
    }
}
