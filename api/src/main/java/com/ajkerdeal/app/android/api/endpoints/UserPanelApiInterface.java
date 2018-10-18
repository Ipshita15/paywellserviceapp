package com.ajkerdeal.app.android.api.endpoints;

import java.util.List;

import com.ajkerdeal.app.android.api.models.CountModel;
import com.ajkerdeal.app.android.api.models.CustomerInfoOrderListModel;
import com.ajkerdeal.app.android.api.models.CustomerOrderListLimitModel;
import com.ajkerdeal.app.android.api.models.UpdateUserInformationModel;
import com.ajkerdeal.app.android.api.models.UpdateUserInformationReturnModel;
import com.ajkerdeal.app.android.api.models.UpdateUserPasswordModel;
import com.ajkerdeal.app.android.api.models.UpdateUserPasswordReturnModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by mitu on 6/16/16.
 */
public interface UserPanelApiInterface {


    @POST("/CustomerInfo/OrderList/{customerId}/{status}")
    Call<List<CustomerInfoOrderListModel>> getCustomerInfo(@Path("customerId") int customerId,@Path("status") int status, @Body CustomerOrderListLimitModel customerOrderListLimitModel);

    @POST("/CustomerInfo/UpdatePersonalInfo/")
    Call<UpdateUserInformationReturnModel> getUpdatePersonalInformation(@Body UpdateUserInformationModel updateUserInformationModel);

    @POST("/CustomerAccess/UpdatePassword/{CustomerId}")
    Call<UpdateUserPasswordReturnModel> getUpdatePassword(@Path("CustomerId") int CustomerId, @Body UpdateUserPasswordModel updateUserPasswordModel);

    @GET("/CustomerInfo/OrderListCount/{customerId}/{status}")
    Call<CountModel> getTotalCountofOrderlist(@Path("customerId") int customerId,@Path("status") int status);


}
