package com.ajkerdeal.app.android.api.endpoints;

import java.util.List;

import com.ajkerdeal.app.android.api.models.Comment;
import com.ajkerdeal.app.android.api.models.Feedback;
import com.ajkerdeal.app.android.api.models.ReviewResposneModel;
import com.ajkerdeal.app.android.api.models.WishlistIndexCount;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Rasel on 6/26/2016.
 */
public interface Review {


    @POST("/ProductRating/RatingDetails/{dealId}")
    Call<List<Comment>> getRatingInformation(@Path("dealId") int dealID, @Body WishlistIndexCount wishlistIndexCount);


    @POST("/ProductRating/FeedBack")
    Call<ReviewResposneModel> sendFeedBack(@Body Feedback feedback);
}
