package com.cloudwell.paywell.services.service.notificaiton

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.notification.NotificationRepogitory
import com.cloudwell.paywell.services.activity.notification.model.ResNotificationReadAPI
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 14/1/19.
 */
class NotificationDataSycService : Service() {


    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        var isAPICalledRunning = false;
    }

    override fun onCreate() {
        super.onCreate()

    }


    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        callnotificaitonCheckDetausAPI();
        return super.onStartCommand(intent, flags, startId)

    }


    private fun callnotificaitonCheckDetausAPI() {
        isAPICalledRunning = true;
        val ah = AppHandler(applicationContext);
        val imeiNo = ah.getImeiNo()


        doAsync {
            val notificaitonSyncData = NotificationRepogitory(applicationContext).getNotificationSyncData()

            uiThread {

                val notificationRepogitory = NotificationRepogitory(applicationContext)
                notificaitonSyncData.forEach {

                    val url = getString(R.string.notif_url)
                    val userName = imeiNo
                    val messageId = it.messageId
                    val format = "json"

                    val callNotificationReadAPI = ApiUtils.getAPIService().callNotificationReadAPI(url, userName, messageId, format);
                    callNotificationReadAPI.enqueue(object : Callback<ResNotificationReadAPI> {
                        override fun onFailure(call: Call<ResNotificationReadAPI>, t: Throwable) {
                            stopSelf()

                        }

                        override fun onResponse(call: Call<ResNotificationReadAPI>, response: Response<ResNotificationReadAPI>) {
                            if (response.isSuccessful) {
                                doAsync {
                                    NotificationRepogitory(applicationContext).deletedSyncData(it.messageId)


                                }

                            }
                        }

                    })


                }
            }


        }
    }

}

