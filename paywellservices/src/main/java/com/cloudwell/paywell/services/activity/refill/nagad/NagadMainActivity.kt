package com.cloudwell.paywell.services.activity.refill.nagad

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.refill.nagad.fragment.MobileNumberQRCodeFragment
import kotlinx.android.synthetic.main.activity_nagad_main.*

class NagadMainActivity : AirTricketBaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nagad_main)

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(R.string.home_nagad_balance_title)
        }


        nagadBalanceClaim.setOnClickListener(this)
        nagadBalanceRefill.setOnClickListener(this)
        nagadQRCode.setOnClickListener(this)




    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.nagadBalanceClaim ->{
                val intent = Intent(applicationContext, NagadBalanceClaimActivity::class.java)
                startActivity(intent)
            }
            R.id.nagadBalanceRefill ->{
                val intent = Intent(this, BalanceRefillActivity::class.java)
                startActivity(intent)
            }
            R.id.nagadQRCode ->{

                val priceChangeFragment = MobileNumberQRCodeFragment()
                priceChangeFragment.show(supportFragmentManager, "dialog")
            }



        }


    }



}
