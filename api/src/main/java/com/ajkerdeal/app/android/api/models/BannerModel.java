package com.ajkerdeal.app.android.api.models;

/**
 * Created by Kazal on 05-Mar-16.
 */
public class BannerModel {


            private String PageName;
            private String Position;
            private int CategoryId;
            private int SubCategoryId;
            private int SubSubCategoryId;
            private String Image_url;
            private Boolean IsActive;
            private int OrderBy;
            private int DealId;
            private int MerchantId;
            private int RedirectionFlag;
            private String BannerImage;
            private int EventId;

    public BannerModel(String pageName, String position, int categoryId, int subCategoryId, int subSubCategoryId, String image_url, Boolean isActive, int orderBy, int dealId, int merchantId, int redirectionFlag, String bannerImage, int eventId) {
        PageName = pageName;
        Position = position;
        CategoryId = categoryId;
        SubCategoryId = subCategoryId;
        SubSubCategoryId = subSubCategoryId;
        Image_url = image_url;
        IsActive = isActive;
        OrderBy = orderBy;
        DealId = dealId;
        MerchantId = merchantId;
        RedirectionFlag = redirectionFlag;
        BannerImage = bannerImage;
        EventId = eventId;
    }

    public String getPageName() {
        return PageName;
    }

    public String getPosition() {
        return Position;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public int getSubCategoryId() {
        return SubCategoryId;
    }

    public int getSubSubCategoryId() {
        return SubSubCategoryId;
    }

    public String getImage_url() {
        return Image_url;
    }

    public Boolean getActive() {
        return IsActive;
    }

    public int getOrderBy() {
        return OrderBy;
    }

    public int getDealId() {
        return DealId;
    }

    public int getMerchantId() {
        return MerchantId;
    }

    public int getRedirectionFlag() {
        return RedirectionFlag;
    }

    public String getBannerImage() {
        return BannerImage;
    }

    public int getEventId() {
        return EventId;
    }

    @Override
    public String toString() {
        return "BannerModel{" +
                "PageName='" + PageName + '\'' +
                ", Position='" + Position + '\'' +
                ", CategoryId=" + CategoryId +
                ", SubCategoryId=" + SubCategoryId +
                ", SubSubCategoryId=" + SubSubCategoryId +
                ", Image_url='" + Image_url + '\'' +
                ", IsActive=" + IsActive +
                ", OrderBy=" + OrderBy +
                ", DealId=" + DealId +
                ", MerchantId=" + MerchantId +
                ", RedirectionFlag=" + RedirectionFlag +
                ", BannerImage='" + BannerImage + '\'' +
                ", EventId=" + EventId +
                '}';
    }
}
