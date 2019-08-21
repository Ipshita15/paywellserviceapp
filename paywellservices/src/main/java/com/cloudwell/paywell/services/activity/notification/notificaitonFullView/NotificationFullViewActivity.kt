package com.cloudwell.paywell.services.activity.notification.notificaitonFullView

import android.content.Intent
import android.graphics.Point
import android.media.RingtoneManager
import android.os.Bundle
import android.text.SpannableString
import android.text.util.Linkify
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.newBase.MVVMBaseActivity
import com.cloudwell.paywell.services.activity.notification.ImageViewActivity
import com.cloudwell.paywell.services.activity.notification.allNotificaiton.NotificationAllActivity
import com.cloudwell.paywell.services.activity.notification.allNotificaiton.NotificationAllActivity.Companion.IS_NOTIFICATION_SHOWN
import com.cloudwell.paywell.services.activity.notification.model.NotificationDetailMessage
import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.view.NotificationFullViewStatus
import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.viewModel.NotificationFullNotifcationViewModel
import com.cloudwell.paywell.services.analytics.AnalyticsManager
import com.cloudwell.paywell.services.analytics.AnalyticsParameters
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.utils.AppHelper.startNotificationSyncService
import com.orhanobut.logger.Logger
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_notification_full_view.*
import org.apache.commons.lang3.StringEscapeUtils
import org.json.JSONException
import org.json.JSONObject


class NotificationFullViewActivity : MVVMBaseActivity() {

    private var mAppHandler: AppHandler? = null

    internal var isNotificationFlow: Boolean = false
    private lateinit var viewModel: NotificationFullNotifcationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_full_view)
        setToolbar(getString(R.string.home_notification_details))

        mAppHandler = AppHandler.getmInstance(applicationContext)

        initViewModel()

        handleLanguage()

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_NOTIFICATION_PAPGE_DETAILS);

    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotificationFullNotifcationViewModel::class.java)

        viewModel.baseViewStatus.observe(this, Observer {
            handleViewCommonStatus(it)
        })

        viewModel.mViewStatus.observe(this, Observer {
            handleViewStatus(it)
        })

        viewModel.mListMutableLiveData.observe(this, Observer {
            displayData(it)
        })

        isNotificationFlow = this.getIntent().getBooleanExtra("isNotificationFlow", false)

        var fcmNotificationDetails = ""
        try {
            fcmNotificationDetails = intent.extras.getString("Notification_Details", "") as String
        } catch (e: Exception) {
            fcmNotificationDetails = ""
        }

        viewModel.init(isNotificationFlow, fcmNotificationDetails)
    }

    private fun handleViewStatus(status: NotificationFullViewStatus?) {
        when (status) {
            NotificationFullViewStatus.START_NOTIFICATION_SERVICE -> startNotificationSyncService(applicationContext)
        }

    }

    fun handleLanguage() {
        if (mAppHandler?.getAppLanguage().equals("en")) {
            notiTitle?.setTypeface(AppController.getInstance().getOxygenLightFont());
            notiMessage?.setTypeface(AppController.getInstance().getOxygenLightFont());

        } else {
            notiTitle?.setTypeface(AppController.getInstance().getAponaLohitFont());
            notiMessage?.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }

    private fun displayData(it: NotificationDetailMessage?) {
        val message = it?.message
        val messageSub = it?.messageSub
        val imageUrl = it?.imageUrl

        var testmessage = "" + message
        Logger.v(testmessage)
        testmessage = StringEscapeUtils.unescapeJava(testmessage)

        testmessage = testmessage.replace("\\", "")
        testmessage = testmessage.replace("\\\\".toRegex(), "")
        testmessage = testmessage.replace("\\\\\\\\".toRegex(), "")
        testmessage = testmessage.replace("\\\\\\\\\\\\".toRegex(), "")

        if (testmessage.contains("notification_action_type")) {
            // air ticket flow
            handleAirTicket(testmessage, messageSub)

        } else {
            // normal

            btAccept.visibility = View.GONE
            btTicketCancel.visibility = View.GONE

            handleNormal(message, messageSub, imageUrl)
        }


    }

    private fun handleNormal(message: String?, messageSub: String?, imageUrl: String?) {
        val s1 = StringEscapeUtils.unescapeJava(message);

        val spannableString = SpannableString(s1);
        Linkify.addLinks(spannableString, Linkify.WEB_URLS);

        notiTitle.text = messageSub
        notiMessage.text = spannableString

        if (!imageUrl.equals("")) {
            showProgressDialog();
            notiImg?.setVisibility(View.VISIBLE);
            val display = getWindowManager().getDefaultDisplay();
            val size = Point();
            display.getSize(size);
            val width = size.x;


            Picasso.get().load(imageUrl)
                    .resize(width, 0)
                    .into(notiImg, object : Callback {
                        override fun onError(e: Exception?) {
                            dismissProgressDialog();
                        }

                        override fun onSuccess() {
                            dismissProgressDialog();
                        }
                    })
            notiImg.setOnClickListener {
                ImageViewActivity.TAG_IMAGE_URL = imageUrl
                val intent = Intent(this, ImageViewActivity::class.java)
                startActivity(intent)

            }
        }
    }

    private fun handleAirTicket(testmessage: String, messageSub: String?) {
        try {
            val jsonObject = JSONObject(testmessage)
            val notification_action_type = jsonObject.getString("notification_action_type")
            val original_message = jsonObject.getString("original_message")
            val id = jsonObject.getInt("id")

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val s1 = StringEscapeUtils.unescapeJava(original_message);

            val spannableString = SpannableString(s1);
            Linkify.addLinks(spannableString, Linkify.WEB_URLS);
            notiTitle.text = messageSub
            notiMessage.text = spannableString

            if (notification_action_type == "AirTicketReScheduleConfirmation") {
                btAccept.visibility = View.VISIBLE
                btTicketCancel.visibility = View.VISIBLE
                btAccept.setOnClickListener {

                    callAccept(id)
                }

                btTicketCancel.setOnClickListener {
                    callReject(id)

                }

            } else {
                btAccept.visibility = View.GONE
                btTicketCancel.visibility = View.GONE

            }

            try {
                //automatic call to API
                val action = intent.extras.getString("action", "")
                if (!action.equals("")) {
                    if (action.equals("Accept")) {
                        callAccept(id)
                    } else {
                        callReject(id)
                    }
                }
            } catch (e: Exception) {
                Logger.e("" + e.message)
            }


        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun callReject(id: Int) {
        showProgressDialog()
        viewModel.callReScheduleNotificationAccept(id, 2).observeForever {
            dismissProgressDialog()
            if (it != null) {
                showDialogMessage(it.message)
            }
        }
    }

    private fun callAccept(id: Int) {
        showProgressDialog()
        viewModel.callReScheduleNotificationAccept(id, 1).observeForever {
            dismissProgressDialog()
            if (it != null) {
                showdialogMessageWithFinishedActivity(it.message)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (isNotificationFlow) {
            val intent = Intent(this, NotificationAllActivity::class.java)
            intent.putExtra(IS_NOTIFICATION_SHOWN, true)
            startActivity(intent)
            finish()
        } else {
            finish()
        }
    }
}
