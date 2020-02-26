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
    public static final String URL_statementInquiry = HOST_URL_PHP_7 + "Android/AndroidWebViewController/StatementInquiry";
    public static final String URL_balanceStatement = HOST_URL_PHP_7 + "Android/AndroidWebViewController/balanceStatement";
    public static final String URL_salesStatementForhttps = HOST_URL_PHP_7 + "Android/AndroidWebViewController/salesStatement";
    public static final String URL_getAllTransactionStatementForHttps = HOST_URL_PHP_7 + "Android/AndroidWebViewController/getAllTransactionStatement";


    public static final String URL_ek_shope_token = "https://ekshop.paywellonline.com/get_token";
    public static final String URL_ek_redirect = "https://ekshop.paywellonline.com/redirect?";
    public static final String URL_ek_report = "https://ekshop.paywellonline.com/get_report";

    public static final String URL_chat = "https://agentapi.paywellonline.com/chatting-retailer/";
    public static final String URL_update_check = "https://app.paywellonline.com/apk/paywell_services_latest_version.apk";
    public static final String URL_check_version = "https://app.paywellonline.com/version_update_info/paywellservice_update.txt";
    public static final String URL_pb_bill_pay = "https://api.paywellonline.com/PaywelltransactionPollyBiddyut/pollyBiddyutBillPayAPI";
    public static final String URL_banglalion_bill_pay = "https://api.paywellonline.com/PayWellInternateBillPay/banglalionBillPay";
    public static final String URL_banglalion_bill_inquiry = "https://api.paywellonline.com/PayWellInternateBillPay/banglalionEnquiry";
    public static final String URL_bkash_balance_check = "https://bkapi.paywellonline.com/retailer/userBalanceCheck.php?";


}
