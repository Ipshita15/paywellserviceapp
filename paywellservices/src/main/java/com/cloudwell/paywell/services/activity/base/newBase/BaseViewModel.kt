package com.cloudwell.paywell.services.activity.base.newBase

import android.arch.lifecycle.ViewModel
import com.cloudwell.paywell.services.activity.notification.NotificationRepogitory
import com.cloudwell.paywell.services.app.AppController

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/2/19.
 */
open class BaseViewModel : ViewModel() {
    val mNotificationRepository: NotificationRepogitory
    val status = SingleLiveEvent<BaseViewState>()

    init {
        mNotificationRepository = NotificationRepogitory(AppController.getContext())
    }


}
