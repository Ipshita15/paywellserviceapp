package com.cloudwell.paywell.services.activity.refill.nagad

import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.refill.RefillBalanceMainActivity
import com.cloudwell.paywell.services.activity.refill.banktransfer.BankTransferMainActivity
import com.cloudwell.paywell.services.activity.refill.card.CardTransferMainActivity
import com.cloudwell.paywell.services.activity.topup.TransLogActivity
import com.cloudwell.paywell.services.analytics.AnalyticsManager
import com.cloudwell.paywell.services.analytics.AnalyticsParameters
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.utils.ConnectionDetector
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_booking_main.*
import kotlinx.android.synthetic.main.activity_nagad_main.*
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import java.util.ArrayList

class NagadMainActivity : BaseActivity() {

    private var mCd: ConnectionDetector? = null

    private var mAppHandler: AppHandler? = null
    private var nagadConstrainLayout: ConstraintLayout? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nagad_main)
        setToolbar(getString(R.string.home_nagad_balance_title))

        initView()

    }

    private fun initView() {
        mAppHandler = AppHandler.getmInstance(applicationContext)
        nagadConstrainLayout = findViewById<ConstraintLayout>(R.id.nagadConstrainLayout)
        mCd = ConnectionDetector(AppController.getContext())


        var mAppHandler = AppHandler.mInstance

        if (mAppHandler.getAppLanguage().equals("en", ignoreCase = true)) {
            nagadBalanceRefill.typeface = AppController.getInstance().oxygenLightFont

            nagadQRCode.typeface = AppController.getInstance().oxygenLightFont
            nagadBalanceClaim.typeface = AppController.getInstance().oxygenLightFont
            nagadRefillLog.typeface = AppController.getInstance().oxygenLightFont
        } else {
            nagadBalanceRefill.typeface = AppController.getInstance().aponaLohitFont

            nagadQRCode.typeface = AppController.getInstance().aponaLohitFont
            nagadBalanceClaim.typeface = AppController.getInstance().aponaLohitFont
            nagadRefillLog.typeface = AppController.getInstance().aponaLohitFont
        }

        nagadBalanceRefill.setOnClickListener {
            val intent = Intent(this, BalanceRefillActivity::class.java)
            startActivity(intent)
        }




    }




}
