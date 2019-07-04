package com.cloudwell.paywell.services.activity.eticket.airticket.base

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.BookingCancelActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.fragment.PriceChangeFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.fragment.TicketActionMenuFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.fragment.TricketChooserFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.fragment.TricketingStatusFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model.ResIssueTicket
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.viewModel.BookingStatsViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketCencel.TricketCancelActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.TicketViewerActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.emailTicket.PassengerEmailSendListActivity
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.constant.AllConstant
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_booking_main.*
import java.io.File

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-04-28.
 */
open class TransitionLogBaseActivity : AirTricketBaseActivity() {

    var limit: Int = 0
    lateinit var mViewMode: BookingStatsViewModel

    var pinNumber: String = ""
    var bookingId: String = ""


    fun showActionMenuPopupMessate(model: Datum) {
//        model.message = "Ticketed"

        AppStorageBox.put(applicationContext, AppStorageBox.Key.BOOKING_STATUS_ITEM, model)


        val tricketChooserFragment = TicketActionMenuFragment()

        tricketChooserFragment.setOnClickHandlerTest(object : TicketActionMenuFragment.OnClickHandler {
            override fun onReschedule(item: Datum) {

                if (item.journeyType.equals("MultiStop")) {
                    showDialogMesssage("MultiStop reschedule request can't accept")
                } else {
                    val mAppHandler = AppHandler.getmInstance(applicationContext)
                    val userName = mAppHandler.imeiNo
                    callCancelMapping(userName, item.bookingId!!, "", KEY_ReSchedule, item)
                }


            }

            override fun onTicketCancel(item: Datum) {

                TricketCancelActivity.model = item
                val intent = Intent(applicationContext, TricketCancelActivity::class.java)
                intent.putExtra(TricketCancelActivity.KEY_TITLE, AllConstant.Action_Ticket_Cancel)
                startActivity(intent)

            }

            override fun onReissue(item: Datum) {

                if (item.journeyType.equals("MultiStop")) {
                    showDialogMesssage("MultiStop reissue request can't accept")
                } else {
                    val mAppHandler = AppHandler.getmInstance(applicationContext)
                    val userName = mAppHandler.imeiNo
                    callCancelMapping(userName, item.bookingId!!, "", KEY_ReIssue, item)
                }
            }

            override fun onClickIsisThicketButton() {
                model.bookingId?.let {
                    bookingId = it
                    askForPin(it, AllConstant.Action_IsisThicket)
                }
            }

            override fun onClickCancelButton() {
                val model = AppStorageBox.get(applicationContext, AppStorageBox.Key.BOOKING_STATUS_ITEM) as Datum

                val intent = Intent(applicationContext, BookingCancelActivity::class.java)
                intent.putExtra(BookingCancelActivity.KEY_BOOKING_ID, model.bookingId)
                startActivity(intent)


            }
        })

        tricketChooserFragment.show(supportFragmentManager, "dialog")
    }


    public fun showThicketIntentPopupMessage(datum: Datum) {


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


    public fun showMsg(msg: String) {
        val t = TricketingStatusFragment()
        TricketingStatusFragment.message = msg
        t.show(supportFragmentManager, "dialog")


    }

    fun showTricketPriceChangeDialog(modelPriceChange: ResIssueTicket) {


        val priceChangeFragment = PriceChangeFragment()
        priceChangeFragment.modelPriceChange = modelPriceChange

        priceChangeFragment.setOnClickHandlerTest(object : PriceChangeFragment.OnClickHandler {
            override fun onClickActionIssueTicket() {
                mViewMode.issueTicket(isInternetConnection, pinNumber, bookingId, true)
            }
        })

        priceChangeFragment.show(supportFragmentManager, "dialog")


    }


    fun askForPin(bookingId: String, action: String) {
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

                if (action.equals(AllConstant.Action_IsisThicket)) {
                    mViewMode.issueTicket(isInternetConnection, pinNumber, bookingId, false)
                } else if (action.equals(AllConstant.Action_reIssueTicket)) {
                    mViewMode.issueTicket(isInternetConnection, pinNumber, bookingId, false)
                }


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
