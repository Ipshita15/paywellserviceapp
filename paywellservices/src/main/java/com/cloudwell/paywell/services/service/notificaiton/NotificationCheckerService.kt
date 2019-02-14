package com.cloudwell.paywell.services.service.notificaiton

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.cloudwell.paywell.services.activity.notification.NotificationRepogitory
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.eventBus.GlobalApplicationBus
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.service.notificaiton.model.APIResNoCheckNotification
import com.cloudwell.paywell.services.service.notificaiton.model.EventNewNotificaiton
import com.cloudwell.paywell.services.service.notificaiton.model.StartNotificationService
import com.orhanobut.logger.Logger
import com.squareup.otto.Subscribe
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 14/1/19.
 */
class NotificationCheckerService : Service() {
    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        var isAPICalledRunning = false;
    }

    override fun onCreate() {
        super.onCreate()
        try {
            GlobalApplicationBus.getBus().register(this);
        } catch (e: Exception) {

        }

        callnotificaitonCheckDetausAPI();
    }


    @Subscribe
    fun callAgainBalanceCheckAPI(event: StartNotificationService) {
        callnotificaitonCheckDetausAPI();

    };


    override fun onDestroy() {
        GlobalApplicationBus.getBus().unregister(this);
        Logger.v("onDestroy")
        super.onDestroy()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)

    }


    private fun callnotificaitonCheckDetausAPI() {
        isAPICalledRunning = true;
        val ah = AppHandler.getmInstance(applicationContext)
        val imeiNo = ah.getImeiNo()


        val responseBodyCall = ApiUtils.getAPIService().callCheckNotification(imeiNo)
        responseBodyCall.enqueue(object : Callback<APIResNoCheckNotification> {
            override fun onFailure(call: Call<APIResNoCheckNotification>, t: Throwable) {
                Logger.e(t.localizedMessage)
                isAPICalledRunning = false;
            }

            override fun onResponse(call: Call<APIResNoCheckNotification>, response: Response<APIResNoCheckNotification>) {
                Logger.v(response.toString())
                isAPICalledRunning = false;
                val unread = response.body()?.unread;
                val parseInt = Integer.parseInt(unread);
                GlobalApplicationBus.getBus().post(EventNewNotificaiton(parseInt))

                val detail_message = response.body()?.detail_message;
                if (detail_message != null) {
                    if (detail_message.size > 0) {
                        doAsync {
                            val notificationRepogitory = NotificationRepogitory(applicationContext);
                            notificationRepogitory.insertLocalData(detail_message)
                        }

                    }
                }
            }

        })


    }

}

