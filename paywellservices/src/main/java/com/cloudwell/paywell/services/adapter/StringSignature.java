package com.cloudwell.paywell.services.adapter;

import com.bumptech.glide.load.Key;

import java.security.MessageDigest;

import androidx.annotation.NonNull;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-11.
 */
class StringSignature implements Key {
    public StringSignature(String s) {
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
