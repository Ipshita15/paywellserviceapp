package com.ajkerdeal.app.android.api.endpoints;

import com.ajkerdeal.app.android.api.models.PaywellRequestBody;
import com.ajkerdeal.app.android.api.models.PaywellResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Sumaya on 10-Oct-17.
 */

public interface PaywellCancelInterface {
    @Headers({
            "API_KEY: Ajkerdeal_~La?Rj73FcLm",
            "Authorization: Basic UGF5d2VsbDpfUjRbNWNIV1Nj"
    })
    @POST("/ThirdPartyOrderAction/ChangedStatus")
    Call<PaywellResponse> getCancelOrder(@Body PaywellRequestBody paywellRequestBody);
}
