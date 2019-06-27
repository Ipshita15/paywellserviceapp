package com.cloudwell.paywell.services.activity.product.ekShop.report

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.cloudwell.paywell.services.activity.base.ProductEecommerceBaseActivity
import com.cloudwell.paywell.services.activity.product.ekShop.report.adapter.OrderDetailsAdapter
import com.cloudwell.paywell.services.app.AppHandler
import com.google.gson.Gson


class ReportDetailsActivity : ProductEecommerceBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.report_activity_details)
        setToolbar(getString(com.cloudwell.paywell.services.R.string.home_title_ek_shope))

        val stringExtra = intent.getStringExtra("data")
        val mcArray = Gson().fromJson(stringExtra, Array<com.cloudwell.paywell.services.activity.product.ekShop.model.OrderDetail>::class.java)


        val recyclerView = findViewById<View>(com.cloudwell.paywell.services.R.id.recycler_view) as RecyclerView
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        val appHandler = AppHandler.getmInstance(applicationContext)
        val isEnglish = appHandler.getAppLanguage().equals("en", ignoreCase = true)

        val mAdapter = OrderDetailsAdapter(mcArray, isEnglish)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setLayoutManager(mLayoutManager)
        recyclerView.setItemAnimator(DefaultItemAnimator())
        recyclerView.setAdapter(mAdapter)

    }

}
