
package com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model;


import com.google.gson.annotations.SerializedName;


public class DescoPrepaidTrxLogRequest {

    @SerializedName("format")
    private String format;
    @SerializedName("limit")
    private String limit;
    @SerializedName("ref_id")
    private String refId;
    @SerializedName("service")
    private String service;
    @SerializedName("username")
    private String username;

    public DescoPrepaidTrxLogRequest(String format, String limit, String refId, String service, String username) {
        this.format = format;
        this.limit = limit;
        this.refId = refId;
        this.service = service;
        this.username = username;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
