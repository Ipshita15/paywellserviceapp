package com.cloudwell.paywell.services.activity.refill.nagad

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import kotlinx.android.synthetic.main.activity_nagad_main.*

class NagadMainActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nagad_main)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(R.string.home_nagad_balance_title)
        }


        nagadBalanceClaim.setOnClickListener(this)




    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.nagadBalanceClaim ->{
                val intent = Intent(applicationContext, NagadBalanceClaimActivity::class.java)
                startActivity(intent)
            }


        }
    }



}
