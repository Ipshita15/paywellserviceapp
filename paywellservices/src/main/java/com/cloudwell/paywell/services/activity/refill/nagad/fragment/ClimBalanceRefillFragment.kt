package com.cloudwell.paywell.services.activity.refill.nagad.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.refill.nagad.BalanceRefillFragment
import com.cloudwell.paywell.services.activity.refill.nagad.UssdRefillFragment
import com.google.android.material.tabs.TabLayout
import java.util.*
import kotlin.collections.ArrayList

class ClimBalanceRefillFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.clim_balance_refil_fragment_layout, container, false)

        return view
    }

    override fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // find views by id
        val viewPager: ViewPager = view.findViewById(R.id.viewpager_in_fragment)
        val tabLayout: TabLayout = view.findViewById(R.id.tabs_in_fragment)

        // attach tablayout with viewpager
        tabLayout.setupWithViewPager(viewPager)
        val adapter = ViewPagerAdapter(childFragmentManager)

        // add your fragments
        adapter.addFrag(BalanceRefillFragment(), "নগদ অ্যাপ")
        adapter.addFrag(UssdRefillFragment(), "USSD রিফিল প্রক্রিয়া")

        // set adapter on viewpager
        viewPager.adapter = adapter
    }
//
    public class ViewPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
     var mFragmentList: ArrayList<Fragment> = ArrayList()
     var mFragmentTitleList: ArrayList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position);
        }

        override fun getCount(): Int {
            return mFragmentList.size;
        }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    fun addFrag(sampleFragment: Fragment, s: String) {
         mFragmentList.add(sampleFragment)
        mFragmentTitleList.add(s)
    }


    }
}
