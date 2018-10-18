package com.ajkerdeal.app.android.api.endpoints;

import java.util.List;

import com.ajkerdeal.app.android.api.models.CategoryModel;
import com.ajkerdeal.app.android.api.models.SubcategoriesViewModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Rasel on 5/14/2016.
 */
public interface CategorySelectionApiInterface {


    @GET("AppApi/Categories/GetCategoryData")
    Call<List<CategoryModel>> getCategories();


    @GET("/AppApi/Categories/GetSubCategories/{subcategoryId}")
    Call<SubcategoriesViewModel> getSubcategoriesAndSubSubcategories(@Path("subcategoryId") int subcategoryId);
}
