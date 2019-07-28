package com.cloudwell.paywell.services.activity.eticket.airticket

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.fragment.IndianWayFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.fragment.OneWayV2Fragment
import com.cloudwell.paywell.services.activity.eticket.airticket.fragment.RoundTripFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.multiCity.MultiCityFragment
import com.cloudwell.paywell.services.app.AppController

class SearchFlightAdapter(fm: FragmentManager?, var item: Datum = Datum()) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {

        if (item.mSearchLog.size == 0) {

            when (position) {
                0
                -> return OneWayV2Fragment()
                1
                -> return RoundTripFragment()
                2
                -> return MultiCityFragment()
                3
                -> return IndianWayFragment()
                else -> return null
            }

        } else {
            if (item.journeyType.equals("Oneway")) {
                return OneWayV2Fragment()

            } else if (item.journeyType.equals("Return")) {
                return RoundTripFragment()

            } else if (item.journeyType.equals("MultiStop")) {

                return MultiCityFragment()
            }

        }
        return null
    }

    override fun getCount(): Int {

        if (item.mSearchLog.size == 0) {
            return 4
        } else {
            return 1
        }


    }

    override fun getPageTitle(position: Int): CharSequence? {
        if (item.mSearchLog.size == 0) {
            when (position) {
                0 -> {
                    return AppController.getContext().getString(R.string.one_way_menu_title)
                }
                1 -> {
                    return AppController.getContext().getString(R.string.round_trip_menu_title)
                }
                2 -> {
                    return AppController.getContext().getString(R.string.multi_city_menu_title)
                }
                3 -> {
                    return AppController.getContext().getString(R.string.indian_menu_title)
                }
                else -> {
                    return ""
                }
            }
        } else {
            if (item.journeyType.equals("Oneway")) {
                return AppController.getContext().getString(R.string.one_way_menu_title)

            } else if (item.journeyType.equals("Return")) {
                return AppController.getContext().getString(R.string.round_trip_menu_title)

            } else if (item.journeyType.equals("MultiStop")) {

                return AppController.getContext().getString(R.string.multi_city_menu_title)
            }

        }

        return ""
    }
}