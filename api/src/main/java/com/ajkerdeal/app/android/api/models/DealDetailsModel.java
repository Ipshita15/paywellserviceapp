package com.ajkerdeal.app.android.api.models;

import com.google.gson.annotations.SerializedName;

public class DealDetailsModel {

    @SerializedName("DealId")
    public int dealId;
    @SerializedName("DealTitle")
    public String dealTitle;

    public String getImageLink() {
        return imageLink;
    }

    @SerializedName("ImageLink")
    public String imageLink;


    @SerializedName("DealPrice")
    public double dealPrice;
    @SerializedName("DealDiscount")
    public double dealDiscount;
    @SerializedName("BulletDescription")
    public String bulletDescription;
    @SerializedName("FolderName")
    public String imagePathFolderName;
    @SerializedName("AccountsTitle")
    public String accountsTitle;
    @SerializedName("RegularPrice")
    public double regularPrice;
    @SerializedName("SignupStartingDate")
    public String signupStartingDate;
    @SerializedName("SignupClosingDate")
    public String signupClosingDate;
    @SerializedName("IsShowStartingDate")
    public boolean isShowStartingDate;
    @SerializedName("QtnLimitPerUser")
    public short quantityRestrict;
    @SerializedName("QtnAfterBooking")
    public int quantityAfterBooking;
    @SerializedName("ShortDescription")
    public String shortDescription;
    @SerializedName("ProfileId")
    public int merchantId;
    @SerializedName("CompanyName")
    public String merchantName;
    @SerializedName("CategoryId")
    public int CategoryId;
    @SerializedName("SubCategoryId")
    public int SubCategoryId;
    @SerializedName("SubSubCategoryId")
    public int SubSubCategoryID;
    @SerializedName("DeliveryChargeAmount")
    public double DeliveryChargeAmount;
    @SerializedName("DeliveryChargeAmountOutSideDhaka")
    public double DeliveryChargeAmountOutSideDhaka;
    @SerializedName("Sizes")
    public String Sizes;
    @SerializedName("Colors")
    public String Colors;

    @SerializedName("MerchantInsideDhakaSpeed")
    public int MerchantInsideDhakaSpeed;
    @SerializedName("MerchantOutsideDhakaSpeed")
    public int MerchantOutsideDhakaSpeed;
    @SerializedName("Category")
    public String Category;

    @SerializedName("IsSoldOut")
    private boolean IsSoldOut;

    @SerializedName("IsDealClosed")
    private boolean IsDealClosed;

    @SerializedName("ImageCount")
    private int ImageCount;



    @SerializedName("TotalOrder")
    private int TotalOrder;

    @SerializedName("TotalReview")
    private int TotalReview;

    @SerializedName("GoodProductQuality")
    private int GoodProductQuality;

    @SerializedName("BadProductQuality")
    private int BadProductQuality;

    @SerializedName("DeliverySpeedDhaka")
    private int DeliverySpeedDhaka;

    @SerializedName("DeliverySpeedOutSideDhaka")
    private int DeliverySpeedOutSideDhaka;

    @SerializedName("AwardName")
    private String AwardName;

    @SerializedName("AwardValue")
    private int AwardValue;

    @SerializedName("AwardType")
    private int AwardType;

    @SerializedName("TotalPoint")
    private int TotalPoint;

    @SerializedName("DeliverySpeedDhakaPercentage")
    private int DeliverySpeedDhakaPercentage;

    @SerializedName("DeliverySpeedOutDhakaPercentage")
    private int DeliverySpeedOutDhakaPercentage;

    @SerializedName("ReviewPercentage")
    private int ReviewPercentage;

    @SerializedName("DeliverySpeedStarRating")
    private int DeliverySpeedStarRating;

    @SerializedName("ProductQualityStarRating")
    private float ProductQualityStarRating;

    @SerializedName("MerchantTotalOrder")
    private int MerchantTotalOrder;

    @SerializedName("MerchantTotalReview")
    private int MerchantTotalReview;

