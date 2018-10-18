package com.ajkerdeal.app.android.api.endpoints;

import com.ajkerdeal.app.android.api.models.CartOrderModel;
import com.ajkerdeal.app.android.api.models.CartOrderReferenceModel;
import com.ajkerdeal.app.android.api.models.CartOrderRequestBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by mitu on 4/16/17.
 */

public interface CartOrderInterface {
    @POST("/CustomersOrder/CartOrder/")
    Call<List<CartOrderModel>> putCartOrder(@Body List<CartOrderRequestBody> cartOrderRequestBodyList);

    @GET("/CustomersOrder/MultipleReferenceId/{cartId}")
    Call<CartOrderReferenceModel> getReferenceID(@Path("cartId") int cartId);
}
