package com.cloudwell.paywell.services.activity.notification.allNotificaiton

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.MainActivity
import com.cloudwell.paywell.services.activity.base.newBase.MVVMBaseActivity
import com.cloudwell.paywell.services.activity.notification.SwipeController
import com.cloudwell.paywell.services.activity.notification.SwipeController.SwipeControllerActions
import com.cloudwell.paywell.services.activity.notification.allNotificaiton.view.NotificationViewStatus
import com.cloudwell.paywell.services.activity.notification.allNotificaiton.viewModel.NotificationNotifcationViewModel
import com.cloudwell.paywell.services.activity.notification.model.NotificationDetailMessage
import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.NotificationFullViewActivity
import com.cloudwell.paywell.services.analytics.AnalyticsManager
import com.cloudwell.paywell.services.analytics.AnalyticsParameters
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.utils.AppHelper.startNotificationSyncService
import com.google.gson.JsonParser
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_notification_view.*
import kotlinx.android.synthetic.main.activity_notification_view.view.*
import kotlinx.android.synthetic.main.dialog_notification.view.*
import kotlinx.android.synthetic.main.layout_notificaiton_all.*
import org.apache.commons.lang3.StringEscapeUtils
import org.json.JSONObject


class NotificationAllActivity : MVVMBaseActivity(), SwipeControllerActions {
    private var listView: RecyclerView? = null
    private var mAppHandler: AppHandler? = null
    private var mLinearLayout: CoordinatorLayout? = null

    private var position: Int = 0
    lateinit var adapter: SimpleAdapter
    private var isNotificationFlow: Boolean = false

    lateinit var viewModel: NotificationNotifcationViewModel

