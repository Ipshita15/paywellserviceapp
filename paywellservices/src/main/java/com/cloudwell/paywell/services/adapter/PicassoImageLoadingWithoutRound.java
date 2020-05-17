package com.cloudwell.paywell.services.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.signature.ObjectKey;
import com.cloudwell.paywell.services.app.AppController;

import ss.com.bannerslider.ImageLoadingService;

/**
 * @author S.Shahini
 * @since 4/7/18
 */

public class PicassoImageLoadingWithoutRound implements ImageLoadingService {
    private String mImageUpdateVersionString;
    //private int roundEadius = 16;

    public PicassoImageLoadingWithoutRound(String imageUpdateVersionString) {

        mImageUpdateVersionString = imageUpdateVersionString;
    }

    @Override
    public void loadImage(String url, ImageView imageView) {


        Glide.with(AppController.getContext())
                .load(url)
                .signature(new ObjectKey(mImageUpdateVersionString))
               // .transform(new CenterCrop(), new RoundedCorners(roundEadius))
                .into(imageView);

    }

    @Override
    public void loadImage(int url, ImageView imageView) {
        Glide.with(AppController.getContext())
                .load(url)
                .signature(new StringSignature(mImageUpdateVersionString))
               // .transform(new CenterCrop(), new RoundedCorners(roundEadius))
                .into(imageView);
    }

    @Override
    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {


        Glide.with(AppController.getContext())
                .load(url)
                .signature(new StringSignature(mImageUpdateVersionString))
               // .transform(new CenterCrop(), new RoundedCorners(roundEadius))
                .into(imageView);

    }
}
