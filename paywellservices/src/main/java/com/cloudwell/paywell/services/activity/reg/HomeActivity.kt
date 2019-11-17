package com.cloudwell.paywell.services.activity.reg

import android.content.Intent
import android.os.Bundle
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        getSupportActionBar()?.hide()


        btRegistration.setOnClickListener {
            val intent = Intent(applicationContext, EntryMainActivity::class.java)
            startActivity(intent)
        }


    }
}
