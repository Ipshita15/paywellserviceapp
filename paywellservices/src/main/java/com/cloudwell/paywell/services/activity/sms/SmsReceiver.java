package com.cloudwell.paywell.services.activity.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.cloudwell.paywell.services.endpoints.SmsListener;

public class SmsReceiver extends BroadcastReceiver {
    private static SmsListener mListener;
    Boolean aBoolean = false;
    String dataParsed;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            //Just to fetch otp sent from PayWell or 01841016566
            if (sender.endsWith("PayWell") || sender.endsWith("01841016566")) {
                aBoolean = true;
            }
            String messageBody = smsMessage.getDisplayMessageBody();
            dataParsed = messageBody.replaceAll("[^0-9]", "");   // here dataParsed contains otp which is in number format
            //Pass on the text to our listener.
            if (aBoolean && !dataParsed.isEmpty() && dataParsed.contains("to get your phone number")) {
                mListener.messageReceived(dataParsed);  // attach value to interface object
            }
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}