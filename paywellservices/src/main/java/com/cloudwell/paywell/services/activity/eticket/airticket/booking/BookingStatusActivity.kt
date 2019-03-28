package com.cloudwell.paywell.services.activity.eticket.airticket.booking

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.FileProvider
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.BookingStatusListAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.ItemClickListener
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.TicketViewerActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.emailTicket.PassengerEmailSendListActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.fragment.TricketChooserFragment
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

    lateinit var responseList: BookingList
    lateinit var tag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_booking_main)

        setToolbar(getString(com.cloudwell.paywell.services.R.string.booking_status_menu))

        responseList = AppStorageBox.get(applicationContext, AppStorageBox.Key.AIRTICKET_BOOKING_RESPONSE) as BookingList

        val bundle = intent.extras
        if (!bundle.isEmpty) {
            tag = bundle.getString("tag")
        }
        initViewInitialization()
    }

    private fun initViewInitialization() {
        val customAdapter = BookingStatusListAdapter(responseList, this.applicationContext, object : ItemClickListener {
            override fun onItemClick(datum: Datum) {
                showTricketIntentPopupMessage(datum)
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

    private fun showTricketIntentPopupMessage(datum: Datum) {


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


}
