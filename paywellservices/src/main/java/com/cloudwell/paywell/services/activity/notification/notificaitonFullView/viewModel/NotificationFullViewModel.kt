package com.cloudwell.paywell.services.activity.notification.notificaitonFullView.viewModel

import android.arch.lifecycle.MutableLiveData
import com.cloudwell.paywell.services.activity.base.newBase.BaseViewModel
import com.cloudwell.paywell.services.activity.base.newBase.SingleLiveEvent
import com.cloudwell.paywell.services.activity.notification.model.NotificationDetailMessage
import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.model.NotificationDetailMessageSync
import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.view.NotificationFullViewStatus
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 10/2/19.
 */

class NotificationFullViewModel : BaseViewModel() {

    val mViewStatus = SingleLiveEvent<NotificationFullViewStatus>()
    val mListMutableLiveData = MutableLiveData<NotificationDetailMessage>()


    fun init(isNotificationFlow: Boolean, fcmNotificationDetailsJson: String) {
        if (isNotificationFlow) {
            handleNotificationClickFlow(fcmNotificationDetailsJson);
        } else {
            handleNormalFlow();
        }

    }

    private fun handleNormalFlow() {
        val stringData = mNotificationRepository.getNotificationDetailsData();
        val gson = Gson()
        val modelObject = gson.fromJson(stringData, NotificationDetailMessage::class.java)
        mListMutableLiveData.value = modelObject


    }

    private fun handleNotificationClickFlow(fcmNotificationDetailsJson: String) {

        val jsonObject = JSONObject(fcmNotificationDetailsJson);
        val message_id = jsonObject.getInt("message_id");
        val title = jsonObject.getString("title");
        val message = jsonObject.getString("message");
        val image = jsonObject.getString("image");

        val model = NotificationDetailMessage("2019-02-13 01:59:59", "", image, message, "" + message_id, title, "Read", "Notification", "2019-02-28 23:59:59");
        mListMutableLiveData.value = model


        doAsync {
            mNotificationRepository.saveNotificationData(model)
            val syncData = NotificationDetailMessageSync(model?.messageId!!)
            mNotificationRepository.addNotificationSyncData(syncData)

            uiThread {
                mViewStatus.value = NotificationFullViewStatus.START_NOTIFICATION_SERVICE
            }
        }
    }
}