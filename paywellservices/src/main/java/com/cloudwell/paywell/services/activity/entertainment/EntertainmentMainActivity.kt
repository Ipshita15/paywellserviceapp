package com.cloudwell.paywell.services.activity.entertainment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.entertainment.bongo.BongoMainActivity
import com.cloudwell.paywell.services.app.AppHandler
import kotlinx.android.synthetic.main.activity_entertainment_main.*


class EntertainmentMainActivity : BaseActivity() {

    private var mAppHandler: AppHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entertainment_main)


        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(getString(R.string.home_entertainment))
            supportActionBar!!.elevation = 0f
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#2d2e31")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.parseColor("#b31f2b"))
            };
        }

        mAppHandler = AppHandler.getmInstance(applicationContext)

        inilizzeData()
    }

    private fun inilizzeData() {

        entertainmentBtnBongo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, BongoMainActivity::class.java))
        })

    }
}
