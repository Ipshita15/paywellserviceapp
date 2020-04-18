package com.cloudwell.paywell.services.activity.education.BBC

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.education.BBC.adapter.StatusCheckAdapter
import com.cloudwell.paywell.services.activity.education.BBC.model.StatusCheckCourseItem
import com.cloudwell.paywell.services.activity.education.BBC.model.StatusCheckResponsePojo
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_b_b_cstatus_check.*

class BBCstatusCheckActivity : AppCompatActivity() {

    var courses: List<StatusCheckCourseItem?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_b_cstatus_check)

        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(getString(R.string.check_status))
            supportActionBar!!.elevation = 0f
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#5aac40")));
        }


        val data = intent.getStringExtra("data")
        val response = Gson().fromJson(data, StatusCheckResponsePojo::class.java)
        courses = response.courses

        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        val dividerItemDecoration = DividerItemDecoration(bbc_status_list.getContext(),
                linearLayoutManager.getOrientation())
        bbc_status_list.addItemDecoration(dividerItemDecoration)
        bbc_status_list.layoutManager = linearLayoutManager

        val adapter = StatusCheckAdapter(applicationContext, courses)
        bbc_status_list.adapter = adapter

       // Toast.makeText(applicationContext, response.message+ courses?.size, Toast.LENGTH_LONG).show()
    }
}
