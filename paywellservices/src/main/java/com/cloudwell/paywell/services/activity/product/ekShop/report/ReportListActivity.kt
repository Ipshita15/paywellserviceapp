package com.cloudwell.paywell.services.activity.product.ekShop.report

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.recyclerview.widget.GridLayoutManager
import com.cloudwell.paywell.services.activity.base.ProductEecommerceBaseActivity
import com.cloudwell.paywell.services.activity.product.ekShop.model.OrderDetail
import com.cloudwell.paywell.services.activity.product.ekShop.report.adapter.HeaderRecyclerForEkShopReport
import com.cloudwell.paywell.services.activity.product.ekShop.report.adapter.OnClickHandler
import com.cloudwell.paywell.services.activity.product.ekShop.report.ui.report.ReportViewModel
import com.cloudwell.paywell.services.app.AppHandler
import com.google.gson.Gson
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.report_list_activity.*


class ReportListActivity : ProductEecommerceBaseActivity() {
    lateinit var sectionAdapter: SectionedRecyclerViewAdapter;

    private lateinit var viewModel: ReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.report_list_activity)
        setToolbar(getString(com.cloudwell.paywell.services.R.string.home_title_ek_shope))

        val stringExtra = intent.getStringExtra("data")
        val mcArray = Gson().fromJson(stringExtra, Array<com.cloudwell.paywell.services.activity.product.ekShop.model.Data>::class.java)

        val groupBy = mcArray.groupBy {
            it.requestDatetime?.split(" ")?.get(0)
        }


        sectionAdapter = SectionedRecyclerViewAdapter()

        val appHandler = AppHandler.getmInstance(applicationContext)
        val isEnglish = appHandler.getAppLanguage().equals("en", ignoreCase = true)

        for ((k, v) in groupBy) {
            val sectionData = k?.let {
                HeaderRecyclerForEkShopReport(it, v, isEnglish, object : OnClickHandler {
                    override fun onDetailsClick(orderDetails: List<OrderDetail>?) {
                        val toJson = Gson().toJson(orderDetails)
                        val intent = Intent(applicationContext, ReportDetailsActivity::class.java)
                        intent.putExtra("data", toJson)
                        startActivity(intent)
                    }


                })
            }
            sectionAdapter.addSection(sectionData)
        }


        val display = this.getWindowManager().getDefaultDisplay()
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = resources.displayMetrics.density
        val dpWidth = outMetrics.widthPixels / density
        val columns: Int;
        if (dpWidth > 320) {
            columns = 1;
        } else {
            columns = 1;
        }

        val glm = GridLayoutManager(applicationContext, columns)
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (sectionAdapter.getSectionItemViewType(position)) {
                    SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER ->
                        return columns
                    else ->
                        return 1
                }
            }
        }


        rvForEKShopReport.setLayoutManager(glm)
        rvForEKShopReport.setHasFixedSize(true)
        rvForEKShopReport.setAdapter(sectionAdapter)
        rvForEKShopReport.isNestedScrollingEnabled = false;


    }


}
