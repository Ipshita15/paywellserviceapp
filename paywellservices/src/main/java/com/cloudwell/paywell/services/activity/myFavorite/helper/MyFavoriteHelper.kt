package com.cloudwell.paywell.services.activity.myFavorite.helper

import android.content.Context
import android.os.AsyncTask
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.myFavorite.MenuStatus
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu
import com.cloudwell.paywell.services.database.DatabaseClient

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
class MyFavoriteHelper {

    companion object {
        fun insertData(context: Context) {
            val listOfData = mutableListOf<FavoriteMenu>()

            listOfData.add(FavoriteMenu(R.string.mobileOperator, R.string.home_topup, R.drawable.all_operator, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.brilliant, R.string.home_topup, R.drawable.brilli_ant, MenuStatus.UnFavorite.text, 0))

            // utility
            listOfData.add(FavoriteMenu(R.string.home_utility_desco, R.string.home_utility, R.drawable.ic_desco, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_desco_pay, R.string.home_utility, R.drawable.ic_bill_pay, MenuStatus.UnFavorite.text, 0))

            listOfData.add(FavoriteMenu(R.string.home_utility_dpdc, R.string.home_utility, R.drawable.ic_dpdc, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_dpdc_bill_pay, R.string.home_utility, R.drawable.ic_dpdc, MenuStatus.UnFavorite.text, 0))

            listOfData.add(FavoriteMenu(R.string.home_utility_pollibiddut, R.string.home_utility, R.drawable.ic_polli_biddut, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_pollibiddut_registion, R.string.home_utility, R.drawable.ic_registration, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_pollibiddut_bill_pay_favorite, R.string.home_utility, R.drawable.ic_bill_pay, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_pollibiddut_reg_inquiry, R.string.home_utility, R.drawable.ic_enquiry, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_pollibiddut_bill_inquiry, R.string.home_utility, R.drawable.ic_enquiry, MenuStatus.UnFavorite.text, 0))

            listOfData.add(FavoriteMenu(R.string.home_utility_wasa, R.string.home_utility, R.drawable.ic_wasa, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_wasa_pay, R.string.home_utility, R.drawable.ic_bill_pay, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_wasa_inquiry, R.string.home_utility, R.drawable.ic_enquiry, MenuStatus.UnFavorite.text, 0))


            listOfData.add(FavoriteMenu(R.string.home_utility_west_zone, R.string.home_utility, R.drawable.ic_west_zone, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_west_zone_pay, R.string.home_utility, R.drawable.ic_bill_pay, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_west_zone_pay_inquiry, R.string.home_utility, R.drawable.ic_west_zone, MenuStatus.UnFavorite.text, 0))


            listOfData.add(FavoriteMenu(R.string.home_utility_qubee, R.string.home_utility, R.drawable.ic_qubee, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_qubee_recharge, R.string.home_utility, R.drawable.ic_qubee, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_qubee_recharge_inquiry, R.string.home_utility, R.drawable.ic_recharge_information, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_qubee_wrong_acc_title_favoirte, R.string.home_utility, R.drawable.ic_wrong_acc, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_qubee_wrong_amount_title, R.string.home_utility, R.drawable.ic_wrong_acc, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_qubee_payment_not_title_favoirte, R.string.home_utility, R.drawable.ic_payment_not_recived, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_beximco, R.string.home_utility, R.drawable.ic_realvu, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_ivac, R.string.home_utility, R.drawable.ic_ivac, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_ivac_free_pay_favorite, R.string.home_utility, R.drawable.ic_bill_pay, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_ivac_inquiry, R.string.home_utility, R.drawable.ic_enquiry, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_banglalion, R.string.home_utility, R.drawable.ic_banglalion, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_banglalion_recharge, R.string.home_utility, R.drawable.ic_banglalion_recharge, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_utility_banglalion_recharge_inquiry, R.string.home_utility, R.drawable.ic_recharge_information, MenuStatus.UnFavorite.text, 0))


            // end utility
//            listOfData.add(FavoriteMenu(R.string.home_eticket_bus, R.string.home_eticket, R.drawable.ic_bus_ticket, MenuStatus.UnFavorite.text, 0))
//            listOfData.add(FavoriteMenu(R.string.home_eticket_train, R.string.home_eticket, R.drawable.ic_train_ticket, MenuStatus.UnFavorite.text, 0))

            listOfData.add(FavoriteMenu(R.string.home_mfs_mycash, R.string.home_mfs_fav, R.drawable.ic_train_ticket, MenuStatus.UnFavorite.text, 0))

            listOfData.add(FavoriteMenu(R.string.home_product_ajker_deal, R.string.home_product_catalog, R.drawable.ic_ajker_deal, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_product_pw_wholesale, R.string.home_product_catalog, R.drawable.ic_wholesale, MenuStatus.UnFavorite.text, 0))

            listOfData.add(FavoriteMenu(R.string.home_statement_mini, R.string.home_statement, R.drawable.ic_statement, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_statement_balance, R.string.home_statement, R.drawable.ic_statement, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_statement_sales, R.string.home_statement, R.drawable.ic_statement, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_statement_transaction, R.string.home_statement, R.drawable.ic_statement, MenuStatus.UnFavorite.text, 0))

            listOfData.add(FavoriteMenu(R.string.home_refill_bank, R.string.home_refill_balance, R.drawable.ic_bank_deposit, MenuStatus.UnFavorite.text, 0))
            listOfData.add(FavoriteMenu(R.string.home_refill_card, R.string.home_refill_balance, R.drawable.ic_visa_master_card, MenuStatus.UnFavorite.text, 0))



            AsyncTask.execute {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .mFavoriteMenuDab()
                        .insert(listOfData)
            }
        }
    }


}