    @SerializedName("InsertedOn")
    private String InsertedOn;

    @SerializedName("CommissionPerCoupon")
    private int mCommissionPerCoupon;

    public DealDetailsModel(int mCommissionPerCoupon) {
        this.mCommissionPerCoupon = mCommissionPerCoupon;
    }

    public int getmCommissionPerCoupon() {
        return mCommissionPerCoupon;
    }

    public void setmCommissionPerCoupon(int mCommissionPerCoupon) {
        this.mCommissionPerCoupon = mCommissionPerCoupon;
    }

    @Override
    public String toString() {
        return "DealDetailsModel{" +
                "mCommissionPerCoupon=" + mCommissionPerCoupon +
                '}';
    }

    private int IsCompanyClosed;


    // Don't add serialize to these variable
    private String mImage;
    private String mImageCrazydeal;
    private int mImageLocation;


    public DealDetailsModel(int dealId, String dealTitle, double dealPrice, double dealDiscount, String bulletDescription, String imagePathFolderName, String accountsTitle, double regularPrice, String signupStartingDate, String signupClosingDate, boolean isShowStartingDate, short quantityRestrict, int quantityAfterBooking, String shortDescription, int merchantId, String merchantName, int categoryId, int subCategoryId, int subSubCategoryID, double deliveryChargeAmount, double deliveryChargeAmountOutSideDhaka, String sizes, String colors, int merchantInsideDhakaSpeed, int merchantOutsideDhakaSpeed, String category, boolean isSoldOut, boolean isDealClosed, int imageCount, int totalOrder, int totalReview, int goodProductQuality, int badProductQuality, int deliverySpeedDhaka, int deliverySpeedOutSideDhaka, String awardName, int awardValue, int awardType, int totalPoint, int deliverySpeedDhakaPercentage, int deliverySpeedOutDhakaPercentage, int reviewPercentage, int deliverySpeedStarRating, float productQualityStarRating, int merchantTotalOrder, int merchantTotalReview, String insertedOn, int isCompanyClosed, String mImage, String mImageCrazydeal, int mImageLocation) {
        this.dealId = dealId;
        this.dealTitle = dealTitle;
        this.dealPrice = dealPrice;
        this.dealDiscount = dealDiscount;
        this.bulletDescription = bulletDescription;
        this.imagePathFolderName = imagePathFolderName;
        this.accountsTitle = accountsTitle;
        this.regularPrice = regularPrice;
        this.signupStartingDate = signupStartingDate;
        this.signupClosingDate = signupClosingDate;
        this.isShowStartingDate = isShowStartingDate;
        this.quantityRestrict = quantityRestrict;
        this.quantityAfterBooking = quantityAfterBooking;
        this.shortDescription = shortDescription;
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        CategoryId = categoryId;
        SubCategoryId = subCategoryId;
        SubSubCategoryID = subSubCategoryID;
        DeliveryChargeAmount = deliveryChargeAmount;
        DeliveryChargeAmountOutSideDhaka = deliveryChargeAmountOutSideDhaka;
        Sizes = sizes;
        Colors = colors;
        MerchantInsideDhakaSpeed = merchantInsideDhakaSpeed;
        MerchantOutsideDhakaSpeed = merchantOutsideDhakaSpeed;
        Category = category;
        IsSoldOut = isSoldOut;
        IsDealClosed = isDealClosed;
        ImageCount = imageCount;
        TotalOrder = totalOrder;
        TotalReview = totalReview;
        GoodProductQuality = goodProductQuality;
        BadProductQuality = badProductQuality;
        DeliverySpeedDhaka = deliverySpeedDhaka;
        DeliverySpeedOutSideDhaka = deliverySpeedOutSideDhaka;
        AwardName = awardName;
        AwardValue = awardValue;
        AwardType = awardType;
        TotalPoint = totalPoint;
        DeliverySpeedDhakaPercentage = deliverySpeedDhakaPercentage;
        DeliverySpeedOutDhakaPercentage = deliverySpeedOutDhakaPercentage;
        ReviewPercentage = reviewPercentage;
        DeliverySpeedStarRating = deliverySpeedStarRating;
        ProductQualityStarRating = productQualityStarRating;
        MerchantTotalOrder = merchantTotalOrder;
        MerchantTotalReview = merchantTotalReview;
        InsertedOn = insertedOn;
        IsCompanyClosed = isCompanyClosed;
        this.mImage = mImage;
        this.mImageCrazydeal = mImageCrazydeal;
        this.mImageLocation = mImageLocation;
    }

