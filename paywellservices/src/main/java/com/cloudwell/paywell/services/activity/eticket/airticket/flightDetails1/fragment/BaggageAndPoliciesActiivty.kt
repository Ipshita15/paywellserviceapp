package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import kotlinx.android.synthetic.main.activity_baggage_and_policies_actiivty.*
import java.util.*


class BaggageAndPoliciesActiivty : AirTricketBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_baggage_and_policies_actiivty)
        setToolbar(getString(com.cloudwell.paywell.services.R.string.title_activity_baggage_and_policies_actiivty))

        setupViewPager(viewpager)
        tabs.setupWithViewPager(viewpager)


    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(RolesFragment(), getString(R.string.roles))
        adapter.addFragment(FlightFareFragment(), getString(R.string.fares_information))
        viewPager.adapter = adapter
    }


    internal inner class ViewPagerAdapter(var manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position]
        }
    }


}
