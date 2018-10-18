package com.ajkerdeal.app.android.api.models;

/**
 * Created by piash on 3/19/16.
 */
public class CategorywiseProductModel {
    private int DealId;
    private String DealTitle;
    private String AccountsTitle;
    private String FolderName;
    private String ImageLink;
    private String CustomiseMsg;
    private double DealPrice;
    private double DealDiscount;
    private double RegularPrice;
    private boolean IsSoldOut;
    private int DealPriority;
    private int ProfileId ;
    private String CompanyName;
    private int CategoryId;
    private String Category;
    private String CategoryEng;
    private int SubCategoryId;
    private String SubCategory;
    private String SubCategoryEng;
    private int SubSubCategoryId;
    private String SubSubCategory;
    private String SubSubCategoryEng;
    private int Total;
    private int ShowingPriority;
    private int imageWishList;

    private int AwardType;
    private int AwardValue;
    private int TotalPoint;
    private String AwardName;


    public CategorywiseProductModel(int dealId, String dealTitle, String accountsTitle, String folderName, String imageLink, String customiseMsg, double dealPrice, double dealDiscount, double regularPrice, boolean isSoldOut, int dealPriority, int profileId, String companyName, int categoryId, String category, String categoryEng, int subCategoryId, String subCategory, String subCategoryEng, int subSubCategoryId, String subSubCategory, String subSubCategoryEng, int total, int showingPriority, int imageWishList, int awardType, int awardValue, int totalPoint, String awardName) {
        DealId = dealId;
        DealTitle = dealTitle;
        AccountsTitle = accountsTitle;
        FolderName = folderName;
        ImageLink = imageLink;
        CustomiseMsg = customiseMsg;
        DealPrice = dealPrice;
        DealDiscount = dealDiscount;
        RegularPrice = regularPrice;
        IsSoldOut = isSoldOut;
        DealPriority = dealPriority;
        ProfileId = profileId;
        CompanyName = companyName;
        CategoryId = categoryId;
        Category = category;
        CategoryEng = categoryEng;
        SubCategoryId = subCategoryId;
        SubCategory = subCategory;
        SubCategoryEng = subCategoryEng;
        SubSubCategoryId = subSubCategoryId;
        SubSubCategory = subSubCategory;
        SubSubCategoryEng = subSubCategoryEng;
        Total = total;
        ShowingPriority = showingPriority;
        this.imageWishList = imageWishList;
        AwardType = awardType;
        AwardValue = awardValue;
        TotalPoint = totalPoint;
        AwardName = awardName;
    }


    public int getDealId() {
        return DealId;
    }

    public String getDealTitle() {
        return DealTitle;
    }

    public String getAccountsTitle() {
        return AccountsTitle;
    }

    public String getFolderName() {
        return FolderName;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public String getCustomiseMsg() {
        return CustomiseMsg;
    }

    public double getDealPrice() {
        return DealPrice;
    }

    public double getDealDiscount() {
        return DealDiscount;
    }

    public double getRegularPrice() {
        return RegularPrice;
    }

    public boolean isSoldOut() {
        return IsSoldOut;
    }

    public int getDealPriority() {
        return DealPriority;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public String getCategory() {
        return Category;
    }

    public String getCategoryEng() {
        return CategoryEng;
    }

    public int getSubCategoryId() {
        return SubCategoryId;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public String getSubCategoryEng() {
        return SubCategoryEng;
    }

    public int getSubSubCategoryId() {
        return SubSubCategoryId;
    }

    public String getSubSubCategory() {
        return SubSubCategory;
    }

    public String getSubSubCategoryEng() {
        return SubSubCategoryEng;
    }

    public int getTotal() {
        return Total;
    }

    public int getShowingPriority() {
        return ShowingPriority;
    }

    public int getImageWishList() {
        return imageWishList;
    }

    public int getAwardType() {
        return AwardType;
    }

    public int getAwardValue() {
        return AwardValue;
    }

    public int getTotalPoint() {
        return TotalPoint;
    }

    public String getAwardName() {
        return AwardName;
    }


    @Override
    public String toString() {
        return "CategorywiseProductModel{" +
                "DealId=" + DealId +
                ", DealTitle='" + DealTitle + '\'' +
                ", AccountsTitle='" + AccountsTitle + '\'' +
                ", FolderName='" + FolderName + '\'' +
                ", ImageLink='" + ImageLink + '\'' +
                ", CustomiseMsg='" + CustomiseMsg + '\'' +
                ", DealPrice=" + DealPrice +
                ", DealDiscount=" + DealDiscount +
                ", RegularPrice=" + RegularPrice +
                ", IsSoldOut=" + IsSoldOut +
                ", DealPriority=" + DealPriority +
                ", ProfileId=" + ProfileId +
                ", CompanyName='" + CompanyName + '\'' +
                ", CategoryId=" + CategoryId +
                ", Category='" + Category + '\'' +
                ", CategoryEng='" + CategoryEng + '\'' +
                ", SubCategoryId=" + SubCategoryId +
                ", SubCategory='" + SubCategory + '\'' +
                ", SubCategoryEng='" + SubCategoryEng + '\'' +
                ", SubSubCategoryId=" + SubSubCategoryId +
                ", SubSubCategory='" + SubSubCategory + '\'' +
                ", SubSubCategoryEng='" + SubSubCategoryEng + '\'' +
                ", Total=" + Total +
                ", ShowingPriority=" + ShowingPriority +
                ", imageWishList=" + imageWishList +
                ", AwardType=" + AwardType +
                ", AwardValue=" + AwardValue +
                ", TotalPoint=" + TotalPoint +
                ", AwardName='" + AwardName + '\'' +
                '}';
    }
}
