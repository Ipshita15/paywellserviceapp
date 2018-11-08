package com.cloudwell.paywell.services.activity.topup.model;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 11/7/18.
 */
public class MobileOperator {
    private String mName;
    private int mIcon;
    private int mIsSeletedIcon;
    private boolean mIsSeleted;

    public MobileOperator(String name, int icon, int isSeletedIcon, boolean isSeleted) {
        mName = name;
        mIcon = icon;
        mIsSeletedIcon = isSeletedIcon;
        mIsSeleted = isSeleted;
    }

    public int getIcon() {
        return mIcon;
    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isSeleted() {
        return mIsSeleted;
    }

    public void setSeleted(boolean seleted) {
        mIsSeleted = seleted;
    }

    public int getIsSeletedIcon() {
        return mIsSeletedIcon;
    }

    public void setIsSeletedIcon(int isSeletedIcon) {
        mIsSeletedIcon = isSeletedIcon;
    }
}

