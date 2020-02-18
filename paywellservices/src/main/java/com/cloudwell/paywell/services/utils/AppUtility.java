package com.cloudwell.paywell.services.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.airticket.menu.AirTicketMenuActivity;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.menu.BusTicketMenuActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.MYCashMainActivity;
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu;
import com.cloudwell.paywell.services.activity.product.AjkerDealActivity;
import com.cloudwell.paywell.services.activity.product.WholesaleActivity;
import com.cloudwell.paywell.services.activity.refill.banktransfer.BankTransferMainActivity;
import com.cloudwell.paywell.services.activity.refill.card.CardTransferMainActivity;
import com.cloudwell.paywell.services.activity.statements.ViewStatementActivity;
import com.cloudwell.paywell.services.activity.topup.TopupMainActivity;
import com.cloudwell.paywell.services.activity.topup.brilliant.BrilliantTopupActivity;
import com.cloudwell.paywell.services.activity.utility.banglalion.BanglalionMainActivity;
import com.cloudwell.paywell.services.activity.utility.banglalion.BanglalionRechargeActivity;
import com.cloudwell.paywell.services.activity.utility.banglalion.BanglalionRechargeInquiryActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.postpaid.DESCOPostpaidBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.postpaid.DESCOPostpaidMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.dpdc.DPDCMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.dpdc.DPDCPostpaidBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.WASABillPayActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.WASAMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.westzone.WZPDCLBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.westzone.WZPDCLMainActivity;
import com.cloudwell.paywell.services.activity.utility.ivac.IvacFeeInquiryMainActivity;
import com.cloudwell.paywell.services.activity.utility.ivac.IvacFeePayActivity;
import com.cloudwell.paywell.services.activity.utility.ivac.IvacMainActivity;
import com.cloudwell.paywell.services.activity.utility.karnaphuli.KarnaphuliBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.karnaphuli.KarnaphuliMainActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.PBMainActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.PBBillPayNewActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.billStatus.PBBillStatusActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.MobileNumberChangeActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.registion.PBRegistrationActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.constant.AllConstant;

import java.io.File;
import java.util.Locale;

import androidx.core.content.ContextCompat;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/26/19.
 */
