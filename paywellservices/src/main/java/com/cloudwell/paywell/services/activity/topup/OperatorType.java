package com.cloudwell.paywell.services.activity.topup;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 10/29/18.
 */
public enum OperatorType {
    GP("GP"),
    ROBI("ROBI"),
    BANGLALINK("BANGLALINK"),
    TELETALK("TELETALK"),
    AIRTEL("AIRTEL");

    private String text;

    OperatorType(String text) {
        this.text = text;
    }


    public String getText() {
        return text;
    }
}
