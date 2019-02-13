package com.cloudwell.paywell.services.activity.notification.allNotificaiton

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.cloudwell.paywell.services.activity.MainActivity
import com.cloudwell.paywell.services.activity.base.newBase.MVVPBaseActivity
import com.cloudwell.paywell.services.activity.notification.allNotificaiton.view.NotificationViewStatus
import com.cloudwell.paywell.services.activity.notification.allNotificaiton.viewModel.NotificationViewModel
import com.cloudwell.paywell.services.activity.notification.model.NotificationDetailMessage
import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.NotificationFullViewActivity
import com.cloudwell.paywell.services.analytics.AnalyticsManager
import com.cloudwell.paywell.services.analytics.AnalyticsParameters
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.utils.AppHelper.startNotificationSyncService


class NotificationAllActivity : MVVPBaseActivity() {
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

        viewModel.status.observe(this, Observer {
            handleViewCommonStatus(it)
        })

        viewModel.mViewStatus.observe(this, Observer {
            handleViewStatus(it)
        })

        subscribeDataStreams(viewModel)

        // call for data
        viewModel.onPullRequested(intent)

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


    inner class MsgAdapter(private val mContext: Context, val t: List<NotificationDetailMessage>) : BaseAdapter() {
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return t.size
        }

        override fun getItem(position: Int): Any? {
            return t[position]
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val viewHolder: ViewHolder
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(com.cloudwell.paywell.services.R.layout.dialog_notification, parent, false)
                viewHolder = ViewHolder()
                viewHolder.title = convertView!!.findViewById(com.cloudwell.paywell.services.R.id.title)
                viewHolder.date = convertView.findViewById(com.cloudwell.paywell.services.R.id.date)
                viewHolder.msg = convertView.findViewById(com.cloudwell.paywell.services.R.id.message)
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
            }

            val model = t[position]

            if (model.status.equals("Unread")) {
                viewHolder.title!!.setTextColor(Color.parseColor("#ff0000"))
            } else {
                viewHolder.title!!.setTextColor(Color.parseColor("#355689"))
            }

            viewHolder.title!!.text = model.messageSub
            viewHolder.date!!.text = model.addedDatetime
            viewHolder.msg!!.text = model.message
            if (mAppHandler!!.appLanguage.equals("en", ignoreCase = true)) {
                viewHolder.title!!.typeface = AppController.getInstance().oxygenLightFont
                viewHolder.date!!.typeface = AppController.getInstance().oxygenLightFont
                viewHolder.msg!!.typeface = AppController.getInstance().oxygenLightFont
            } else {
                viewHolder.title!!.typeface = AppController.getInstance().aponaLohitFont
                viewHolder.date!!.typeface = AppController.getInstance().aponaLohitFont
                viewHolder.msg!!.typeface = AppController.getInstance().aponaLohitFont
            }
            return convertView
        }


        private inner class ViewHolder {
            internal var title: TextView? = null
            internal var date: TextView? = null
            internal var msg: TextView? = null
        }
    }

    companion object {
        const val IS_NOTIFICATION_SHOWN = "isNotificationShown"
        var length: Int = 0

    }
}
