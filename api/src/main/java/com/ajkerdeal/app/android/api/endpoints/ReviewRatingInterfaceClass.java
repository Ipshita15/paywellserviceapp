package com.ajkerdeal.app.android.api.endpoints;

import java.util.List;

import com.ajkerdeal.app.android.api.models.ReviewRatingModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by mitu on 6/14/17.
 */

public interface ReviewRatingInterfaceClass {
    @GET("/Merchant/GetMerchantInfo/{merchantId}")
    Call<List<ReviewRatingModel>> getAllData(@Path("merchantId") int merchantId);
}
