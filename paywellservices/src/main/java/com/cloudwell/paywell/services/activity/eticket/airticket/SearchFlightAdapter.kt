package com.cloudwell.paywell.services.activity.eticket.airticket

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class SearchFlightAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 ->
                OneWayFragment()
            1 -> RoundTripFragment()
            else ->
                RoundTripFragment()
        }
    }

    override fun getCount(): Int {
        return 3;
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "One-way"
            1 -> "Round-trip"
            else -> {
                return ""
            }
        }
    }
}