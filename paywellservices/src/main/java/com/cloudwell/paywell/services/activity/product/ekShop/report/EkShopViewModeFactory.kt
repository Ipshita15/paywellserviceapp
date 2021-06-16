package com.cloudwell.paywell.services.activity.product.ekShop.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cloudwell.paywell.services.activity.product.ekShop.report.ui.report.AKShopRepo
import com.cloudwell.paywell.services.activity.product.ekShop.report.ui.report.ReportViewModel

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-25.
 */

class EkShopViewModeFactory(private val aKShopRepo: AKShopRepo) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReportViewModel(aKShopRepo) as T
    }

}
