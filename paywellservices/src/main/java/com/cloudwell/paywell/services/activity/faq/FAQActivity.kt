package com.cloudwell.paywell.services.activity.faq

import android.os.Bundle
import android.util.Log
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.faq.adapter.ExpandableListAdapter
import kotlinx.android.synthetic.main.content_f_a_q.*

class FAQActivity : BaseActivity() {

    var listAdapter: ExpandableListAdapter? = null
    var listDataHeader: List<String>? = null
    var listDataChild: HashMap<String, List<String>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_f_a_q)
        setToolbar(getString(R.string.faq))

        val isAfterLogin = intent.getBooleanExtra("isAfterLogin", false)

        prepareListData(isAfterLogin)

        listAdapter = ExpandableListAdapter(this, listDataHeader, listDataChild)

        // setting list adapter

        // setting list adapter
        expListView.setAdapter(listAdapter)
    }

    private fun prepareListData(isAfterLogin: Boolean) {
        listDataHeader = ArrayList()
        listDataChild = HashMap()
        listDataHeader = getResources().getStringArray(R.array.faq_header).toList()


        val listChild = getResources().getStringArray(R.array.faq_child).toList()

        (listDataHeader as List<String>).forEachIndexed { index, element ->
            val key = listDataHeader!!.get(index);
            val subData = mutableListOf<String>(listChild.get(index))
            listDataChild!!.put(key, subData.toList())

        }
        Log.e("", "")


//        // Adding child data
//        (listDataHeader as ArrayList<String>).add("পেওয়েল একাউন্ট খুলতে কি আমি অন্য কারও ট্রেড" + "লাইসেন্স ব্যবহার করতে পারি?")
//
//
//        // Adding child data
//        val top250: MutableList<String> = ArrayList()
//        top250.add("আমরা আপনাকে আপনার নিজস্ব ট্রেড লাইসেন্স ব্যাবহার করতে পরামর্শ দিব")
//        val nowShowing: MutableList<String> = ArrayList()
//        nowShowing.add("The Conjuring")
//        val comingSoon: MutableList<String> = ArrayList()
//        comingSoon.add("2 Guns")
//        listDataChild!![(listDataHeader as ArrayList<String>).get(0)] = top250 // Header, Child data
//        listDataChild!![(listDataHeader as ArrayList<String>).get(1)] = nowShowing
//        listDataChild!![(listDataHeader as ArrayList<String>).get(2)] = comingSoon
    }


}