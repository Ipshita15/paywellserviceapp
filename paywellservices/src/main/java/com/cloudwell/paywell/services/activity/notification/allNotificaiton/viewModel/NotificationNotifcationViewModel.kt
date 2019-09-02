package com.cloudwell.paywell.services.activity.notification.allNotificaiton.viewModel

import android.arch.lifecycle.MutableLiveData
import com.cloudwell.paywell.services.activity.base.newBase.BaseViewState
import com.cloudwell.paywell.services.activity.base.newBase.SingleLiveEvent
import com.cloudwell.paywell.services.activity.notification.BaseNotifcationViewModel
import com.cloudwell.paywell.services.activity.notification.allNotificaiton.view.NotificationViewStatus
import com.cloudwell.paywell.services.activity.notification.model.NotificationDetailMessage
import com.cloudwell.paywell.services.activity.notification.model.ResNotificationAPI
import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.model.NotificationDetailMessageSync
import com.cloudwell.paywell.services.utils.DateUtils.notificationDateFormat
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 6/2/19.
 */
class NotificationNotifcationViewModel : BaseNotifcationViewModel() {

    val mListMutableLiveData = MutableLiveData<List<NotificationDetailMessage>>()
    val mViewStatus = SingleLiveEvent<NotificationViewStatus>()

    fun onPullRequested(isFlowForComingNewNotification: Boolean, internetConnection: Boolean) {

        if (isFlowForComingNewNotification) {
            // new notification combing
            baseViewStatus.value = BaseViewState(true)

            if (internetConnection) {
                callNotificationRemoteAPI()
            } else {

                baseViewStatus.value = BaseViewState(isNoInternectConnectionFoud = true)
            }

        } else {
            mNotificationRepository.getLocalNotificationData()?.observeForever { detailMessages ->
                if (detailMessages?.size == 0) {
                    // first time in app
                    if (internetConnection) {
                        callNotificationRemoteAPI()
                    } else {
                        baseViewStatus.value = BaseViewState(isNoInternectConnectionFoud = true)
                    }
                } else {
                    // normal time
                    val filletDataByCurrentTime = filletDataByCurrentTime(detailMessages);
                    mListMutableLiveData.setValue(filletDataByCurrentTime)
                }
            }
        }

    }

    private fun callNotificationRemoteAPI() {

        baseViewStatus.value = BaseViewState(true)

        mNotificationRepository.remoteNotificationDate.observeForever { it ->
            baseViewStatus.value = BaseViewState(false)

            when {
                it == null -> mViewStatus.value = NotificationViewStatus.SHOW_NO_NOTIFICAITON_FOUND

                it.mNotificationDetailMessage.isEmpty() -> mViewStatus.value = NotificationViewStatus.SHOW_NO_NOTIFICAITON_FOUND

                else -> handleRespose(it)
            }

        }
    }

    private fun handleRespose(it: ResNotificationAPI?) {
        var dateFilterData = filletDataByCurrentTime(it?.mNotificationDetailMessage);

        // clear old data
        doAsync {
            mNotificationRepository.clearNotificationData();

            // save notification data
            uiThread {
                doAsync {
                    mNotificationRepository.saveNotificationData(dateFilterData as MutableList<NotificationDetailMessage>)
                }

            }
            // reversed ana show in UI
            uiThread {
                dateFilterData = filletDataByCurrentTime(dateFilterData);
                mListMutableLiveData.setValue(dateFilterData)
                baseViewStatus.value = BaseViewState(false)
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
            val date = SimpleDateFormat(notificationDateFormat, Locale.ENGLISH).parse(it.messageExpiryTime)
            currentTimeMillis < date.time
        }

        if (notificationList != null) {
            notificationList = notificationList.sortedBy {
                SimpleDateFormat(notificationDateFormat, Locale.ENGLISH).parse(it.addedDatetime).time
            }

            notificationList = notificationList.reversed()

        }
        return notificationList


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

    fun deleteNotification(messageId: String): MutableLiveData<String> {
        return mNotificationRepository.notificationDelete(messageId)
    }

    fun deleteNotificationFromLocal(notificationDetailMessage: List<NotificationDetailMessage>) {

        if (notificationDetailMessage != null) {
            doAsync {
                mNotificationRepository.deleteNotificationForLocalDB(notificationDetailMessage)
                uiThread {
                    refreshNotificationUI()
                }
            }
        }
    }


    fun refreshNotificationUI() {
        mNotificationRepository.getLocalNotificationData()?.observeForever { detailMessages ->

            val filletDataByCurrentTime = filletDataByCurrentTime(detailMessages);
            mListMutableLiveData.setValue(filletDataByCurrentTime)
            if (detailMessages?.size == 0) {
                mViewStatus.value = NotificationViewStatus.SHOW_NO_NOTIFICAITON_FOUND
            }
        }
    }


}


