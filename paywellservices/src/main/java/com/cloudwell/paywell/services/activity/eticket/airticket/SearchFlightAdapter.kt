package com.cloudwell.paywell.services.activity.eticket.airticket

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.fragment.IndianWayFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.fragment.OneWayV2Fragment
import com.cloudwell.paywell.services.activity.eticket.airticket.multiCity.MultiCityFragment
import com.cloudwell.paywell.services.app.AppController

class SearchFlightAdapter(fm: FragmentManager?, item: Datum) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {

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

    }

    override fun getCount(): Int {
        return 4;
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> AppController.getContext().getString(R.string.one_way_menu_title)
            1 -> AppController.getContext().getString(R.string.round_trip_menu_title)
            2 -> AppController.getContext().getString(R.string.multi_city_menu_title)
            3 -> AppController.getContext().getString(R.string.indian_menu_title)
            else -> {
                return ""
            }
        }
    }
}