package com.ajkerdeal.app.android.api.endpoints;

import java.util.List;

import com.ajkerdeal.app.android.api.models.CountModel;
import com.ajkerdeal.app.android.api.models.DealDetailsModel;
import com.ajkerdeal.app.android.api.models.LimitModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by has9 on 5/4/16.
 */
public interface CrazyDealApiInterface {
    @POST("CrazyDeals/GetCrazyDeals")
    Call<List<DealDetailsModel>> getCrazyDeals(@Body LimitModel Limits);

    @GET("CrazyDeals/GetCrazyDealsCount")
    Call<CountModel> getCrazyCount();
}
