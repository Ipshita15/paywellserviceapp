package com.cloudwell.paywell.services.activity.notification;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import androidx.annotation.Nullable;

public class ImageViewActivity extends BaseActivity {

    public static String TAG_IMAGE_URL = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.home_notification_img_view);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        final SubsamplingScaleImageView imageView = findViewById(R.id.imageViewNotification);

        showProgressDialog();

        Glide.with(ImageViewActivity.this)
                .asBitmap()
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        dismissProgressDialog();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        dismissProgressDialog();
                        return false;
                    }
                })
                .load(TAG_IMAGE_URL)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        imageView.setImage(ImageSource.bitmap(bitmap)); //For SubsampleImage
                    }
                });

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_NOTIFICATION_FULL_IMAGE_VIEW);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
