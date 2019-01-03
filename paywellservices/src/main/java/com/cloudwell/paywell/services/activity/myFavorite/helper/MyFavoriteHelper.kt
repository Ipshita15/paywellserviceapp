package com.cloudwell.paywell.services.activity.myFavorite.helper

import android.content.Context
import android.os.AsyncTask
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.database.DatabaseClient
import com.cloudwell.paywell.services.activity.myFavorite.MenuStatus
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
class MyFavoriteHelper {

    companion object {
        fun insertData(context: Context) {
            val listOfData = mutableListOf<FavoriteMenu>()

            val favoriteMenu1 = FavoriteMenu(R.string.mobileOperator, R.string.home_topup, R.drawable.all_operator, MenuStatus.UnFavorite.text)
            listOfData.add(favoriteMenu1)

            val favoriteMenu2 = FavoriteMenu(R.string.brilliant, R.string.home_topup, R.drawable.all_operator, MenuStatus.UnFavorite.text)
            listOfData.add(favoriteMenu2)


            val favoriteMenu3 = FavoriteMenu(R.string.home_utility_desco, R.string.home_utility, R.drawable.all_operator, MenuStatus.UnFavorite.text)
            listOfData.add(favoriteMenu3)

            val favoriteMenu4 = FavoriteMenu(R.string.home_utility_desco_pay, R.string.home_utility, R.drawable.all_operator, MenuStatus.UnFavorite.text)
            listOfData.add(favoriteMenu4)

            val favoriteMenu5 = FavoriteMenu(R.string.home_utility_dpdc, R.string.home_utility, R.drawable.all_operator, MenuStatus.UnFavorite.text)
            listOfData.add(favoriteMenu5)

            val favoriteMenu6 = FavoriteMenu(R.string.home_utility_dpdc_pay, R.string.home_utility, R.drawable.all_operator, MenuStatus.UnFavorite.text)
            listOfData.add(favoriteMenu6)

            val favoriteMenu7 = FavoriteMenu(R.string.home_utility_dpdc_pay, R.string.home_utility, R.drawable.all_operator, MenuStatus.UnFavorite.text)
            listOfData.add(favoriteMenu7)


            AsyncTask.execute {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .mFavoriteMenuDab()
                        .insert(listOfData);
            }
        }
    }


}
