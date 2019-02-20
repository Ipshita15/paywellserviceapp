package com.cloudwell.paywell.services.activity.base

import android.arch.lifecycle.ViewModel
import com.cloudwell.paywell.services.activity.base.newBase.BaseViewState
import com.cloudwell.paywell.services.activity.base.newBase.SingleLiveEvent

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 19/2/19.
 */
open class BaseViewModel : ViewModel() {
    val baseViewStatus = SingleLiveEvent<BaseViewState>()
}
