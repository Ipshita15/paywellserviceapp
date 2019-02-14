package com.cloudwell.paywell.services.activity.base.newBase

import com.cloudwell.paywell.services.activity.base.BaseActivity

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 10/2/19.
 */
abstract class MVVMBaseActivity : BaseActivity() {
    fun handleViewCommonStatus(status: BaseViewState?) {
        if (status != null) {
            if (status.isProgressIndicatorShown) {
                showProgressDialog()
            }
            if (!status.isProgressIndicatorShown) {
                dismissProgressDialog()
            }
            if (status.isNoInternectConnectionFoud) {
                showNoInternetConnectionFound()
            }
        }
    }
}
