package com.ajkerdeal.app.android.api.endpoints;


import java.util.List;

import com.ajkerdeal.app.android.api.models.CountModel;
import com.ajkerdeal.app.android.api.models.DealDetailsModel;
import com.ajkerdeal.app.android.api.models.LimitModel;
import com.ajkerdeal.app.android.api.models.PhoneOrderModel;
import com.ajkerdeal.app.android.api.models.Ratings;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by mitu on 4/20/16.
 */
public interface DealDetailsApiInterface {

    @GET
    Call<List<DealDetailsModel>> getDealDetailsOthersMerchantRequest(@Url String url);

    @GET("/DealDetails/GetDealDetails/{DealId}")
    Call<DealDetailsModel> getDealDetails(@Path("DealId") int dealId);

    @GET("/ProductRating/GetRating/{dealId}")
    Call<List<Ratings>> getRatingInformation(@Path("dealId") int dealID);

    @POST("/CrazyDeals/GetCrazyDeals")
    Call<List<DealDetailsModel>> getDealDetailsCrazyDealsRequest(@Body LimitModel limitModelDealDetails);

    @GET("/DealDetails/GetCateroryRelatedDeals/{dealId}/{categoryId}/{subcategoryId}/{dealLimit}")
    Call<List<DealDetailsModel>> getPopularProduct(@Path("dealId") int dealId, @Path("categoryId") int categoryId ,
                                                   @Path("subcategoryId") int subcategoryId, @Path("dealLimit") int dealLimit);

    @POST("/CustomersOrder/PhoneOrder")
    Call<CountModel> insertLogInformation(@Body PhoneOrderModel phoneOrderModel);

}
