package com.ajkerdeal.app.android.api.endpoints;

import com.ajkerdeal.app.android.api.models.ElasticSearachProduct;
import com.ajkerdeal.app.android.api.models.ElasticSearchModel;
import com.ajkerdeal.app.android.api.models.SearchProductRequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by mitu on 7/20/17.
 */

public interface ElasticSearchApiInterface {
    @GET("/Search/SearchSuggetion/{keyword}")
    Call<ElasticSearchModel> getSearchSuggetion(@Path("keyword") String value);

    /*@GET("/Search/SearchSuggetion/{keyword}/{appId}/{deviceId}/{firebaseId}/{customerId}")
    Call<ElasticSearchModel> getSearchSuggetion(@Path("keyword") String value, @Path("appId") String appId, @Path("deviceId") String deviceId, @Path("firebaseId") String firebaseId, @Path("customerId") String customerId);*/

    @POST("/search/SearchProduct/{index}/{count}")
    Call<ElasticSearachProduct>getSearchProduct(@Path("index") int index, @Path("count") int count, @Body SearchProductRequestBody searchProductRequestBody);

    /*@POST("/search/SearchProduct/{index}/{count}/{appId}/{deviceId}/{firebaseId}/{customerId}")
    Call<ElasticSearachProduct>getSearchProduct(@Path("index") int index, @Path("count")int count, @Path("appId") String appId, @Path("deviceId") String deviceId, @Path("firebaseId") String firebaseId, @Path("customerId") String customerId, @Body SearchProductRequestBody searchProductRequestBody);*/


}
