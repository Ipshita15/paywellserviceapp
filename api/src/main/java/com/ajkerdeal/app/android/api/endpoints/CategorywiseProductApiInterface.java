package com.ajkerdeal.app.android.api.endpoints;


import java.util.List;

import com.ajkerdeal.app.android.api.models.BannerFilterRequestBody;
import com.ajkerdeal.app.android.api.models.BrandModel;
import com.ajkerdeal.app.android.api.models.CategorywiseCountModel;
import com.ajkerdeal.app.android.api.models.CategorywiseFilterCountModel;
import com.ajkerdeal.app.android.api.models.CategorywiseFilterLimitModel;
import com.ajkerdeal.app.android.api.models.CategorywiseLimitModel;
import com.ajkerdeal.app.android.api.models.CategorywisePriceRangeModel;
import com.ajkerdeal.app.android.api.models.CategorywiseProductModel;
import com.ajkerdeal.app.android.api.models.CountBannerFilter;
import com.ajkerdeal.app.android.api.models.CountModel;
import com.ajkerdeal.app.android.api.models.CwiseBodyModel;
import com.ajkerdeal.app.android.api.models.CwiseModel;
import com.ajkerdeal.app.android.api.models.CwisewithCountModel;
import com.ajkerdeal.app.android.api.models.EventBody;
import com.ajkerdeal.app.android.api.models.EventPayload;
import com.ajkerdeal.app.android.api.models.EventTotalCountdBody;
import com.ajkerdeal.app.android.api.models.MerchantwiseProductBody;
import com.ajkerdeal.app.android.api.models.MerchantwiseProductCountBody;
import com.ajkerdeal.app.android.api.models.NinetyNineModelBody;
import com.ajkerdeal.app.android.api.models.NinetyNineShopCountBody;
import com.ajkerdeal.app.android.api.models.WishlistBody;
import com.ajkerdeal.app.android.api.models.WishlistCountModel;
import com.ajkerdeal.app.android.api.models.WishlistIndexCount;
import com.ajkerdeal.app.android.api.models.WishlistModelAll;
import com.ajkerdeal.app.android.api.models.WishlistModelWithoutUserId;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by piash on 4/23/16.
 */
public interface CategorywiseProductApiInterface {

    @POST("/AppApi/Categories/GetCategoryProduct/{isFirstLoad}")
    public Call<CwisewithCountModel> getCategorywiseProductAll(@Body CwiseBodyModel cwiseBodyModel, @Path("isFirstLoad") boolean isFistLoad);

    @GET("/AppApi/Categories/InitialProducts/{categoryId}/{subCategoryId}/{subSubCategoryId}")
    public Call<List<CategorywiseProductModel>> getCategorywiseProductInitialInterface(@Path("categoryId") int categoryId, @Path("subCategoryId") int subCategoryId, @Path("subSubCategoryId") int subSubCategoryId);

    @GET("/AppApi/Categories/GetCategoryProductCount/{categoryId}/{subCategoryId}/{subSubCategoryId}")
    public Call<CategorywiseCountModel> getCategorywiseProductCount(@Path("categoryId") int categoryId, @Path("subCategoryId") int subCategoryId, @Path("subSubCategoryId") int subSubCategoryId);

    @POST("/AppApi/Categories/Products/{categoryId}/{subCategoryId}/{subSubCategoryId}")
    public Call<List<CategorywiseProductModel>> getCategorywiseProductEndless(@Path("categoryId") int categoryId, @Path("subCategoryId") int subCategoryId, @Path("subSubCategoryId") int subSubCategoryId, @Body CategorywiseLimitModel categorywiseLimitModel);


    @POST("/AppApi/Categories/GetCategoryProductCount/{categoryId}/{subCategoryId}/{subSubCategoryId}")
    Call<CategorywiseFilterCountModel> getCategorywiseFilterProductCount(@Path("categoryId") int categoryId, @Path("subCategoryId") int subCategoryId, @Path("subSubCategoryId") int subSubCategoryId, @Body CategorywiseFilterLimitModel categorywiseFilterLimitModel);

    @GET
    Call<List<BrandModel>> getCategorywiseProductByBand(@Url String url);

    @GET("/filter/GetPriceRangeForFilter/{categoryId}/{subCategoryId}")
    Call<List<CategorywisePriceRangeModel>> getCategorywiseProductPriceRange(@Path("categoryId") int categoryId, @Path("subCategoryId") int subCategoryId);

    @POST("/Wishlist/AddMultiple/")
    Call<WishlistCountModel> insertWishlistToServer(@Body WishlistBody wishlistBody);

    @GET("/Wishlist/GetWishlistProducts/{customerId}")
    Call<List<WishlistModelAll>> getWishlistAll(@Path("customerId") int id);

    @GET("/Wishlist/WishlistProductsCount/{customerId}")
    Call<WishlistCountModel> getwishlistTotalCount(@Path("customerId") int customerId);

    @POST("/Wishlist/LoadWishlistProducts")
    Call<List<WishlistModelWithoutUserId>> getWishlistAllWithoutUserId(@Body List<Integer> wishListBodyWithoutUserId);

    @POST("/Wishlist/UnFavWishlistProducts/{customerId}")
    Call<WishlistCountModel> deleteFromWishList(@Body List<Integer> wishlistUnFavModel, @Path("customerId") int customerId);

    @POST("/Wishlist/GetWishlistProducts/{customerId}")
    Call<List<WishlistModelAll>> getWishlistAllEndless(@Body WishlistIndexCount wishlistIndexCount, @Path("customerId") int customerId );

    @POST("/AppApi/MerchantwiseProduct/GetProducts")
    Call<List<CategorywiseProductModel>> getAllMerchantwiseProduct(@Body MerchantwiseProductBody merchantwiseProductBody);

    @POST("/AppApi/MerchantwiseProduct/GetProductCount")
    Call<WishlistCountModel> getAllMerchantwiseProductCount(@Body MerchantwiseProductCountBody merchantwiseProductCountBody);

    @POST("/AppApi/Categories/LoadPricewiseProducts")
    Call<List<CategorywiseProductModel>> getNinetyNineShopProduct(@Body NinetyNineModelBody body);

    @POST("/AppApi/Categories/ProductCountWithRange")
    Call<WishlistCountModel> getNinetyNineShopTotalCount(@Body NinetyNineShopCountBody body);

    @POST("/Recommends/RecommendedProducts")
    Call<List<WishlistModelWithoutUserId>> getRecommendedProducts(@Body List<Integer> wishListBodyWithoutUserId);

    @POST("/Event/EventDeals")
    Call<List<EventPayload>> getEventDeals(@Body EventBody eventBody);

    @POST("/Event/EventDealsCount")
    Call<CountModel> getEventTotalCount(@Body EventTotalCountdBody eventTotalCountdBody);


    @POST
    Call<List<CwiseModel>> getAllBannerProduct(@Url String url,@Body BannerFilterRequestBody bannerFilterRequestBody);

    @POST
    Call<CountBannerFilter> getBannerFileterCount(@Url String url, @Body BannerFilterRequestBody bannerFilterRequestBody);


   /* @POST("/Banner/{actionUrl}")
    Call<List<EventPayload>> getEventDeals(@Body EventBody eventBody);

    @POST("/Event/EventDealsCount")
    Call<CountModel> getEventTotalCount(@Body EventTotalCountdBody eventTotalCountdBody);

*/
}