    public DealDetailsModel(int dealId, String dealTitle, double dealPrice, double dealDiscount,
                            String bulletDescription, String imagePathFolderName, String accountsTitle,
                            double regularPrice, String signupStartingDate, String signupClosingDate,
                            boolean isShowStartingDate, short quantityRestrict, int quantityAfterBooking,
                            String shortDescription, int merchantId, String merchantName,
                            int ImageCount, String size, String colors) {
        this.dealId = dealId;
        this.dealTitle = dealTitle;
        this.dealPrice = dealPrice;
        this.dealDiscount = dealDiscount;
        this.bulletDescription = bulletDescription;
        this.imagePathFolderName = imagePathFolderName;
        this.accountsTitle = accountsTitle;
        this.regularPrice = regularPrice;
        this.signupStartingDate = signupStartingDate;
        this.signupClosingDate = signupClosingDate;
        this.isShowStartingDate = isShowStartingDate;
        this.quantityRestrict = quantityRestrict;
        this.quantityAfterBooking = quantityAfterBooking;
        this.shortDescription = shortDescription;
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.ImageCount = ImageCount;
        Sizes = size;
        Colors = colors;

    }


   /* public DealDetailsModel(String imagePathFolderName, int dealId, int merchantId,
                          String dealTitle, double dealPrice, int imageLocation) {

        this.dealId = dealId;
        this.merchantId = merchantId;
        this.imagePathFolderName = imagePathFolderName;
        this.dealTitle = dealTitle;
        this.dealPrice = dealPrice;
        this.mImageLocation = imageLocation;

        mImage = "http://www.ajkerdeal.com/Images/Deals/"+this.imagePathFolderName+"/1.jpg";

    }*/
   /* public DealDetailsModel(String imagePathFolderName, int dealId, int merchantId,
                          String dealTitle, double dealPrice) {

        this.dealId = dealId;
        this.merchantId = merchantId;
        this.imagePathFolderName = imagePathFolderName;
        this.dealTitle = dealTitle;
        this.dealPrice = dealPrice;

        mImage = "http://www.ajkerdeal.com/Images/Deals/"+imagePathFolderName+"/1.jpg";

    }*/

    //getDataSetCrazyDeal

    public DealDetailsModel(String imagePathFolderName, int dealId, String dealTitle, double dealPrice) {

        this.dealId = dealId;
        this.imagePathFolderName = imagePathFolderName;
        this.dealTitle = dealTitle;
        this.dealPrice = dealPrice;
        mImageCrazydeal = "http://cdn.ajkerdeal.com/Images/Deals/"+imagePathFolderName+"/mhomebanner.jpg";
        this.imagePathFolderName = imagePathFolderName;



    }

    public DealDetailsModel(int dealId, String dealTitle, double dealPrice, String imagePathFolderName, double regularPrice) {
        this.dealId = dealId;
        this.dealTitle = dealTitle;
        this.dealPrice = dealPrice;
        this.imagePathFolderName = imagePathFolderName;
        this.regularPrice = regularPrice;
    }
    ///getDataSetMerchant
    //getDataSetPopularProduct

