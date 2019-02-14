package com.cloudwell.paywell.services.activity.notification.allNotificaiton

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.MainActivity
import com.cloudwell.paywell.services.activity.base.newBase.MVVMBaseActivity
import com.cloudwell.paywell.services.activity.notification.allNotificaiton.adapter.MsgAdapter
import com.cloudwell.paywell.services.activity.notification.allNotificaiton.view.NotificationViewStatus
import com.cloudwell.paywell.services.activity.notification.allNotificaiton.viewModel.NotificationViewModel
import com.cloudwell.paywell.services.activity.notification.model.NotificationDetailMessage
import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.NotificationFullViewActivity
import com.cloudwell.paywell.services.analytics.AnalyticsManager
import com.cloudwell.paywell.services.analytics.AnalyticsParameters
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.utils.AppHelper.startNotificationSyncService


class NotificationAllActivity : MVVMBaseActivity() {
    private var listView: ListView? = null
    private var mAppHandler: AppHandler? = null
    private var mLinearLayout: LinearLayout? = null

    private var position: Int = 0
    lateinit var adapter: MsgAdapter
    private var isNotificationFlow: Boolean = false

    private lateinit var viewModel: NotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_notification_view)

        setupToolbarTitle()
        initializer()
        initViewModel()
        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_NOTIFICATION_PAPGE)


    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotificationViewModel::class.java)

        viewModel.baseViewStatus.observe(this, Observer {
            handleViewCommonStatus(it)
        })

        viewModel.mViewStatus.observe(this, Observer {
            handleViewStatus(it)
        })

        subscribeDataStreams(viewModel)

        // call for data
        val isFlowForComingNewNotification = intent.getBooleanExtra(MainActivity.KEY_COMMING_NEW_NOTIFICATION, false);

        viewModel.onPullRequested(isFlowForComingNewNotification, isInternetConnection)

    }

    private fun handleViewStatus(status: NotificationViewStatus?) {
        when (status) {
            NotificationViewStatus.START_NOTFICATION_FULL_VIEW_ACTIVITY -> {
                startNotificationFullViewActivity()
            }

            NotificationViewStatus.START_NOTIFICATION_SERVICE -> startNotificationSyncService(applicationContext)

            NotificationViewStatus.NOTIFY_DATA_SET_CHANGE -> {
                adapter.notifyDataSetChanged()
            }

            NotificationViewStatus.SHOW_NO_NOTIFICAITON_FOUND -> {
                showSnackMessageWithTextMessage(getString(R.string.no_notification_msg))
            }
        }

    }

    private fun subscribeDataStreams(viewModel: NotificationViewModel) {
        viewModel.mListMutableLiveData.observe(this, Observer<List<NotificationDetailMessage>> { t ->
            this.setupAdapter(t!!)
        })

    }

    private fun setupAdapter(t: List<NotificationDetailMessage>) {
        adapter = MsgAdapter(this, t)
        listView!!.adapter = adapter
        listView!!.onItemClickListener = AdapterView.OnItemClickListener { _, view, msgPosition, id ->
            position = msgPosition
            viewModel.onItemClick(position)
        }

    }

    private fun startNotificationFullViewActivity() {
        startActivity(Intent(this@NotificationAllActivity, NotificationFullViewActivity::class.java))
    }


    private fun setupToolbarTitle() {
        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setTitle(com.cloudwell.paywell.services.R.string.home_notification)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    fun initializer() {
        isNotificationFlow = intent.getBooleanExtra(IS_NOTIFICATION_SHOWN, false)
        listView = findViewById(com.cloudwell.paywell.services.R.id.listViewNotification)
        mLinearLayout = findViewById(com.cloudwell.paywell.services.R.id.linearLayout)
        mAppHandler = AppHandler(this)
    }

    override fun onBackPressed() {
        if (isNotificationFlow) {
            val intent = Intent(this@NotificationAllActivity, MainActivity::class.java)
            intent.putExtra(IS_NOTIFICATION_SHOWN, true)
            startActivity(intent)
            finish()
        } else {
            finish()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }




    companion object {
        const val IS_NOTIFICATION_SHOWN = "isNotificationShown"
        var length: Int = 0

    }
}
