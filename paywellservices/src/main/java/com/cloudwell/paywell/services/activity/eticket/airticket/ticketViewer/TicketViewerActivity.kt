package com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.FileProvider
import android.widget.Toast
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.orhanobut.logger.Logger
import java.io.File


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/25/19.
 */
class TicketViewerActivity : AirTricketBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_ticket_view)
        setToolbar(getString(com.cloudwell.paywell.services.R.string.title_ticket_viewe))
        initViewInitialization()


    }

    private fun initViewInitialization() {


        val dir = File(Environment.getExternalStorageDirectory().toString() + File.separator + "PayWell/")
        dir.mkdirs()

        val pdfFile = File(dir.absolutePath, "HTS19032310529.pdf")





        downloadAndOpenPdf("https://notify.paywellonline.com/haltripFiles/HTS19032310529.pdf", pdfFile)


    }

    fun downloadAndOpenPdf(url: String, file: File) {
        if (!file.isFile()) {
            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val req = DownloadManager.Request(Uri.parse(url))
            req.setDestinationUri(Uri.fromFile(file))
            req.setTitle("Some title")

            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    unregisterReceiver(this)
                    if (file.exists()) {
                        openPdfDocument(file)
                    }
                }
            }
            registerReceiver(receiver, IntentFilter(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            dm!!.enqueue(req)
            Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show()
        } else {
            openPdfDocument(file)
        }
    }

    fun openPdfDocument(file: File) {

//        val fromFile = Uri.fromFile(file)
//
//        pdfView.fromUri(fromFile)
//                .spacing(10)
//                .swipeHorizontal(true)
//                .enableSwipe(true)
//                .enableDoubletap(true)
//                .pageFitPolicy(FitPolicy.WIDTH)
//                .autoSpacing(true)
//                .defaultPage(0).load()


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
        }

    }


}