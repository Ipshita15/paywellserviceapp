package com.ajkerdeal.app.android.api.endpoints;

import com.ajkerdeal.app.android.api.models.ReferenceIdModel;
import com.ajkerdeal.app.android.api.models.SingleOrderModel;
import com.ajkerdeal.app.android.api.models.SinglorderKeyModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by mitu on 5/31/16.
 */
public interface SingleOrderApiInterface {
    @POST("/CustomersOrder/SingleOrder/")
    Call<SinglorderKeyModel> putSingleOrder(@Body SingleOrderModel singleOrderModel);

    @GET("/CustomersOrder/ReferenceId/{couponId}")
    Call<ReferenceIdModel> getCouponId(@Path("couponId") int couponId);
}
