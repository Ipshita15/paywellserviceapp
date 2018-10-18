package com.cloudwell.paywell.services.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cloudwell.paywell.services.app.AppHandler;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private String[] imageUrls;
    private AppHandler mAppHandler;
    public static final long UPDATE_IMG_CACHE_CLEAN_INTERVAL = 7 * 24 * 60 * 60;// 1 day

    public ViewPagerAdapter(Context context, String[] imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        mAppHandler = new AppHandler(context);
    }

    @Override
    public int getCount() {
        return imageUrls.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        if ((System.currentTimeMillis() / 1000) >= (mAppHandler.getImgCacheCleanUpdateCheck() + UPDATE_IMG_CACHE_CLEAN_INTERVAL)) {
            mAppHandler.setImgCacheCleanUpdateCheck(System.currentTimeMillis() / 1000);
            Picasso.get()
                    .load(imageUrls[position])
                    .fit()
                    .centerCrop()
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(imageView);
            container.addView(imageView);
        } else {
            Picasso.get()
                    .load(imageUrls[position])
                    .fit()
                    .centerCrop()
                    .into(imageView);
            container.addView(imageView);
        }

        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
