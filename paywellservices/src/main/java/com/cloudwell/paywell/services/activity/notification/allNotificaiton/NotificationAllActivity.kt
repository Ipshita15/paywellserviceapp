package com.cloudwell.paywell.services.activity.notification.allNotificaiton

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
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
import com.cloudwell.paywell.services.utils.AppHelper.startNotificationSyncService
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_notification_view.*
import kotlinx.android.synthetic.main.activity_notification_view.view.*
import kotlinx.android.synthetic.main.dialog_notification.view.*


class NotificationAllActivity : MVVMBaseActivity(), SwipeControllerActions {
    private var listView: RecyclerView? = null
    private var mAppHandler: AppHandler? = null
    private var mLinearLayout: LinearLayout? = null

    private var position: Int = 0
    lateinit var adapter: SimpleAdapter
    private var isNotificationFlow: Boolean = false

    lateinit var viewModel: NotificationNotifcationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_notification_view)

        setupToolbarTitle()
        initializer()
        initViewModel()
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
        val swipeController = object : SwipeController(this, listView) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                super.onSwiped(viewHolder, direction)
            }
        }
        adapter = SimpleAdapter(t,this,swipeController,listView!!)
        listView!!.adapter = adapter
//        val swipeHandler = object : SwipeToDeleteCallback(this) {
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val adapter = listView!!.adapter as SimpleAdapter
//                adapter.removeItem(viewHolder.adapterPosition)
//            }
//        }





        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(listView)
        listView!!.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
                swipeController.onDraw(c)
            }
        })
    }

    private fun startNotificationFullViewActivity() {
        startActivity(Intent(this@NotificationAllActivity, NotificationFullViewActivity::class.java))
    }


    private fun setupToolbarTitle() {

        notification_toolbar.clear_all_ConstraintLayout.setOnClickListener {
            deleteNotificationFromServer(viewModel.mListMutableLiveData.value!!)
        }
        setSupportActionBar(notification_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    fun initializer() {

        isNotificationFlow = intent.getBooleanExtra(IS_NOTIFICATION_SHOWN, false)
        listView = findViewById(com.cloudwell.paywell.services.R.id.listViewNotification)
        listView!!.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        mLinearLayout = findViewById(com.cloudwell.paywell.services.R.id.linearLayout)
        mAppHandler = AppHandler.getmInstance(applicationContext)

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

    abstract class SwipeToDeleteCallback(context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_deleted)
        private val intrinsicWidth = deleteIcon!!.intrinsicWidth
        private val intrinsicHeight = deleteIcon!!.intrinsicHeight
        private val background = ColorDrawable()
        private val backgroundColor = Color.parseColor("#f44336")
        private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

        override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
            /**
             * To disable "swipe" for specific item return 0 here.
             * For example:
             * if (viewHolder?.itemViewType == YourAdapter.SOME_TYPE) return 0
             * if (viewHolder?.adapterPosition == 0) return 0
             */
            if (viewHolder?.adapterPosition == 10) return 0
            return super.getMovementFlags(recyclerView, viewHolder)
        }

        override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
            return false
        }

        override fun onChildDraw(
                c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
        ) {

            val itemView = viewHolder.itemView
            val itemHeight = itemView.bottom - itemView.top
            val isCanceled = dX == 0f && !isCurrentlyActive

            if (isCanceled) {
                clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                return
            }

            // Draw the red delete background
            background.color = backgroundColor
            background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            background.draw(c)

            // Calculate position of delete icon
            val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
            val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
            val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
            val deleteIconRight = itemView.right - deleteIconMargin
            val deleteIconBottom = deleteIconTop + intrinsicHeight

            // Draw the delete icon
            deleteIcon!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            deleteIcon.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
            c?.drawRect(left, top, right, bottom, clearPaint)
        }
    }

    class SimpleAdapter(private val t: List<NotificationDetailMessage>, context: Context,swipeController: SwipeController,recyclerView: RecyclerView) : RecyclerView.Adapter<SimpleAdapter.ViewHolder>() {

        var swipeController: SwipeController=swipeController
        var recyclerView: RecyclerView=recyclerView
        public var counter=0
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
            holder.message.text = t.get(position).message
            holder.date.text=t.get(position).addedDatetime
            holder.notificationRandomImage.setImageResource(getImageDrawable(counter))
//            holder.notificationCardView.setOnClickListener{view->
//                Toast.makeText(context,"Test text",Toast.LENGTH_SHORT).show()
//            }
            counter++
            holder.itemView.setOnClickListener() {

                    (context as NotificationAllActivity).onRecyclerViewItemClick(position,"ADAPTER")

            }
        }
        private fun getImageDrawable(counter: Int): Int {
            when(counter) {
                0 -> return R.drawable.notification_one
                1 -> return R.drawable.notification_two
                2 -> return R.drawable.notification_three
                else -> return R.drawable.notification_one
            }
        }

        override fun getItemCount(): Int = t.size

        fun removeItem(position: Int) {
            var myList: MutableList<NotificationDetailMessage> = t as MutableList<NotificationDetailMessage>
            myList.removeAt(position)
            notifyDataSetChanged()
        }

//        class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
//                LayoutInflater.from(parent.context).inflate(R.layout.dialog_notification, parent, false)) {
//
//            fun bind(name: NotificationDetailMessage,counter: Int) = with(itemView) {
//                title.text = name.messageSub
//                message.text = name.message
//                date.text=name.addedDatetime
//                notificationRandomImage.setImageResource(getImageDrawable(counter))
//
//                getCradView().setImageResource(R.mipmap.paywell_icon)
//
//            }
//            fun getCradView():ImageView = with(itemView){
//               return notificationRandomImage
//            }
//
//        }
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title = view.title
            val message = view.message
            val date = view.date
            val notificationRandomImage = view.notificationRandomImage
            val notificationCardView = view.notificationCardView
        }
    }

    override fun onLeftClicked(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRightClicked(position: Int) {

        var singleNotification = ArrayList<NotificationDetailMessage>()
        singleNotification.add(viewModel.mListMutableLiveData.value!!.get(position))

        deleteNotificationFromServer(singleNotification)
    }
    override fun onRecyclerViewItemClick(position: Int, parent:String) {
        viewModel.onItemClick(position)

    }

    private fun deleteNotificationFromServer(messageIdList: List<NotificationDetailMessage>) {
        showProgressDialog()
        var messageIdListString = StringBuilder("")

        for (x in 0 until messageIdList.size) {
            messageIdListString.append(messageIdList.get(x).messageId)
            if (!(x == messageIdList.size - 1)) {
                messageIdListString.append(",")
            }
        }

        viewModel.deleteNotification(messageIdListString.toString()).observeForever {
            dismissProgressDialog()
            if (it != null && !it.isEmpty()) {
                val jsonObject = JsonParser().parse(it).asJsonObject
                var status: String = jsonObject.getAsJsonPrimitive("status").asString
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

}
