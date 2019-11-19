package com.cloudwell.paywell.services.activity.home;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-11-19.
 */
public interface OtpReceivedInterface {
    void onOtpReceived(String otp);
    void onOtpTimeout();
}
