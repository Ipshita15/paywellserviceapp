package com.ajkerdeal.app.android.api.endpoints;


import java.util.List;

import com.ajkerdeal.app.android.api.models.BannerModel;
import com.ajkerdeal.app.android.api.models.EventHomePagePayLoad;
import com.ajkerdeal.app.android.api.models.HomeCategoryBlockModel;
import com.ajkerdeal.app.android.api.models.HomeCategoryModel;
import com.ajkerdeal.app.android.api.models.NavigationDrawerCategoryModel;
import com.ajkerdeal.app.android.api.models.NewArrivalModel;
import com.ajkerdeal.app.android.api.models.NewArrivalPayLoad;
import com.ajkerdeal.app.android.api.models.PromotionBannerModel;
import com.ajkerdeal.app.android.api.models.PromotionBannerReqModel;
import com.ajkerdeal.app.android.api.models.UserTrackingModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by has9 on 4/20/16.
 */

public interface HomeApiInterface {

    @GET("/AppApi/Home/HomeCategoriesV2")
    Call<List<HomeCategoryModel>> getHomeCategories();

    @GET("/AppApi/Home/HomeProductBlocks")
    Call<List<HomeCategoryBlockModel>> getHomeProductBlocks();

    @GET("/Banner/AppBanner")
    Call<List<BannerModel>> getBanner();

    @GET("/AppApi/Home/NavDrawerCategories")
    Call<List<NavigationDrawerCategoryModel>> getNavigationDrawerCategories();

    @POST("/CustomerInfo/UserTracking")
    Call<String> getTrackingId(@Body UserTrackingModel userTrackingModel);

    @GET("/Banner/PromotionBanner")
    Call<List<PromotionBannerModel>> getPromotion();

    @POST("/Banner/DealsFilterByBannar?{categoryId}")
    Call<List<PromotionBannerReqModel>> getPromotionData(@Path("categoryId") String categoryId);

    @GET("/Event/EventPercentage/{eventID}")
    Call<EventHomePagePayLoad> getEventPayloadForHome(@Path("eventID") int eventId);

    @POST("/AppApi/Home/JustInDeals")
    Call<List<NewArrivalPayLoad>> getHomeNewArrival(@Body NewArrivalModel newArrivalModel );

}