public class AppUtility {

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    public static String getProgressDisplayLine(long currentBytes, long totalBytes) {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes);
    }

    private static String getBytesToMBString(long bytes) {
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }

    public static Intent onAppShortcutItemClick(FavoriteMenu favoriteMenu,Context context) {

        AppHandler mAppHandler=AppHandler.getmInstance(context);



        Intent intent;

        int resId = ResorceHelper.getResId(favoriteMenu.getName(), R.string.class);

        switch (resId) {

            case R.string.mobileOperator:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_MENU);
                return new Intent(context, TopupMainActivity.class);

            case R.string.brilliant:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_TOPUP_BRILLIANT_MENU);

                return new Intent(context, BrilliantTopupActivity.class);
            case R.string.home_utility_desco:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_DESCO_MENU);

                return new Intent(context, DESCOPostpaidMainActivity.class);

            case R.string.home_utility_desco_pay:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_DESCO_BILL_PAY);

                return new Intent(context, DESCOPostpaidBillPayActivity.class);

            case R.string.home_utility_desco_pay_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_DESCO_BILL_PAY_INQUIRY);

                intent = new Intent(context, DESCOPostpaidMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_DESCO_INQUERY, true);
                return intent;

            case R.string.home_utility_dpdc:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_DPDC_MENU);

                return new Intent(context, DPDCMainActivity.class);

            case R.string.home_utility_dpdc_bill_pay:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_DPDC_BILL_PAY);

                return new Intent(context, DPDCPostpaidBillPayActivity.class);

            case R.string.home_utility_dpdc_bill_pay_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_DPDC_BILL_PAY_INQUIRY);

                intent = new Intent(context, DPDCMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_DPDC_INQUERY, true);
                return intent;


            case R.string.home_utility_pollibiddut:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_MENU);

                intent = new Intent(context, PBMainActivity.class);
                return intent;

            case R.string.home_utility_pollibiddut_registion:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_REGISTION);

                intent = new Intent(context, PBRegistrationActivity.class);
                return intent;

            case R.string.home_utility_pollibiddut_bill_pay_favorite:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_BILL_PAY);

                intent = new Intent(context, PBBillPayNewActivity.class);
                return intent;

            case R.string.home_utility_pollibiddut_reg_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_REGISTION_INQUIRY);

                intent = new Intent(context, PBMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_RG_INQUERY, true);
                return intent;

            case R.string.home_utility_pollibiddut_bill_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_BILL_INQUIRY);


                intent = new Intent(context, PBMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_BILL_INQUERY, true);
                return intent;

            case R.string.home_utility_pb_request_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_BILL_STATUS);


                intent = new Intent(context, PBBillStatusActivity.class);
                return intent;

            case R.string.home_utility_pb_bill_statu_inquery:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_BILL_STATUS_INQUIRY);

                intent = new Intent(context, PBMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_REQUEST_BILL_INQUIRY, true);
                return intent;
            case R.string.home_utility_pb_bill_change_number:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_MOBILE_NUMBER_CHANGE);

                intent = new Intent(context, MobileNumberChangeActivity.class);
                return intent;

            case R.string.home_utility_pb_phone_number_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_MOBILE_NUMBER_CHANGE_INQUIRY);

                intent = new Intent(context, PBMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_MOBILE_NUMBER_CHANGE_INQUIRY, true);
                return intent;

            case R.string.home_utility_wasa:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_WASA_MENU);

                intent = new Intent(context, WASAMainActivity.class);
                return intent;

            case R.string.home_utility_wasa_pay:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_WASA_BILL_PAY);

                intent = new Intent(context, WASABillPayActivity.class);
                return intent;

            case R.string.home_utility_wasa_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_WASA_BILL_PAY_INQUIRY);

                intent = new Intent(context, WASAMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_WASA_BILL_INQUIRY, true);
                return intent;

            case R.string.home_utility_west_zone:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_WZPDCL_MENU);

                intent = new Intent(context, WZPDCLMainActivity.class);
                return intent;


            case R.string.home_utility_west_zone_pay:

                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_WZPDCL_BILL_PAY);

                intent = new Intent(context, WZPDCLBillPayActivity.class);
                return intent;

            case R.string.home_utility_west_zone_pay_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_WZPDCL_BILL_INQUIRY);

                intent = new Intent(context, WZPDCLMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_WEST_ZONE_BILL_INQUIRY, true);
                return intent;


            case R.string.home_utility_ivac:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_IVAC_MENU);

                intent = new Intent(context, IvacMainActivity.class);
                return intent;

            case R.string.home_utility_ivac_free_pay_favorite:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_IVAC_BILL_PAY);

                intent = new Intent(context, IvacFeePayActivity.class);
                return intent;

            case R.string.home_utility_ivac_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_IVAC_BILL_INQUIRY);

                intent = new Intent(context, IvacFeeInquiryMainActivity.class);
                return intent;


            case R.string.home_utility_banglalion:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_BANGLALION_MENU);

                intent = new Intent(context, BanglalionMainActivity.class);
                return intent;

            case R.string.home_utility_banglalion_recharge:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_BANGLALION_RECHARGE);

                intent = new Intent(context, BanglalionRechargeActivity.class);
                return intent;

            case R.string.home_utility_banglalion_recharge_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_BANGLALION_RECHARGE_INQUIRY);


                intent = new Intent(context, BanglalionRechargeInquiryActivity.class);
                return intent;

            case R.string.home_utility_karnaphuli:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_KARNAPHULI_MENU);

                intent = new Intent(context, KarnaphuliMainActivity.class);
                return intent;

            case R.string.home_utility_karnaphuli_bill_pay:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_KARNAPHULI_MENU_BILL_PAY);

                intent = new Intent(context, KarnaphuliBillPayActivity.class);
                return intent;

            case R.string.home_utility_karnaphuli_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_KARNAPHULI_MENU_BILL_PAY_INQUIRY);

                intent = new Intent(context, KarnaphuliMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_KARACHI_INQUIRY, true);
                return intent;

            case R.string.home_eticket_bus:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_ETICKET_BUS);


                intent = new Intent(context, BusTicketMenuActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_BUS, true);
                return intent;

            case R.string.home_eticket_air:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_ETICKET_AIR);

                intent = new Intent(context, AirTicketMenuActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AIR, true);
                return intent;


            case R.string.home_mfs_mycash:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_MFS_MYCASH_MENU);

                intent = new Intent(context, MYCashMainActivity.class);
                return intent;


            case R.string.home_statement_mini:


                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_STATEMENT_MINI_MENU);


                intent = new Intent(context, ViewStatementActivity.class);
                intent.putExtra(ViewStatementActivity.DESTINATION_TITLE,"mini");

                return intent;
            case R.string.home_statement_balance:

                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_STATEMENT_BALANCE_MENU);


                intent = new Intent(context, ViewStatementActivity.class);

                intent.putExtra(ViewStatementActivity.DESTINATION_TITLE,"balance");

                return intent;


            case R.string.home_statement_sales:

                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_STATEMENT_SALES_MENU);


                intent = new Intent(context, ViewStatementActivity.class);
                intent.putExtra(ViewStatementActivity.DESTINATION_TITLE,"sales");

                return intent;

            case R.string.home_statement_transaction:

                intent = new Intent(context, ViewStatementActivity.class);
                intent.putExtra(ViewStatementActivity.DESTINATION_TITLE,"trx");

                return intent;


            case R.string.home_refill_bank:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_BALANCE_REFILL_BANK);


                intent = new Intent(context, BankTransferMainActivity.class);
                return intent;

            case R.string.home_refill_card:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_BALANCE_REFILL_CARD);

                intent = new Intent(context, CardTransferMainActivity.class);
                return intent;


            case R.string.home_product_ajker_deal:

                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_PRODUCT_AJKER_DEAL_MENU);
                intent = new Intent(context, AjkerDealActivity.class);
                return intent;

            case R.string.home_product_pw_wholesale:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_PRODUCT_WHOLESALE_MENU);
                intent = new Intent(context, WholesaleActivity.class);
                return intent;

            default:
                return null;


        }


    }

}
