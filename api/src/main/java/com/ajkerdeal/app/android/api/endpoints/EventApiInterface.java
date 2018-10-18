package com.ajkerdeal.app.android.api.endpoints;

import java.util.List;

import com.ajkerdeal.app.android.api.models.EventCategoryWiseDealsCount;
import com.ajkerdeal.app.android.api.models.EventDealsCount;
import com.ajkerdeal.app.android.api.models.LiveEventsModel;
import com.ajkerdeal.app.android.api.models.PostEventCategoryWiseDealsCount;
import com.ajkerdeal.app.android.api.models.PostEventDealsCount;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by MhRaju on 1/12/2017.
 */
public interface EventApiInterface {

    @GET("/Event/LiveEvents")
    Call<LiveEventsModel> getLiveEventsModelCall();

    @POST("/Event/EventCategoryWiseDealsCount")
    Call<List<EventCategoryWiseDealsCount>> getEventCategoryWiseDealsCount(@Body PostEventCategoryWiseDealsCount postEventCategoryWiseDealsCount);

/*    @POST("/Event/EventDeals")
    Call<List<EventDealsModel>> getEventDealsModel(@Body PostEventDealsModel postEventDealsModel);*/

    @POST("/Event/EventDealsCount")
    Call<EventDealsCount> getEventDealsCount(@Body PostEventDealsCount postEventDealsCount);
}
