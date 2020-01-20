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
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketCencel.model.ResSingleBooking;
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
import com.cloudwell.paywell.services.activity.refill.nagad.model.ResTranstionINquiry;
import com.cloudwell.paywell.services.activity.refill.nagad.model.refill_log.RefillLog;
import com.cloudwell.paywell.services.activity.reg.model.AuthRequestModel;
import com.cloudwell.paywell.services.activity.reg.model.RegistrationModel;
import com.cloudwell.paywell.services.activity.topup.model.RequestTopup;
import com.cloudwell.paywell.services.activity.topup.model.TopupReposeData;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model.PalliBidyutBillPayRequest;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model.PalliBidyutBillPayResponse;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.model.RequestBillStatusData;
import com.cloudwell.paywell.services.app.model.APIResBalanceCheck;
import com.cloudwell.paywell.services.app.model.APIResposeGenerateToken;
import com.cloudwell.paywell.services.service.notificaiton.model.APIResNoCheckNotification;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
                                                 @Field("Amount") String amount,
                                                 @Field("ref_id") String refId);

    @POST("PaywelltransactionPollyBiddyut/pollyBiddyutBillStatusQueryAPI")
    @FormUrlEncoded
    Call<RequestBillStatusData> callBillStatusRequestAPI(@Field("username") String username,
                                                         @Field("pass") String password,
                                                         @Field("account_no") String accountNo,
                                                         @Field("month") String month,
                                                         @Field("year") String year,
                                                         @Field("ref_id") String refId,
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

    @POST
    @FormUrlEncoded
    Call<String> deleteNotification(@Url String url,
                                    @Field("username") String username,
                                    @Field("message_id") String messageId);


    @Multipart
    @POST("PaywelltransactionHaltrip/airSearch")
    Call<ReposeAirSearch> callAirSearch(@Part("username") String username,
                                        @Part("search_params") RequestAirSearch search_params,
                                        @Part("ref_id") String refId);


    @Multipart
    @POST("PaywelltransactionHaltrip/airPriceSearch")
    Call<ResposeAirPriceSearch> callairPriceSearch(@Part("username") String username,
                                                   @Part("search_params") RequestAirPriceSearch search_params,
                                                   @Part("ref_id") String refId);


    @Multipart
    @POST("PaywelltransactionHaltrip/airRulesSearch")
    Call<ResposeAirRules> airRulesSearch(@Part("username") String username,
                                         @Part("search_params") RequestAirPriceSearch search_params);


    @GET("PaywelltransactionHaltrip/getAirports?")
    Call<ResGetAirports> getAirports(@Query("username") String username, @Query("format") String format, @Query("iso") String iso,@Query("ref_id") String refId );

    @FormUrlEncoded
    @POST("PaywelltransactionHaltrip/getBookingList")
    Call<BookingList> callAirBookingListSearch(@Field("username") String username,
                                               @Field("limit") int limit,
                                               @Field("ref_id") String refId);

    @Multipart
    @POST("PaywelltransactionHaltrip/airPreBooking")
    Call<ResAirPreBooking> airPreBooking(@Part("username") String username,
                                         @Part("format") String format,
                                         @Part("search_params") RequestAirPrebookingSearchParamsForServer search_params,
                                         @Part("ref_id") String refId);


    @Multipart
    @POST("PaywelltransactionHaltrip/airBooking")
    Call<ResBookingAPI> airBooking(@Part("username") String username,
                                   @Part("password") String password,
                                   @Part("format") String format,
                                   @Part("search_params") RequestAirPrebookingSearchParamsForServer search_params,
                                   @Part("ref_id") String refId);

    @POST("PaywelltransactionHaltrip/cancelBooking")
    @FormUrlEncoded
    Call<JsonObject> cancelBooking(@Field("username") String username,
                                   @Field("password") String password,
                                   @Field("BookingID") String bookingId,
                                   @Field("reason") String cancelReason,
                                   @Field("format") String apiFormat,
                                   @Field("ref_id") String refId);


    @POST("PaywelltransactionHaltrip/cancelTicket")
    @FormUrlEncoded
    Call<JsonObject> cancelTicket(@Field("username") String username,
                                  @Field("password") String password,
                                  @Field("BookingID") String bookingId,
                                  @Field("reason") String cancelReason,
                                  @Field("cancel_type") String cancel_type,
                                  @Field("format") String apiFormat,
                                  @Field("ref_id") String refId);

    @POST("PaywelltransactionHaltrip/reIssueTicket")
    @FormUrlEncoded
    Call<JsonObject> reIssueTicket(@Field("username") String username,
                                   @Field("password") String password,
                                   @Field("BookingID") String bookingId,
                                   @Field("reason") String cancelReason,
                                   @Field("ref_id") String refId);


    @POST("/PaywelltransactionHaltrip/reScheduleTicket")
    @FormUrlEncoded
    Call<JsonObject> reScheduleTicket(@Field("username") String username,
                                      @Field("password") String password,
                                      @Field("BookingID") String bookingId,
                                      @Field("reason") String cancelReason,
                                      @Field("SearchId") String searchId,
                                      @Field("ResultID") String resultID,
                                      @Field("format") String apiFormat);

    @POST("/PaywelltransactionHaltrip/infoUpdateTicket")
    @Multipart
    Call<JsonObject> infoUpdateTicket(@Part("username") String username,
                                   @Part("password") String password,
                                   @Part("BookingID") String bookingId,
                                   @Part("reason") String cancelReason,
                                   @Part("passengers") String passengers,
                                   @Part("ref_id") String refId);


    @POST("PaywelltransactionHaltrip/getCancelMap")
    @FormUrlEncoded
    Call<ResCancellationMapping> getCancelMap(@Field("username") String username,
                                              @Field("booking_id") String bookingId,
                                              @Field("ref_id") String refId);

    @Multipart
    @POST("PaywelltransactionHaltrip/uploadBookingFiles")
    Call<JsonObject> uploadBookingFiles(@Part("username") String username,
                                        @Part("booking_id") String password,
                                        @Part("params") String fileUploadReqSearchPara);


    @POST("PaywelltransactionHaltrip/send_invoice_to_user")
    @Multipart
    Call<ResInvoideEmailAPI> callSendInvoiceAPI(@Part("username") String username,
                                                @Part("booking_id") String bookingId,
                                                @Part("price_invoice_status") int priceInvoiceStatus,
                                                @Part("email_address") String email_address);


    @POST("PaywelltransactionHaltrip/getCommissionMapping")
    @Multipart
    Call<ResCommistionMaping> callGetCommissionMappingAPI(@Part("username") String username, @Part("ref_id") String refId);


    @POST("PaywelltransactionHaltrip/getSingleBooking")
    @Multipart
    Call<ResSingleBooking> getSingleBooking(@Part("username") String username, @Part("booking_id") String bookingId, @Part("ref_id") String refId);

    @POST("PaywelltransactionHaltrip/airTicketIssue")
    @Multipart
    Call<ResIssueTicket> callIssueTicketAPI(@Part("username") String username, @Part("password") String password, @Part("BookingID") String BookingID, @Part("IsAcceptedPriceChangeandIssueTicket") boolean ssAcceptedPriceChangeandIssueTicket, @Part("ref_id") String refId);


    @POST("PaywelltransactionHaltrip/reIssueNotificationAccept")
    @Multipart
    Call<ResposeReScheduleNotificationAccept> reIssueNotificationAccept(@Part("username") String username, @Part("id") int id, @Part("accept_status") int accept_status);


    @POST()
    @Multipart
    Call<ResEkShopToken> getEkshopToken(@Url String url, @Part("uid") String rid, @Part("utype") String utype, @Part("ref_id") String refId);

    @POST()
    @Multipart
    Call<ResEKReport> getReport(@Url String url, @Part("uid") String rid, @Part("start_date") String start_date, @Part("end_date") String end_date, @Part("order_code") String order_code, @Part("ref_id") String refId);

    @POST("PaywellParibahanService/getBusListData")
    @FormUrlEncoded
    Call<ResGetBusListData> getBusListData(@Field("username") String username, @Field("skey") String skey, @Field("ref_id") String refId);


    @POST("PaywellParibahanService/getBusSchedule?")
    @FormUrlEncoded
    Call<ResponseBody> getBusSchedule(@Field("username") String username, @Field("transport_id") String transport_id, @Field("skey") String skey, @Field("accessKey") String accessKey, @Field("ref_id") String refId);


    @FormUrlEncoded
    @POST("PaywellParibahanService/seatCheck")
    Call<ResponseBody> seatCheck(@Field("username") String username,
                                 @Field("skey") String skey,
                                 @Field("accessKey") String accessKey,
                                 @Field("transport_id") String transport_id,
                                 @Field("route") String route,
                                 @Field("bus_id") String bus_id,
                                 @Field("departure_id") String departure_id,
                                 @Field("departure_date") String departure_date,
                                 @Field("seat_ids") String seat_ids,
                                 @Field("ref_id") String refId);

    @FormUrlEncoded
    @POST("PaywellParibahanService/getTransactionData")
    Call<TransactionLogDetailsModel> getBusTransactionLogFromServer(@Field("username") String username, @Field("skey") String skey, @Field("limit") String limit, @Field("ref_id") String refId);

    @POST
    @FormUrlEncoded
    Call<String> getToken(@Url String ur,
                          @Field("rid") String rid);


    @FormUrlEncoded
    @POST("PaywellParibahanService/seatCheckAndBlock")
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
                                                @Field("total_amount") String total_amount,
                                                @Field("ref_id") String refId);

    @FormUrlEncoded
    @POST("PaywellParibahanService/confirmPayment")
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
                                              @Field("password") String password,
                                              @Field("ref_id") String refId);

    @POST("/PaywelltransactionPollyBiddyut/pollyBiddyutBillPayAPIAsync")
    Call<PalliBidyutBillPayResponse> postPalliBidyutBills(@Body PalliBidyutBillPayRequest body);


    @FormUrlEncoded
    @POST
    Call<ResTranstionINquiry> transactionInquiry(@Url String url,
                                                 @Field("sec_token") String username,
                                                 @Field("imei") String skey,
                                                 @Field("pin") String accessKey,
                                                 @Field("trxOrPhoneNo") String transactionId,
                                                 @Field("format") String customerName,
                                                 @Field("gateway_id") String customerPhone,
                                                 @Field("amount") String customerAddress,
                                                 @Field("ref_id") String refId);

    @FormUrlEncoded
    @POST
    Call<RefillLog> refillLogInquiry(@Url String url,
                                     @Field("sec_token") String username,
                                     @Field("imei") String skey,
                                     @Field("format") String customerName,
                                     @Field("gateway_id") String customerPhone,
                                     @Field("limit") String limit,
                                     @Field("ref_id") String refId);


    @POST("PaywellUserRegistration/userInformationForRegistration")
    Call<ResponseBody> userInformationForRegistration(@Body RegistrationModel regModel);


    @POST("PaywelltransactionRetailer/userServiceProfiling")
    Call<ResponseBody> userServiceProfiling(@Body AuthRequestModel regModel);



    @POST("PaywellUserRegistration/unverifiedDataUpdate")
    Call<ResponseBody> unverifiedDataCollectAndUpdate( @Body JsonObject body);




}


