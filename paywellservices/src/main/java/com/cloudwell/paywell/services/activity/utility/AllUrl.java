package com.cloudwell.paywell.services.activity.utility;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 10/30/18.
 */
public class AllUrl {

    // host url
    private static final String HOST_URL = "https://api.paywellonline.com/";
    public static final String HOST_URL_AUTHENTICATION = "https://agentapi.paywellonline.com/PaywellAuthentication/getToken";

    public static final String HOST_URL_PHP_7 = "https://agentapi.paywellonline.com/";

    public static final String HOST_URL_bkapi = "https://bkapi.paywellonline.com/retailer/transactionInquiry";
    public static final String sec_token = "a67c46503d7a8b617782fc50176d7f6d";

    public static final String HOST_URL_lastSuccessfulTrx = "https://bkapi.paywellonline.com/retailer/lastSuccessfulTrx.php";




    // Base url
    public static final String BASE_URL = HOST_URL + "";
    public static final String BASE_URL_PHP_7 = HOST_URL_PHP_7 + "";



    // webview url
    public static final String URL_statementInquiry =  HOST_URL_PHP_7+"Android/AndroidWebViewController/StatementInquiry";
    public static final String URL_balanceStatement =  HOST_URL_PHP_7+"Android/AndroidWebViewController/balanceStatement";
    public static final String URL_salesStatementForhttps =  HOST_URL_PHP_7+"Android/AndroidWebViewController/salesStatement";
    public static final String URL_getAllTransactionStatementForHttps =  HOST_URL_PHP_7+"Android/AndroidWebViewController/getAllTransactionStatement";

    public static final String privacy = "https://www.paywellonline.com/Privacy.php";
    public static final String len = "https://api.paywellonline.com/retailerPromotionImage/retailer_pic_";
    public static final String FACEBOOK_URL = "https://www.facebook.com/PayWellOnline/";

    public static final  String packageNameYoutube = "com.google.android.youtube";
    public static final  String linkRefillBalance = "https://www.youtube.com/watch?v=EEgUyMt0sv8";
    public static final  String linkDpdcBillPay = "https://www.youtube.com/watch?v=EovJfDwrKSc&t=4s";
    public static final  String linkPolliBillPay = "https://www.youtube.com/watch?v=SAuIFcUclvs&t=1s";
    public static final  String linkAllServices = "https://www.youtube.com/watch?v=RNEjADit-PQ";
    public static final  String linkBusTicket = "https://www.youtube.com/watch?v=nqdc_vKPnWw&t=14s";
    public static final  String linkAirTicket = "https://www.youtube.com/watch?v=evzzJ5Ld4qs&t=1s";

    public static final  String flightAdapter = "https://notify.paywellonline.com/airlines/images_airline/";


    public static final  String gettoken = "https://agentapi.paywellonline.com/Authantication/PaywellAuth/getToken?";
    public static final  String ProfilingReg = "https://agentapi.paywellonline.com/Registration/UserRegistration/userServiceProfilingReg";
    public static final  String resetPassword = "https://agentapi.paywellonline.com/Authantication/PaywellAuth/resetPassword";
    public static final  String refreshToken = "https://agentapi.paywellonline.com/Authantication/PaywellAuth/refreshToken";

    public static final  String ekshop_paywell_auth_check = "https://ekshop.gov.bd/ekshop_integration/api/paywell-auth-check";





}
