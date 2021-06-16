package com.cloudwell.paywell.services.eventBus.model;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-05-29.
 */
public class MessageToBottom {
    String message;

    public MessageToBottom(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
