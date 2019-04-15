package com.cloudwell.paywell.services.activity.eticket.airticket.booking

import android.Manifest
import android.app.DownloadManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.BookingCancelActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.BookingStatusListAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.ItemClickListener
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.fragment.PriceChangeFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.fragment.ThicketActionMenuFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.fragment.TricketChooserFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model.BookingStatuViewStatus
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.viewModel.BookingStatuViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.menu.AirTicketMenuActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.TicketViewerActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.emailTicket.PassengerEmailSendListActivity
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_booking_main.*
import kotlinx.android.synthetic.main.item_booking_status.*
import java.io.File


class BookingStatusActivity : AirTricketBaseActivity() {
    lateinit var tag: String

    private lateinit var viewMode: BookingStatuViewModel

    var pinNumber: String = ""
    var bookingId: String = ""
    var limit: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_booking_main)
        setToolbar(getString(com.cloudwell.paywell.services.R.string.booking_status_menu))

        val bundle = intent.extras
        limit = bundle.getInt(AirTicketMenuActivity.KEY_LIMIT)

        initViewModel(limit)
    }


    private fun initViewModel(limit: Int) {

        viewMode = ViewModelProviders.of(this).get(BookingStatuViewModel::class.java)

        viewMode.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewCommonStatus(it)
        })


        viewMode.mViewStatus.observe(this, Observer {
            it?.let { it1 -> handleViewStatus(it1) }
        })


        viewMode.responseList.observe(this, Observer {
            it?.let { it1 -> setupList(it1) }
        })



        viewMode.getBookingStatus(isInternetConnection, limit)


    }

    private fun handleViewStatus(it: BookingStatuViewStatus) {
        if (it.isShowProcessIndicatior) {
            showProgressDialog()
        } else if (!it.isShowProcessIndicatior) {
            dismissProgressDialog()
        }

        if (!it.successMessageTricketStatus.equals("")) {
            showMsg(it.successMessageTricketStatus)
        }

        if (it.modelPriceChange != null) {
            showTricketPriceChangeDialog(it.modelPriceChange!!)
        }
    }

    private fun showTricketPriceChangeDialog(modelPriceChange: List<com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model.Datum>) {


        val priceChangeFragment = PriceChangeFragment()
        priceChangeFragment.modelPriceChange = modelPriceChange

        priceChangeFragment.setOnClickHandlerTest(object : PriceChangeFragment.OnClickHandler {
            override fun onClickActionIssueTicket() {
                viewMode.issueTicket(isInternetConnection, pinNumber, bookingId, true)
            }
        })

        priceChangeFragment.show(supportFragmentManager, "dialog")


    }


    private fun showMsg(msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Message")
        builder.setMessage(msg)
        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
            viewMode.getBookingStatus(isInternetConnection, limit)

        }
        val alert = builder.create()
        alert.show()
    }

    fun setupList(responseList: BookingList) {
        val customAdapter = BookingStatusListAdapter(responseList, this.applicationContext, object : ItemClickListener {
            override fun onItemClick(datum: Datum) {
                showThicketIntentPopupMessage(datum)
            }

            override fun onActionButtonClick(position: Int, model: Datum) {
                showActionMenuPopupMessate(model)

            }

        })

        listBookingList.adapter = customAdapter

        val mLayoutManager = LinearLayoutManager(applicationContext)
        listBookingList.setLayoutManager(mLayoutManager)
        listBookingList.setItemAnimator(DefaultItemAnimator())

        tvSerialNumber.typeface = Typeface.DEFAULT_BOLD
        tvBookingId.typeface = Typeface.DEFAULT_BOLD
        tvDate.typeface = Typeface.DEFAULT_BOLD
        tvBookingStatus.typeface = Typeface.DEFAULT_BOLD
        tvAction.typeface = Typeface.DEFAULT_BOLD
    }

    private fun showActionMenuPopupMessate(model: Datum) {

        AppStorageBox.put(applicationContext, AppStorageBox.Key.BOOKING_STATUS_ITEM, model)


        val tricketChooserFragment = ThicketActionMenuFragment()

        tricketChooserFragment.setOnClickHandlerTest(object : ThicketActionMenuFragment.OnClickHandler {
            override fun onClickIssisTricketButton() {


                model.bookingId?.let {
                    bookingId = it
                    askForPin(it)
                }
            }

            override fun onClickCencelButton() {
                val model = AppStorageBox.get(applicationContext, AppStorageBox.Key.BOOKING_STATUS_ITEM) as Datum

                val intent = Intent(applicationContext, BookingCancelActivity::class.java)
                intent.putExtra(BookingCancelActivity.KEY_BOOKING_ID, model.bookingId)
                startActivity(intent)


            }
        })

        tricketChooserFragment.show(supportFragmentManager, "dialog")
    }


    private fun submitBookingListRequest(limit: Int, tag: String) {
        showProgressDialog()

    }

    private fun showThicketIntentPopupMessage(datum: Datum) {


        val tricketChooserFragment = TricketChooserFragment()

        tricketChooserFragment.setOnClickHandlerTest(object : TricketChooserFragment.OnClickHandler {
            override fun onClick(s: String) {
                if (s.equals("view")) {
                    downloadPDFFile(datum)
                } else if (s.equals("email")) {
                    openSendEmailActivity(datum)
                }
            }

        })

        tricketChooserFragment.show(supportFragmentManager, "dialog")
    }

    private fun openSendEmailActivity(datum: Datum) {

        AppStorageBox.put(applicationContext, AppStorageBox.Key.BOOKING_STATUS_ITEM, datum)

        val intent = Intent(this, PassengerEmailSendListActivity::class.java)
        startActivity(intent)


    }

    private fun downloadPDFFile(datum: Datum) {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {

                            try {
                                if (isInternetConnection) {
                                    handlePDFDownload(datum)
                                } else {
                                    showNoInternetConnectionFound()
                                }
                            } catch (e: java.lang.Exception) {
                                Toast.makeText(applicationContext, getString(R.string.please_try_again), Toast.LENGTH_LONG).show()
                                dismissProgressDialog()
                            }


                        } else {

                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    private fun handlePDFDownload(datum: Datum) {
        val dir = File(Environment.getExternalStorageDirectory().toString() + File.separator + "PayWell/")
        dir.mkdirs()

        val split = datum.invoiceUrl.toString().split("/")
        val fileName = split.last()

        val pdfFile = File(dir.absolutePath, fileName)
        downloadAndOpenPdf(datum.invoiceUrl, pdfFile)
    }

    fun downloadAndOpenPdf(url: String, file: File) {
        if (!file.isFile()) {

            showProgressDialog()

            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val req = DownloadManager.Request(Uri.parse(url))
            req.setDestinationUri(Uri.fromFile(file))
            req.setTitle("Some title")

            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    dismissProgressDialog()

                    unregisterReceiver(this)
                    if (file.exists()) {
                        openPdfDocument(file, url)
                    }
                }
            }
            registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            dm.enqueue(req)
            Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show()
        } else {
            openPdfDocument(file, url)
        }
    }

    fun openPdfDocument(file: File, url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        try {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.setDataAndType(Uri.parse(file.absolutePath), "application/pdf")
            } else {
                var uri = Uri.parse(file.absolutePath)
                //val file = File(uri.path!!)
                if (file.exists()) {

                    uri = FileProvider.getUriForFile(applicationContext, applicationContext.getPackageName() + ".provider", file)
                    intent.setDataAndType(uri, "application/pdf")
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)

        } catch (e: Exception) {
            Logger.e("" + e.message)

            openPDFWebView(url)

        }

    }

    private fun openPDFWebView(url: String) {
        if (isInternetConnection) {
            val intent = Intent(this, TicketViewerActivity::class.java)
            intent.putExtra("url", url)
            startActivity(intent)
        } else {
            showNoInternetConnectionFound()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }


    private fun askForPin(bookingId: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.pin_no_title_msg)

        val pinNoET = EditText(this)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        pinNoET.gravity = Gravity.CENTER_HORIZONTAL
        pinNoET.layoutParams = lp
        pinNoET.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_VARIATION_PASSWORD
        pinNoET.transformationMethod = PasswordTransformationMethod.getInstance()
        builder.setView(pinNoET)

        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
            val inMethMan = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inMethMan.hideSoftInputFromWindow(pinNoET.windowToken, 0)

            if (pinNoET.text.toString().length != 0) {
                dialogInterface.dismiss()
                pinNumber = pinNoET.text.toString().trim()
                viewMode.issueTicket(isInternetConnection, pinNumber, bookingId, false)
            } else {
                val snackbar = Snackbar.make(mCoordinateLayout, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        }
        builder.setNegativeButton(R.string.cancel_btn) { dialogInterface, i -> dialogInterface.dismiss() }
        val alert = builder.create()
        alert.show()
    }


}
