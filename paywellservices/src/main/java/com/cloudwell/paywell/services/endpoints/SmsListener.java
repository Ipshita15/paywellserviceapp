package com.cloudwell.paywell.services.endpoints;

/**
 * Created by naima.gani on 1/11/2018.
 */

public interface SmsListener {
    public void messageReceived(String messageText);
}
