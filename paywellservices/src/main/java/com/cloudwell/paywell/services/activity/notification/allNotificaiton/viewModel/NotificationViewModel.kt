package com.cloudwell.paywell.services.activity.notification.allNotificaiton.viewModel

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import com.cloudwell.paywell.services.activity.MainActivity
import com.cloudwell.paywell.services.activity.base.newBase.BaseViewModel
import com.cloudwell.paywell.services.activity.base.newBase.BaseViewState
import com.cloudwell.paywell.services.activity.base.newBase.SingleLiveEvent
import com.cloudwell.paywell.services.activity.notification.allNotificaiton.view.NotificationViewStatus
import com.cloudwell.paywell.services.activity.notification.model.NotificationDetailMessage
import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.model.NotificationDetailMessageSync
import com.cloudwell.paywell.services.utils.DateUtils.notificationDateFormat
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 6/2/19.
 */
class NotificationViewModel : BaseViewModel() {

    val mListMutableLiveData = MutableLiveData<List<NotificationDetailMessage>>()
    val mViewStatus = SingleLiveEvent<NotificationViewStatus>()

    fun onPullRequested(intent: Intent) {
        val booleanExtra = intent.getBooleanExtra(MainActivity.KEY_COMMING_NEW_NOTIFICATION, false);
        if (booleanExtra) {
            // new notification combing
            status.value = BaseViewState(true)
            callNotificationRemoteAPI()
        } else {
            mNotificationRepository.getLocalNotificationData()?.observeForever { detailMessages ->
                if (detailMessages?.size == 0) {
                    // first time in app
                    callNotificationRemoteAPI()
                } else {
                    // normal time
                    val filletDataByCurrentTime = filletDataByCurrentTime(detailMessages);
                    mListMutableLiveData.setValue(filletDataByCurrentTime)
                }
            }
        }


    }

    private fun callNotificationRemoteAPI() {

        status.value = BaseViewState(true)

        mNotificationRepository.remoteNotificationDate.observeForever { resNotificationAPI ->

            var dateFilterData = filletDataByCurrentTime(resNotificationAPI?.mNotificationDetailMessage);

            doAsync {

                mNotificationRepository.clearNotificationData();

                uiThread {
                    doAsync {
                        mNotificationRepository.saveNotificationData(dateFilterData as MutableList<NotificationDetailMessage>)
                    }

                }
                uiThread {
                    dateFilterData = dateFilterData?.reversed()
                    mListMutableLiveData.setValue(dateFilterData)
                    status.value = BaseViewState(false)
                }
            }

        }
    }

    private fun filletDataByCurrentTime(data: List<NotificationDetailMessage>?): List<NotificationDetailMessage>? {
        val currentTimeMillis = System.currentTimeMillis();


        val notificationExpireList = data?.filter {
            val date = SimpleDateFormat(notificationDateFormat).parse(it.messageExpiryTime)
            currentTimeMillis > date.time
        }

        val size = notificationExpireList?.size;

        if (size != null) {
            if (size > 0) {
                doAsync {
                    mNotificationRepository.deletedNotificationData(notificationExpireList)
                }

            }
        }


        var notificationList = data?.filter {
            val date = SimpleDateFormat(notificationDateFormat).parse(it.messageExpiryTime)
            currentTimeMillis < date.time
        }

        if (notificationList != null) {
            notificationList = notificationList.reversed()
        }

        return notificationList;


    }

    fun onItemClick(position: Int) {
        val itemData = mListMutableLiveData.value?.get(position)
        if (itemData?.status.equals("Unread")) {
            itemData?.status = "Read"
            itemData?.let {

                doAsync {
                    mNotificationRepository.updateNotificationStatus(it)
                    uiThread {
                        mViewStatus.value = NotificationViewStatus.NOTIFY_DATA_SET_CHANGE
                    }
                }
            }

            val syncData = NotificationDetailMessageSync(itemData?.messageId!!)
            syncData.let {
                doAsync {
                    mNotificationRepository.addNotificationSyncData(it)
                }
            }


            startSyncService()

            val gson = Gson()
            val json = gson.toJson(itemData)
            saveOnSharePre(json)
            mViewStatus.value = NotificationViewStatus.START_NOTFICATION_FULL_VIEW_ACTIVITY


        } else {

            val gson = Gson()
            val json = gson.toJson(itemData)
            saveOnSharePre(json)
            mViewStatus.value = NotificationViewStatus.START_NOTFICATION_FULL_VIEW_ACTIVITY
        }

    }

    private fun startSyncService() {
        mViewStatus.value = NotificationViewStatus.START_NOTIFICATION_SERVICE


    }

    private fun saveOnSharePre(json: String?) {
        mNotificationRepository.saveNotificationDetailsData(json);
    }


}


