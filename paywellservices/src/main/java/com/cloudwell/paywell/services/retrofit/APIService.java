package com.cloudwell.paywell.services.retrofit;


import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.ReposeAirSearch;
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.RequestAirSearch;
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.ResGetAirports;
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList;
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.model.ResCancellationMapping;
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model.ResIssueTicket;
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.RequestAirPrebookingSearchParamsForServer;
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.ResAirPreBooking;
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.ResBookingAPI;
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.RequestAirPriceSearch;
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.ResposeAirPriceSearch;
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.airRules.ResposeAirRules;
import com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.model.ResCommistionMaping;
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.model.ResInvoideEmailAPI;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResGetBusListData;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResPaymentBookingAPI;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResSeatCheckBookAPI;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.transactionLog.TransactionLogDetailsModel;
import com.cloudwell.paywell.services.activity.notification.model.ResNotificationAPI;
import com.cloudwell.paywell.services.activity.notification.model.ResNotificationReadAPI;
import com.cloudwell.paywell.services.activity.notification.model.ResposeReScheduleNotificationAccept;
import com.cloudwell.paywell.services.activity.product.ekShop.model.ResEKReport;
import com.cloudwell.paywell.services.activity.product.ekShop.model.ResEkShopToken;
import com.cloudwell.paywell.services.activity.refill.model.BranchData;
import com.cloudwell.paywell.services.activity.refill.model.DistrictData;
import com.cloudwell.paywell.services.activity.refill.model.RefillRequestData;
import com.cloudwell.paywell.services.activity.topup.model.RequestTopup;
import com.cloudwell.paywell.services.activity.topup.model.TopupReposeData;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.model.RequestBillStatusData;
import com.cloudwell.paywell.services.app.model.APIResBalanceCheck;
import com.cloudwell.paywell.services.app.model.APIResposeGenerateToken;
import com.cloudwell.paywell.services.service.notificaiton.model.APIResNoCheckNotification;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/29/18.
 */
public interface APIService {


