package com.ajkerdeal.app.android.api.endpoints;

import java.util.List;

import com.ajkerdeal.app.android.api.models.NewArrivalPayLoadRequestBody;
import com.ajkerdeal.app.android.api.models.NewArrivalProductModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by pony on 1/25/17.
 */

public interface NewArrivalProductInterface {
    @POST("/AppApi/Home/JustInDealsWithLimit")
    Call<List<NewArrivalProductModel>> getNewProduct(@Body NewArrivalPayLoadRequestBody newArrivalPayLoadRequestBody);
}
