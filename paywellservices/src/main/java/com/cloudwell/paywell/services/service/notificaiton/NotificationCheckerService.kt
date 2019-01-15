package com.cloudwell.paywell.services.service.notificaiton

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.service.notificaiton.model.EventNewNotificaiton
import com.cloudwell.paywell.services.service.notificaiton.model.StartNotificationService
import com.orhanobut.logger.Logger
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
        EventBus.getDefault().register(this);

        callBalanceCheckAPI();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun callAgainBalanceCheckAPI(event: StartNotificationService) {
        callBalanceCheckAPI();

    };


    override fun onDestroy() {
        EventBus.getDefault().unregister(this);
        Logger.v("onDestroy")

        super.onDestroy()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)

    }


    private fun callBalanceCheckAPI() {
        isAPICalledRunning = true;

        val ah = AppHandler(applicationContext);
        val imeiNo = ah.getImeiNo()

        AndroidNetworking.post(getString(R.string.pw_bal))
                .addBodyParameter("imei_no", imeiNo)
                .setTag("test")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(object : StringRequestListener {
                    override fun onResponse(response: String) {
                        Logger.v(response)
                        isAPICalledRunning = false;

                        val splitArray = response.split("@".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                        if (splitArray.size > 4) {
                            val parseInt = Integer.parseInt(splitArray[4]);
                            if (parseInt > 0) {
                                EventBus.getDefault().post(EventNewNotificaiton(parseInt))
                            }
                        }
                    }

                    override fun onError(error: ANError) {
                        // handle error
                        Logger.e(error.localizedMessage)

                        isAPICalledRunning = false;
                    }
                })

    }


}

