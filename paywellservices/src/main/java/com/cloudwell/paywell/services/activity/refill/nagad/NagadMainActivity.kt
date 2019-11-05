package com.cloudwell.paywell.services.activity.refill.nagad

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.refill.RefillBalanceMainActivity

class NagadMainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nagad_main)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(R.string.home_nagad_balance_title)
        }






    }



}
