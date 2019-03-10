package com.cloudwell.paywell.services.activity.eticket.airticket

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.multiCity.MultiCityFragment

class SearchFlightAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 // Fragment # 0 - This will show FirstFragment
            -> return OneWayFragment()
            1 // Fragment # 1 - This will show SecondFragment different title
            -> return RoundTripFragment()
            2 // Fragment # 2 - This will show ThirdFragment different title
            -> return MultiCityFragment()
            else -> return null
        }

    }

    override fun getCount(): Int {
        return 3;
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "One-way"
            1 -> "Round-trip"
            2 -> "Multi-City"
            else -> {
                return ""
            }
        }
    }
}