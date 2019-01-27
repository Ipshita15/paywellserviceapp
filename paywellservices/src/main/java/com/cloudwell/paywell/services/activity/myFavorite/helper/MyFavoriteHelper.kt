package com.cloudwell.paywell.services.activity.myFavorite.helper

import android.content.Context
import android.os.AsyncTask
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu
import com.cloudwell.paywell.services.constant.IconConstant
import com.cloudwell.paywell.services.database.DatabaseClient
import com.cloudwell.paywell.services.utils.StringConstant

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
class MyFavoriteHelper {

    companion object {
        fun insertData(context: Context) {
            val listOfData = mutableListOf<FavoriteMenu>()


            // 41
            listOfData.add(FavoriteMenu(StringConstant.KEY_mobileOperator, StringConstant.KEY_topup, IconConstant.KEY_all_operator, MenuStatus.UnFavorite.text, 0, 1))
            listOfData.add(FavoriteMenu(StringConstant.KEY_brilliant, StringConstant.KEY_topup, IconConstant.KEY_brilli_ant, MenuStatus.UnFavorite.text, 0, 2))

//            // utility
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_desco, StringConstant.KEY_home_utility, IconConstant.KEY_ic_desco, MenuStatus.UnFavorite.text, 0, 3))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_desco_pay, StringConstant.KEY_home_utility, IconConstant.KEY_ic_bill_pay, MenuStatus.UnFavorite.text, 0, 4))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_desco_pay_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry, MenuStatus.UnFavorite.text, 0, 5))
//
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_dpdc, StringConstant.KEY_home_utility, IconConstant.KEY_ic_dpdc, MenuStatus.UnFavorite.text, 0, 6))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_dpdc_bill_pay, StringConstant.KEY_home_utility, IconConstant.KEY_ic_dpdc, MenuStatus.UnFavorite.text, 0, 7))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_dpdc_bill_pay_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_dpdc, MenuStatus.UnFavorite.text, 0, 8))

            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_pollibiddut, StringConstant.KEY_home_utility, IconConstant.KEY_ic_polli_biddut, MenuStatus.UnFavorite.text, 0, 9))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_pollibiddut_registion, StringConstant.KEY_home_utility, IconConstant.KEY_ic_registration, MenuStatus.UnFavorite.text, 0, 10))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_pollibiddut_bill_pay_favorite, StringConstant.KEY_home_utility, IconConstant.KEY_ic_bill_pay, MenuStatus.UnFavorite.text, 0, 11))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_pollibiddut_reg_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry, MenuStatus.UnFavorite.text, 0, 12))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_pollibiddut_bill_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry, MenuStatus.UnFavorite.text, 0, 13))

            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_pb_request_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_to_know_bill_status, MenuStatus.UnFavorite.text, 0, 14))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_pb_bill_statu_inquery, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry, MenuStatus.UnFavorite.text, 0, 15))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_pb_bill_change_number, StringConstant.KEY_home_utility, IconConstant.KEY_contact_number_change, MenuStatus.UnFavorite.text, 0, 16))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_pb_phone_number_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry, MenuStatus.UnFavorite.text, 0, 17))


            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_wasa, StringConstant.KEY_home_utility, IconConstant.KEY_ic_wasa, MenuStatus.UnFavorite.text, 0, 18))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_wasa_pay, StringConstant.KEY_home_utility, IconConstant.KEY_ic_bill_pay, MenuStatus.UnFavorite.text, 0, 19))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_wasa_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry, MenuStatus.UnFavorite.text, 0, 20))

            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_west_zone, StringConstant.KEY_home_utility, IconConstant.KEY_ic_west_zone, MenuStatus.UnFavorite.text, 0, 21))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_west_zone_pay, StringConstant.KEY_home_utility, IconConstant.KEY_ic_bill_pay, MenuStatus.UnFavorite.text, 0, 22))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_west_zone_pay_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_west_zone, MenuStatus.UnFavorite.text, 0, 23))

            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_ivac, StringConstant.KEY_home_utility, IconConstant.KEY_ic_ivac, MenuStatus.UnFavorite.text, 0, 24))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_ivac_free_pay_favorite, StringConstant.KEY_home_utility, IconConstant.KEY_ic_bill_pay, MenuStatus.UnFavorite.text, 0, 25))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_ivac_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry, MenuStatus.UnFavorite.text, 0, 26))

            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_banglalion, StringConstant.KEY_home_utility, IconConstant.KEY_ic_banglalion, MenuStatus.UnFavorite.text, 0, 27))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_banglalion_recharge, StringConstant.KEY_home_utility, IconConstant.KEY_ic_banglalion_recharge, MenuStatus.UnFavorite.text, 0, 28))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_banglalion_recharge_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_recharge_information, MenuStatus.UnFavorite.text, 0, 29))

            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_karnaphuli, StringConstant.KEY_home_utility, IconConstant.KEY_ic_karnafuli_gas, MenuStatus.UnFavorite.text, 0, 30))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_karnaphuli_bill_pay, StringConstant.KEY_home_utility, IconConstant.KEY_ic_bill_pay, MenuStatus.UnFavorite.text, 0, 31))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_utility_karnaphuli_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry, MenuStatus.UnFavorite.text, 0, 32))
////            // end utility

            listOfData.add(FavoriteMenu(StringConstant.KEY_home_mfs_mycash, StringConstant.KEY_home_mfs_fav, IconConstant.KEY_ic_my_cash, MenuStatus.UnFavorite.text, 0, 33))

            listOfData.add(FavoriteMenu(StringConstant.KEY_home_product_ajker_deal, StringConstant.KEY_home_product_catalog, IconConstant.KEY_ic_ajker_deal, MenuStatus.UnFavorite.text, 0, 34))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_product_pw_wholesale, StringConstant.KEY_home_product_catalog, IconConstant.KEY_ic_wholesale, MenuStatus.UnFavorite.text, 0, 35))

            listOfData.add(FavoriteMenu(StringConstant.KEY_home_statement_mini, StringConstant.KEY_home_statement, IconConstant.KEY_ic_statement, MenuStatus.UnFavorite.text, 0, 36))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_statement_balance, StringConstant.KEY_home_statement, IconConstant.KEY_ic_statement, MenuStatus.UnFavorite.text, 0, 37))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_statement_sales, StringConstant.KEY_home_statement, IconConstant.KEY_ic_statement, MenuStatus.UnFavorite.text, 0, 38))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_statement_transaction, StringConstant.KEY_home_statement, IconConstant.KEY_ic_statement, MenuStatus.UnFavorite.text, 0, 39))

            listOfData.add(FavoriteMenu(StringConstant.KEY_home_refill_bank, StringConstant.KEY_home_refill_balance, IconConstant.KEY_ic_bank_deposit, MenuStatus.UnFavorite.text, 0, 40))
            listOfData.add(FavoriteMenu(StringConstant.KEY_home_refill_card, StringConstant.KEY_home_refill_balance, IconConstant.KEY_ic_visa_master_card, MenuStatus.UnFavorite.text, 0, 41))

            AsyncTask.execute {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .mFavoriteMenuDab()
                        .insert(listOfData)
            }
        }
    }


}