    @POST()
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
                                                 @Field("depositslip") String depositslip,
                                                 @Field("Amount") String amount);

    @POST("PaywelltransactionPollyBiddyut/pollyBiddyutBillStatusQuery")
    @FormUrlEncoded
    Call<RequestBillStatusData> callBillStatusRequestAPI(@Field("username") String username,
                                                         @Field("pass") String password,
                                                         @Field("account_no") String accountNo,
                                                         @Field("month") String month,
                                                         @Field("year") String year,
                                                         @Field("format") String format);

    @POST("RetailerService/checkBalance")
    @FormUrlEncoded
    Call<APIResBalanceCheck> callCheckBalance(@Field("username") String username);

    @POST("RetailerService/checkNotificationDetails")
    @FormUrlEncoded
    Call<APIResNoCheckNotification> callCheckNotification(@Field("username") String username);

    @POST
    @FormUrlEncoded
    Call<ResNotificationAPI> callNotificationAPI(@Url String ur,
                                                 @Field("username") String username,
                                                 @Field("mes_type") String mesType,
                                                 @Field("message_status") String messageStatus,
                                                 @Field("format") String format);

    @POST
    @FormUrlEncoded
    Call<ResNotificationReadAPI> callNotificationReadAPI(@Url String url,
                                                         @Field("username") String username,
                                                         @Field("message_id") String messageId,
                                                         @Field("format") String format);


    @Multipart
    @POST("PaywelltransactionHaltrip/airSearch")
    Call<ReposeAirSearch> callAirSearch(@Part("username") String username,
                                        @Part("search_params") RequestAirSearch search_params);


    @Multipart
    @POST("PaywelltransactionHaltrip/airPriceSearch")
    Call<ResposeAirPriceSearch> callairPriceSearch(@Part("username") String username,
                                                   @Part("search_params") RequestAirPriceSearch search_params);


    @Multipart
    @POST("PaywelltransactionHaltrip/airRulesSearch")
    Call<ResposeAirRules> airRulesSearch(@Part("username") String username,
                                         @Part("search_params") RequestAirPriceSearch search_params);


    @GET("PaywelltransactionHaltrip/getAirports?")
    Call<ResGetAirports> getAirports(@Query("username") String username, @Query("format") String format, @Query("iso") String iso);

    @FormUrlEncoded
    @POST("PaywelltransactionHaltrip/getBookingList")
    Call<BookingList> callAirBookingListSearch(@Field("username") String username,
                                               @Field("limit") int limit);

    @Multipart
    @POST("PaywelltransactionHaltrip/airPreBooking")
    Call<ResAirPreBooking> airPreBooking(@Part("username") String username,
                                         @Part("format") String format,
                                         @Part("search_params") RequestAirPrebookingSearchParamsForServer search_params);


    @Multipart
    @POST("PaywelltransactionHaltrip/airBooking")
    Call<ResBookingAPI> airBooking(@Part("username") String username,
                                   @Part("password") String password,
                                   @Part("format") String format,
                                   @Part("search_params") RequestAirPrebookingSearchParamsForServer search_params);

    @POST("PaywelltransactionHaltrip/cancelBooking")
    @FormUrlEncoded
    Call<JsonObject> cancelBooking(@Field("username") String username,
                                   @Field("password") String password,
                                   @Field("BookingID") String bookingId,
                                   @Field("reason") String cancelReason,
                                   @Field("format") String apiFormat);


    @POST("PaywelltransactionHaltrip/cancelTicket")
    @FormUrlEncoded
    Call<JsonObject> cancelTicket(@Field("username") String username,
                                  @Field("password") String password,
                                  @Field("BookingID") String bookingId,
                                  @Field("reason") String cancelReason,
                                  @Field("format") String apiFormat);


    @POST("/PaywelltransactionHaltrip/reScheduleTicket")
    @FormUrlEncoded
    Call<JsonObject> reScheduleTicket(@Field("username") String username,
                                      @Field("password") String password,
                                      @Field("BookingID") String bookingId,
                                      @Field("reason") String cancelReason,
                                      @Field("SearchId") String searchId,
                                      @Field("ResultID") String resultID,
                                      @Field("format") String apiFormat);

    @POST("/PaywelltransactionHaltrip/reIssueTicket")
    @Multipart
    Call<JsonObject> reIssueTicket(@Part("username") String username,
                                   @Part("password") String password,
                                   @Part("BookingID") String bookingId,
                                   @Part("reason") String cancelReason,
                                   @Part("passengers") String passengers);


    @POST("PaywelltransactionHaltrip/getCancelMap")
    @FormUrlEncoded
    Call<ResCancellationMapping> getCancelMap(@Field("username") String username,
                                              @Field("booking_id") String bookingId);

    @Multipart
    @POST("PaywelltransactionHaltrip/uploadBookingFiles")
    Call<JsonObject> uploadBookingFiles(@Part("username") String username,
                                        @Part("booking_id") String password,
                                        @Part("params") String fileUploadReqSearchPara);


    @POST("PaywelltransactionHaltrip/send_invoice_to_user")
    @Multipart
    Call<ResInvoideEmailAPI> callSendInvoiceAPI(@Part("username") String username,
                                                @Part("booking_id") String bookingId,
                                                @Part("email_address") String email_address);


    @POST("PaywelltransactionHaltrip/getCommissionMapping")
    @Multipart
    Call<ResCommistionMaping> callGetCommissionMappingAPI(@Part("username") String username);


    @POST("PaywelltransactionHaltrip/airTicketIssue")
    @Multipart
    Call<ResIssueTicket> callIssueTicketAPI(@Part("username") String username, @Part("password") String password, @Part("BookingID") String BookingID, @Part("IsAcceptedPriceChangeandIssueTicket") boolean ssAcceptedPriceChangeandIssueTicket);


    @POST("PaywelltransactionHaltrip/reScheduleNotificationAccept")
    @Multipart
    Call<ResposeReScheduleNotificationAccept> reScheduleNotificationAccept(@Part("username") String username, @Part("id") int id, @Part("accept_status") int accept_status);


    @POST()
    @Multipart
    Call<ResEkShopToken> getEkshopToken(@Url String url, @Part("uid") String rid, @Part("utype") String utype);

    @POST()
    @Multipart
    Call<ResEKReport> getReport(@Url String url, @Part("uid") String rid, @Part("start_date") String start_date, @Part("end_date") String end_date, @Part("order_code") String order_code);

    @POST("paywellapi/index.php/PaywellParibahanService/getBusListData")
    @FormUrlEncoded
    Call<ResGetBusListData> getBusListData(@Field("username") String username, @Field("skey") String skey);


    @POST("paywellapi/index.php/PaywellParibahanService/getBusSchedule?")
    @FormUrlEncoded
    Call<ResponseBody> getBusSchedule(@Field("username") String username, @Field("transport_id") String transport_id, @Field("skey") String skey, @Field("accessKey") String accessKey);


    @FormUrlEncoded
    @POST("paywellapi/index.php/PaywellParibahanService/seatCheck")
    Call<ResponseBody> seatCheck(@Field("username") String username,
                                 @Field("skey") String skey,
                                 @Field("accessKey") String accessKey,
                                 @Field("transport_id") String transport_id,
                                 @Field("route") String route,
                                 @Field("bus_id") String bus_id,
                                 @Field("departure_id") String departure_id,
                                 @Field("departure_date") String departure_date,
                                 @Field("seat_ids") String seat_ids);

    @FormUrlEncoded
    @POST("paywellapi/index.php/PaywellParibahanService/getTransactionData")
    Call<TransactionLogDetailsModel> getBusTransactionLogFromServer(@Field("username") String username, @Field("skey") String skey, @Field("limit") String limit);


    @FormUrlEncoded
    @POST("paywellapi/index.php/PaywellParibahanService/seatCheckAndBlock")
    Call<ResSeatCheckBookAPI> seatCheckAndBlock(@Field("userName") String username,
                                                @Field("skey") String skey,
                                                @Field("accessKey") String accessKey,
                                                @Field("transport_id") String transport_id,
                                                @Field("transport_lbls") String transport_lbls,
                                                @Field("route") String route,
                                                @Field("bus_id") String bus_id,
                                                @Field("bus_lbls") String bus_lbls,
                                                @Field("coach_no") String coach_no,
                                                @Field("departure_id") String departure_id,
                                                @Field("departure_date") String departure_date,
                                                @Field("departure_time") String departure_time,
                                                @Field("boarding_point_id") String boarding_point_id,
                                                @Field("boarding_point_name") String boarding_point_name,
                                                @Field("seat_ids") String seat_ids,
                                                @Field("seat_lbls") String seat_lbls,
                                                @Field("bus_is_ac") String bus_is_ac,
                                                @Field("extra_charge") Double extra_charge,
                                                @Field("ticket_price") Double ticket_price,
                                                @Field("total_amount") String total_amount);

    @FormUrlEncoded
    @POST("paywellapi/index.php/PaywellParibahanService/confirmPayment")
    Call<ResPaymentBookingAPI> confirmPayment(@Field("username") String username,
                                              @Field("skey") String skey,
                                              @Field("accessKey") String accessKey,
                                              @Field("transactionId") String transactionId,
                                              @Field("customerName") String customerName,
                                              @Field("customerPhone") String customerPhone,
                                              @Field("customerAddress") String customerAddress,
                                              @Field("customerEmail") String customerEmail,
                                              @Field("customerAge") String customerAge,
                                              @Field("customerGender") String customerGender,
                                              @Field("password") String password);

}


