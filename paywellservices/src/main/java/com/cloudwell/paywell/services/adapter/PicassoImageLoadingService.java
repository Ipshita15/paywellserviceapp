package com.cloudwell.paywell.services.adapter;

import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import ss.com.bannerslider.ImageLoadingService;

/**
 * @author S.Shahini
 * @since 4/7/18
 */

public class PicassoImageLoadingService implements ImageLoadingService {


    public PicassoImageLoadingService() {

    }

    @Override
    public void loadImage(String url, ImageView imageView) {

        Picasso.get()
                .load(url)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imageView);


    }

    @Override
    public void loadImage(int id, ImageView imageView) {
        Picasso.get()
                .load(id)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imageView);
    }

    @Override
    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
        Picasso.get()
                .load(url)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imageView);
    }
}
