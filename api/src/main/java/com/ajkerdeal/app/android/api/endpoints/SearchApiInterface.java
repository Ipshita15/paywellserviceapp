package com.ajkerdeal.app.android.api.endpoints;

import java.util.List;

import com.ajkerdeal.app.android.api.models.DealDetailsModel;
import com.ajkerdeal.app.android.api.models.SearchByCategoryWiseDealsModel;
import com.ajkerdeal.app.android.api.models.SearchCountRequestModel;
import com.ajkerdeal.app.android.api.models.SearchCountResponseModel;
import com.ajkerdeal.app.android.api.models.SearchDataWithCountRequestModel;
import com.ajkerdeal.app.android.api.models.SearchDataWithCountResponsModel;
import com.ajkerdeal.app.android.api.models.SearchKeyModel;
import com.ajkerdeal.app.android.api.models.SearchModels;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mitu on 4/23/16.
 */
public interface SearchApiInterface {
    @POST("/Search/SearchByKeywords")
    Call<SearchModels> getSearchRequest(@Body SearchKeyModel searchKeyModel);

    @POST("/Search/GetSearchDataCount")
    Call<SearchCountResponseModel> getSearchCount(@Body SearchCountRequestModel searchCountRequestModel);

    @POST("/Search/SearchData")
    Call<List<SearchDataWithCountResponsModel>> getSearchDatawithcount (@Body SearchDataWithCountRequestModel searchDataWithCountRequestModel);

    @POST("/Search/SearchByCategoryWiseDeals")
    Call<List<DealDetailsModel>> getSearchByCategoryWiseDeals(@Body SearchByCategoryWiseDealsModel searchByCategoryWiseDealsModel);

  /*  @POST("/Search/CompanyDeals")
    Call<List<SearchInMerchantResponsModel>> getSearchMerchantData(@Body SearchInMerchantRequestModel searchInMerchantRequestModel);
*/
}