    var previousPosition=-1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_view)

        initializer()
        initViewModel()
        setupToolbarTitle()

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_NOTIFICATION_PAPGE)


    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotificationNotifcationViewModel::class.java)

        viewModel.baseViewStatus.observe(this, Observer {
            handleViewCommonStatus(it)
        })

        viewModel.mViewStatus.observe(this, Observer {
            handleViewStatus(it)
        })

        subscribeDataStreams(viewModel)

        // call for data
        val isFlowForComingNewNotification = intent.getBooleanExtra(MainActivity.KEY_COMMING_NEW_NOTIFICATION, false);

        // check to need snc data or not

        var userUsedNotificationFLow = AppStorageBox.get(applicationContext, AppStorageBox.Key.USER_USED_NOTIFICAITON_FLOW) as? Boolean
        if (userUsedNotificationFLow == null){
            userUsedNotificationFLow = true
        }


        viewModel.onPullRequested(isInternetConnection, userUsedNotificationFLow)

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

            NotificationViewStatus.USER_FINISHED_NOTIFICATION_FLOW ->{
                AppStorageBox.put(applicationContext, AppStorageBox.Key.USER_USED_NOTIFICAITON_FLOW, true)
            }

            NotificationViewStatus.SHOW_NO_NOTIFICAITON_FOUND -> {
                showSnackMessageWithTextMessage(getString(R.string.no_notification_msg))

                listViewNotification.visibility = View.GONE
                noNotificationIMG.visibility = View.VISIBLE
                noNotificationTV.visibility = View.VISIBLE
                notificationDetailsTV.visibility = View.VISIBLE

            }

        }

    }

    private fun subscribeDataStreams(viewModel: NotificationNotifcationViewModel) {
        viewModel.mListMutableLiveData.observe(this, Observer<List<NotificationDetailMessage>> { t ->
            this.setupAdapter(t!!)
        })

    }

    private fun setupAdapter(t: List<NotificationDetailMessage>) {

        adapter=SimpleAdapter(ArrayList<NotificationDetailMessage>(), this)
        adapter.notifyDataSetChanged()
        adapter = SimpleAdapter(t, this)
        listView!!.adapter = adapter
        Log.d("TESTING<", previousPosition.toString())
        if(previousPosition>0){
            listView!!.scrollToPosition(previousPosition)
        }

        dismissProgressDialog()
    }

    private fun startNotificationFullViewActivity() {
        startActivity(Intent(this@NotificationAllActivity, NotificationFullViewActivity::class.java))
    }


    private fun setupToolbarTitle() {

        notification_toolbar.clear_all_ConstraintLayout.setOnClickListener {
            if (viewModel.mListMutableLiveData.value != null && viewModel.mListMutableLiveData.value!!.size > 0) {
                confirmDelete("Want to delete all notifications?")
            } else {
                Toast.makeText(applicationContext, "No notification available!!!", Toast.LENGTH_SHORT).show()
            }
        }
        setSupportActionBar(notification_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    fun initializer() {

        isNotificationFlow = intent.getBooleanExtra(IS_NOTIFICATION_SHOWN, false)
        listView = findViewById(R.id.listViewNotification)
        listView!!.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        mLinearLayout = findViewById(R.id.linearLayout)
        mAppHandler = AppHandler.getmInstance(applicationContext)

        val swipeController = object : SwipeController(this, listView) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                super.onSwiped(viewHolder, direction)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(listView)
        listView!!.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                swipeController.onDraw(c)
            }


        })

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


    class SimpleAdapter(private val t: List<NotificationDetailMessage>, context: Context) : RecyclerView.Adapter<SimpleAdapter.ViewHolder>() {

        var counter=0
        var context: Context=context
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.dialog_notification, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (counter>=3){
                counter=0
            }
            holder.title.text = t.get(position).messageSub


            // for air ticket notificaiton remove unwanted // date form message
            var testmessage = "" + t.get(position).message
            Logger.v(testmessage)
            testmessage = StringEscapeUtils.unescapeJava(testmessage)

            testmessage = testmessage.replace("\\", "")
            testmessage = testmessage.replace("\\\\".toRegex(), "")
            testmessage = testmessage.replace("\\\\\\\\".toRegex(), "")
            testmessage = testmessage.replace("\\\\\\\\\\\\".toRegex(), "")

            if (testmessage.contains("notification_action_type")) {
                val s1 = handleAirTicketActionNotificaiton(testmessage)

                val mesage = StringEscapeUtils.unescapeJava(s1);

                val spannableString = SpannableString(mesage);
                Linkify.addLinks(spannableString, Linkify.WEB_URLS);
                holder.message.text = mesage
            }else{


                val mMesage = StringEscapeUtils.unescapeJava(t.get(position).message);

                val spannableString = SpannableString(mMesage);
                Linkify.addLinks(spannableString, Linkify.WEB_URLS);
                // for normal notification
                holder.message.text = mMesage
            }


            holder.date.text=t.get(position).addedDatetime
            holder.notificationRandomImage.setImageResource(getImageDrawable(counter))

            counter++
            holder.itemView.setOnClickListener() {

                    (context as NotificationAllActivity).onRecyclerViewItemClick(position,"ADAPTER")

            }

            val model = t[position]

            if (model.status.equals("Unread")) {
                holder.notificationCardView.setCardBackgroundColor(Color.parseColor("#DAFBFB"))
            } else {
                holder.notificationCardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            }

        }

        private fun handleAirTicketActionNotificaiton(testmessage: String): String? {
            val jsonObject = JSONObject(testmessage)
            val original_message = jsonObject.getString("original_message")

            val s1 = StringEscapeUtils.unescapeJava(original_message);
            return s1
        }

        private fun getImageDrawable(counter: Int): Int {
            when(counter) {
                0 -> return R.drawable.notification_one
                1 -> return R.drawable.notification_two
                2 -> return R.drawable.notification_three
                else -> return R.drawable.notification_one
            }
        }

        fun remove(positions: ArrayList<Int>) {
            for (x in 0 until positions.size step 2) {
                (t as ArrayList).removeAt(x)
                notifyDataSetChanged()

            }
        }
        override fun getItemCount(): Int = t.size

        fun refreshList() {
            notifyDataSetChanged()
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title = view.title
            val message = view.message
            val date = view.date
            val notificationRandomImage = view.notificationRandomImage
            val notificationCardView:CardView = view.notificationCardView
        }
    }

    override fun onRightClicked(position: Int) {

        val positions: ArrayList<Int> = ArrayList()
        positions.add(position)
        val singleNotification = ArrayList<NotificationDetailMessage>()
        singleNotification.add(viewModel.mListMutableLiveData.value!!.get(position))
        deleteNotificationFromServer(singleNotification, positions)
    }
    override fun onRecyclerViewItemClick(position: Int, parent:String) {
        viewModel.onItemClick(position)

    }

    private fun deleteNotificationFromServer(messageIdList: List<NotificationDetailMessage>, positions: ArrayList<Int>) {
        showProgressDialog()
        val messageIdListString = StringBuilder("")

        for (x in 0 until messageIdList.size) {
            messageIdListString.append(messageIdList.get(x).messageId)
            if (!(x == messageIdList.size - 1)) {
                messageIdListString.append(",")
            }
            previousPosition=positions.get(x)-1
        }

        viewModel.deleteNotification(messageIdListString.toString()).observeForever {
            dismissProgressDialog()
            if (it != null && !it.isEmpty()) {
                val jsonObject = JsonParser().parse(it).asJsonObject
                val status: String = jsonObject.getAsJsonPrimitive("status").asString
                if (status == "200") {
                    viewModel.deleteNotificationFromLocal(messageIdList)

                    Toast.makeText(applicationContext, "Successfully deleted", Toast.LENGTH_SHORT).show()
                    Log.d("TEST", "Successfully deleted/ " + messageIdListString.toString())
                } else {
                    Toast.makeText(applicationContext, "Can't be deleted at this moment!!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun confirmDelete(message: String) {
        val positions: ArrayList<Int> = ArrayList()
        for (x in 0 until viewModel.mListMutableLiveData.value!!.size) {
            positions.add(x)
        }

        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Delete") { dialog, which ->
                    deleteNotificationFromServer(viewModel.mListMutableLiveData.value!!, positions)

                }.setNegativeButton("Cancel") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()

    }

}
