package com.cloudwell.paywell.services.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.cloudwell.paywell.services.app.AppHandler;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import ss.com.bannerslider.ImageLoadingService;

/**
 * @author S.Shahini
 * @since 4/7/18
 */

public class PicassoImageLoadingService implements ImageLoadingService {
    public Context context;
    public static final long UPDATE_IMG_CACHE_CLEAN_INTERVAL = 7 * 24 * 60 * 60;// 1 day
    private AppHandler mAppHandler;


    public PicassoImageLoadingService(Context context) {
        this.context = context;
        mAppHandler = new AppHandler(context);
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        if ((System.currentTimeMillis() / 1000) >= (mAppHandler.getImgCacheCleanUpdateCheck() + UPDATE_IMG_CACHE_CLEAN_INTERVAL)) {
            mAppHandler.setImgCacheCleanUpdateCheck(System.currentTimeMillis() / 1000);
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(imageView);
        } else {
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }

    }

    @Override
    public void loadImage(int resource, ImageView imageView) {
        if ((System.currentTimeMillis() / 1000) >= (mAppHandler.getImgCacheCleanUpdateCheck() + UPDATE_IMG_CACHE_CLEAN_INTERVAL)) {
            mAppHandler.setImgCacheCleanUpdateCheck(System.currentTimeMillis() / 1000);
            Picasso.get()
                    .load(resource)
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(imageView);
        } else {
            Picasso.get()
                    .load(resource)
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }
    }

    @Override
    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
        if ((System.currentTimeMillis() / 1000) >= (mAppHandler.getImgCacheCleanUpdateCheck() + UPDATE_IMG_CACHE_CLEAN_INTERVAL)) {
            mAppHandler.setImgCacheCleanUpdateCheck(System.currentTimeMillis() / 1000);
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(imageView);
        } else {
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }
    }
}
