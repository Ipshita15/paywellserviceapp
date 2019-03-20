package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model;

import com.google.gson.annotations.SerializedName;


public class FileUploadReqSearchPara {

    @SerializedName("file_extension")
    private String mFileExtension;
    @SerializedName("image_content")
    private String mImageContent;
    @SerializedName("nid_number")
    private String mNidNumber;
    @SerializedName("passport_number")
    private String mPassportNumber;

    public String getFileExtension() {
        return mFileExtension;
    }

    public void setFileExtension(String fileExtension) {
        mFileExtension = fileExtension;
    }

    public String getImageContent() {
        return mImageContent;
    }

    public void setImageContent(String imageContent) {
        mImageContent = imageContent;
    }

    public String getNidNumber() {
        return mNidNumber;
    }

    public void setNidNumber(String nidNumber) {
        mNidNumber = nidNumber;
    }

    public String getPassportNumber() {
        return mPassportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        mPassportNumber = passportNumber;
    }

}
