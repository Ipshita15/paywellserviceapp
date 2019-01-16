package com.cloudwell.paywell.services.retrofit;


import com.cloudwell.paywell.services.activity.refill.model.BranchData;
import com.cloudwell.paywell.services.activity.refill.model.DistrictData;
import com.cloudwell.paywell.services.activity.refill.model.RefillRequestData;
import com.cloudwell.paywell.services.activity.topup.model.RequestTopup;
import com.cloudwell.paywell.services.activity.topup.model.TopupReposeData;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.model.RequestBillStatusData;
import com.cloudwell.paywell.services.app.model.APIResposeGenerateToken;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/29/18.
 */
public interface APIService {


    @POST("paywellapi/PaywellAuthentication/GenerateToken")
    @FormUrlEncoded
    Call<APIResposeGenerateToken> callGenerateToken(@Url String ur, @Header("Authorization") String AuthorizationKey, @FieldMap Map<String, String> params);

    @POST("PaywellTopUpService/PaywellTopup")
    @Multipart
    Call<TopupReposeData> callTopAPI(@Part("requestData") RequestTopup requestTopup);


    @POST("PayWellBankDepositSystem/getDistrictListforBankDeposit")
    @FormUrlEncoded
    Call<DistrictData> callDistrictDataAPI(@Field("username") String username,
                                           @Field("bankId") String bankId);

    @POST("PayWellBankDepositSystem/getBankBranch")
    @FormUrlEncoded
    Call<BranchData> callBranchDataAPI(@Field("username") String username,
                                       @Field("bankId") String bankId,
                                       @Field("districtId") String districtId);

    @POST("PayWellBankDepositSystem/depositBankSlip")
    @FormUrlEncoded
    Call<RefillRequestData> callBalanceRefillAPI(@Field("username") String username,
                                                 @Field("bankId") String bankId,
                                                 @Field("districtId") String districtId,
                                                 @Field("branchId") String branchId,
                                                 @Field("depositslip") String depositslip);

    @POST("PaywelltransactionPollyBiddyut/pollyBiddyutBillStatusQuery")
    @FormUrlEncoded
    Call<RequestBillStatusData> callBillStatusRequestAPI(@Field("username") String username,
                                                         @Field("pass") String password,
                                                         @Field("account_no") String accountNo,
                                                         @Field("month") String month,
                                                         @Field("year") String year,
                                                         @Field("format") String format);
}


