package com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.model;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 17/1/19.
 */
public class MessageEventMobileNumberChange {
    private int mPosition;
    private ResMobileChangeLog mResMobileChangeLog;

    public MessageEventMobileNumberChange(int position, ResMobileChangeLog resMobileChangeLog) {

        mPosition = position;
        mResMobileChangeLog = resMobileChangeLog;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public ResMobileChangeLog getResMobileChangeLog() {
        return mResMobileChangeLog;
    }

    public void setResMobileChangeLog(ResMobileChangeLog resMobileChangeLog) {
        mResMobileChangeLog = resMobileChangeLog;
    }


}
