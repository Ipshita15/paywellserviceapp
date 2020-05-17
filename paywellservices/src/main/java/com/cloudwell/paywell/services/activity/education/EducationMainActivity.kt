package com.cloudwell.paywell.services.activity.education

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.education.bbc.BBC_Main_Activity
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import kotlinx.android.synthetic.main.activity_education_main.*

class EducationMainActivity : BaseActivity() {

    private var mAppHandler: AppHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_main)
        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(getString(R.string.home_education))
            supportActionBar!!.elevation = 0f
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#5aac40")));
        }

        mAppHandler = AppHandler.getmInstance(applicationContext)

        inilizzeData()
    }

    private fun inilizzeData() {
        if (mAppHandler?.getAppLanguage().equals("en", ignoreCase = true)) {
            educationBtnBbc.setTypeface(AppController.getInstance().oxygenLightFont)
        } else {
            educationBtnBbc.setTypeface(AppController.getInstance().aponaLohitFont)
        }

        educationBtnBbc.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, BBC_Main_Activity::class.java))
        })


    }
}
