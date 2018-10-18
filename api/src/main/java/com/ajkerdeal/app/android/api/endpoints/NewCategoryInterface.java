package com.ajkerdeal.app.android.api.endpoints;

import java.util.List;

import com.ajkerdeal.app.android.api.models.NewCategoryModels;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Sumaya on 12-Oct-17.
 */

public interface NewCategoryInterface {
    @GET("/AppApi/Home/HomeMenu")
    Call<List<NewCategoryModels>> getCategory();
}