    public DealDetailsModel(int dealId, String imagePathFolderName, String dealTitle, double dealPrice) {

        this.dealId = dealId;
        this.imagePathFolderName = imagePathFolderName;
        this.dealTitle = dealTitle;
        this.dealPrice = dealPrice;
        mImage = "http://cdn.ajkerdeal.com/Images/Deals/"+imagePathFolderName+"/1.jpg";
        this.imagePathFolderName = imagePathFolderName;

    }



    public int getTotalOrder() {
        return TotalOrder;
    }

    public int getTotalReview() {
        return TotalReview;
    }

    public int getGoodProductQuality() {
        return GoodProductQuality;
    }

    public int getBadProductQuality() {
        return BadProductQuality;
    }

    public int getDeliverySpeedDhaka() {
        return DeliverySpeedDhaka;
    }

    public int getDeliverySpeedOutSideDhaka() {
        return DeliverySpeedOutSideDhaka;
    }

    public String getAwardName() {
        return AwardName;
    }

    public int getAwardValue() {
        return AwardValue;
    }

    public int getAwardType() {
        return AwardType;
    }

    public int getTotalPoint() {
        return TotalPoint;
    }

    public int getDeliverySpeedDhakaPercentage() {
        return DeliverySpeedDhakaPercentage;
    }

    public int getDeliverySpeedOutDhakaPercentage() {
        return DeliverySpeedOutDhakaPercentage;
    }

    public int getReviewPercentage() {
        return ReviewPercentage;
    }

    public int getDeliverySpeedStarRating() {
        return DeliverySpeedStarRating;
    }

    public float getProductQualityStarRating() {
        return ProductQualityStarRating;
    }


    public String getInsertedOn() {
        return InsertedOn;
    }

    public int getMerchantTotalOrder() {
        return MerchantTotalOrder;
    }

    public int getMerchantTotalReview() {
        return MerchantTotalReview;
    }


    public int getImageCount() {
        return ImageCount;
    }

    public int getDealId() {
        return dealId;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public double getDealPrice() {
        return dealPrice;
    }

    public double getDealDiscount() {
        return dealDiscount;
    }

    public String getBulletDescription() {
        return bulletDescription;
    }

    public String getImagePathFolderName() {
        return imagePathFolderName;
    }

    public String getAccountsTitle() {
        return accountsTitle;
    }

    public double getRegularPrice() {
        return regularPrice;
    }

    public String getSignupStartingDate() {
        return signupStartingDate;
    }

    public String getSignupClosingDate() {
        return signupClosingDate;
    }

    public boolean isShowStartingDate() {
        return isShowStartingDate;
    }

    public short getQuantityRestrict() {
        return quantityRestrict;
    }

    public int getQuantityAfterBooking() {
        return quantityAfterBooking;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }
    public String getSizes() {
        return Sizes;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public int getSubCategoryId() {
        return SubCategoryId;
    }

    public int getSubSubCategoryID() {
        return SubSubCategoryID;
    }

    public double getDeliveryChargeAmount() {
        return DeliveryChargeAmount;
    }

    public double getDeliveryChargeAmountOutSideDhaka() {
        return DeliveryChargeAmountOutSideDhaka;
    }

    public String getColors() {
        return Colors;
    }

    public String getmImage(){return  mImage;}

    public String getmImageCrazydeal(){return mImageCrazydeal;}

    public int getmImageLocation() {
        return mImageLocation;
    }

    public void setmImageLocation(int mImageLocation) {
        this.mImageLocation = mImageLocation;
    }
    public int getMerchantInsideDhakaSpeed() {
        return MerchantInsideDhakaSpeed;
    }

    public int getMerchantOutsideDhakaSpeed() {
        return MerchantOutsideDhakaSpeed;
    }

    public String getCategory() {
        return Category;
    }

    public boolean isSoldOut() {
        return IsSoldOut;
    }

    public boolean isDealClosed() {
        return IsDealClosed;
    }

    public void setIsCompanyClosed(int isCompanyClosed) {
        IsCompanyClosed = isCompanyClosed;
    }

    public int getIsCompanyClosed() {
        return IsCompanyClosed;
    }

}