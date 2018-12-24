package com.cloudwell.paywell.services.adapter;

import android.content.Context;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 10/28/18.
 */
public class MainSliderAdapter extends SliderAdapter {

    private final Context context;
    private final String[] imageUrls;

    public MainSliderAdapter(Context context, String[] imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }


    @Override
    public int getItemCount() {
        return imageUrls.length;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {

        try {
            String imageUrl = imageUrls[position];
            viewHolder.bindImageSlide(imageUrl);
        } catch (Exception e) {

        }




    }
}
