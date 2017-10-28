package com.cloudwell.paywell.services.activity.payments.bkash;

/**
 * Created by android on 7/24/2016.
 */
public class Payments {
    private String id;
    private String amount;
    protected String datetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
