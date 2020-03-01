package com.cloudwell.paywell.services.activity.BBC

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.MainActivity
import com.cloudwell.paywell.services.app.AppHandler
import kotlinx.android.synthetic.main.activity_bbc_main.*
import org.jetbrains.anko.toast

class BBC_Main_Activity : AppCompatActivity(), View.OnClickListener {

    private var mAppHandler: AppHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bbc_main)

        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(getString(R.string.bbc))
            supportActionBar!!.elevation = 0f
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#5aac40")));
        }

        mAppHandler = AppHandler.getmInstance(applicationContext)


        homeBtnEducation.setOnClickListener(this)
        homeBtnMiniStatement.setOnClickListener(this)
        homeBtnSettings.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.homeBtnEducation -> {

                toast(message = "Androidly Long Toasts", toastDuration = Toast.LENGTH_LONG)
                startActivity(Intent(applicationContext, CourseListActivity::class.java))
            }
            R.id.homeBtnMiniStatement -> {
                toast(message = "Androidly Long Toasts", toastDuration = Toast.LENGTH_LONG)
            }
            R.id.homeBtnSettings -> {
                toast(message = "Androidly Long Toasts", toastDuration = Toast.LENGTH_LONG)
            }
        }
    }

    fun Context.toast(context: Context = applicationContext, message: String, toastDuration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, toastDuration).show()
    }
}



