package com.cloudwell.paywell.services.activity.notification

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.notification.model.NotificationDetailMessage
import com.cloudwell.paywell.services.activity.notification.model.ResNotificationAPI
import com.cloudwell.paywell.services.activity.notification.model.ResposeReScheduleNotificationAccept
import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.model.NotificationDetailMessageSync
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.database.DatabaseClient
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.orhanobut.logger.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 6/2/19.
 */
class NotificationRepogitory(private val mContext: Context) {
    private var mAppHandler: AppHandler? = null

    val remoteNotificationDate: MutableLiveData<ResNotificationAPI>
        get() {
            mAppHandler = AppHandler.getmInstance(mContext)
            val url = mContext.getString(R.string.notif_url)
            val userName = mAppHandler!!.imeiNo
            val mesType = "all_message"
            val messageStatus = "all"
            val format = "json"
            val data = MutableLiveData<ResNotificationAPI>()
            val resNotificationAPICall = ApiUtils.getAPIService().callNotificationAPI(url, userName, mesType, messageStatus, format)
            resNotificationAPICall.enqueue(object : Callback<ResNotificationAPI> {
                override fun onResponse(call: Call<ResNotificationAPI>, response: Response<ResNotificationAPI>) {

                    if (response.isSuccessful) {
                        data.value = response.body()
                    } else {
                        data.value = null
                    }
                }

                override fun onFailure(call: Call<ResNotificationAPI>, t: Throwable) {
                    data.value = null

                }
            })

            return data
        }


    fun getLocalNotificationData(): LiveData<MutableList<NotificationDetailMessage>>? {
        return LiveDataReactiveStreams.fromPublisher(DatabaseClient.getInstance(mContext).appDatabase.mNotificationDab().all)
    }

    fun saveNotificationData(mNotificationDetailMessage: MutableList<NotificationDetailMessage>) {

        val insert = DatabaseClient.getInstance(mContext).appDatabase.mNotificationDab().insert(mNotificationDetailMessage)
        Logger.v("Notificaiton data insert" + insert)


    }

    fun saveNotificationData(mNotificationDetailMessage: NotificationDetailMessage) {

        val insert = DatabaseClient.getInstance(mContext).appDatabase.mNotificationDab().insert(mNotificationDetailMessage)
        Logger.v("Notificaiton data insert" + insert)


    }

    fun updateNotificationStatus(itemData: NotificationDetailMessage) {


        val update = DatabaseClient.getInstance(mContext).appDatabase.mNotificationDab().update(itemData)
        Logger.v("Update notification status" + update)

    }

    fun addNotificationSyncData(itemData: NotificationDetailMessageSync) {
        DatabaseClient.getInstance(mContext).appDatabase.mNotificationDab().insertSync(itemData)
    }

    fun getNotificationSyncData(): List<NotificationDetailMessageSync> {
        return DatabaseClient.getInstance(mContext).appDatabase.mNotificationDab().allLSyncData
    }

    fun deletedSyncData(messageId: String) {
        DatabaseClient.getInstance(mContext).appDatabase.mNotificationDab().deleteSync(messageId)
    }

    fun clearNotificationData() {
        DatabaseClient.getInstance(mContext).appDatabase.mNotificationDab().deletedALl()
    }

    fun saveNotificationDetailsData(json: String?) {
        AppStorageBox.put(mContext, AppStorageBox.Key.NOTIFICATION_DETAILS, json);
    }

    fun getNotificationDetailsData(): String {
        return AppStorageBox.get(mContext, AppStorageBox.Key.NOTIFICATION_DETAILS).toString()
    }

    fun insertLocalData(detailMessages: List<NotificationDetailMessage>) {
        DatabaseClient.getInstance(mContext).appDatabase.mNotificationDab().insert(detailMessages)
    }

    fun deletedNotificationData(notificationExpireList: List<NotificationDetailMessage>) {
        DatabaseClient.getInstance(mContext).appDatabase.mNotificationDab().deleteData(notificationExpireList)

    }

    fun callReScheduleNotificationAccept(id: Int, accept_status: Int): MutableLiveData<ResposeReScheduleNotificationAccept> {
        mAppHandler = AppHandler.getmInstance(mContext)
        val url = mContext.getString(R.string.notif_url)
        val userName = mAppHandler!!.imeiNo
        val data = MutableLiveData<ResposeReScheduleNotificationAccept>()
        val resNotificationAPICall = ApiUtils.getAPIService().reIssueNotificationAccept(userName, id, accept_status)
        resNotificationAPICall.enqueue(object : Callback<ResposeReScheduleNotificationAccept> {
            override fun onResponse(call: Call<ResposeReScheduleNotificationAccept>, response: Response<ResposeReScheduleNotificationAccept>) {

                if (response.isSuccessful) {
                    data.value = response.body()
                } else {
                    data.value = null
                }
            }

            override fun onFailure(call: Call<ResposeReScheduleNotificationAccept>, t: Throwable) {
                data.value = null

            }
        })
        return data

    }

}
