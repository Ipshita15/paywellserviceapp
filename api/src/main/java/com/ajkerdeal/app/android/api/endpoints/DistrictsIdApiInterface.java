package com.ajkerdeal.app.android.api.endpoints;

import java.util.List;

import com.ajkerdeal.app.android.api.models.DistrictsnIdsModel;
import com.ajkerdeal.app.android.api.models.LocationIdModel;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by mitu on 5/31/16.
 */
public interface DistrictsIdApiInterface {

    @GET("/CustomersOrder/GetDistricts")
    Call<List<DistrictsnIdsModel>> getDistrictsnIds();

    @GET("/CustomersOrder/GetLocation/14")
    Call<List<LocationIdModel>> getLocationIds();


}
