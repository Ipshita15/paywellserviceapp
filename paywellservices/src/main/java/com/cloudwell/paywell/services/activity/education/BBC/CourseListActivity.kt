package com.cloudwell.paywell.services.activity.education.BBC

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.BBC.BBCsubscribeActivity
import com.cloudwell.paywell.services.activity.education.BBC.adapter.CourseListAdapter
import com.cloudwell.paywell.services.activity.education.BBC.model.CourseListRresponsePojo
import com.cloudwell.paywell.services.activity.education.BBC.model.CourseLlistRequestPojo
import com.cloudwell.paywell.services.activity.education.BBC.model.CoursesItem
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_course_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Sepon Email: ismailhossainsepon@gmail.com  Mobile: +8801612250477.
 */

class CourseListActivity : BaseActivity() {

    private var mAppHandler: AppHandler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_list)

        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle("BBC Course List")
            supportActionBar!!.elevation = 0f
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#5aac40")));
        }
        mAppHandler = AppHandler.getmInstance(applicationContext)

        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        bbc_course_list.layoutManager = linearLayoutManager

        getCourseList();

    }

    private fun getCourseList() {
        showProgressDialog()
        var courselist: List<CoursesItem>
        val pojo = CourseLlistRequestPojo()
        ApiUtils.getAPIServiceV2().getBBCcourseList(pojo).enqueue(object : Callback<CourseListRresponsePojo>{

            override fun onResponse(call: Call<CourseListRresponsePojo>, response: Response<CourseListRresponsePojo>) {
                dismissProgressDialog()
                if (response.isSuccessful){

                    if (response.body()?.status == 200){
                        courselist = response.body()?.courses as List<CoursesItem>
                        setadapter(courselist)

                    }else{
                        showErrorCallBackMessagev1(response.body()?.message)
                    }
                }
            }

            override fun onFailure(call: Call<CourseListRresponsePojo>, t: Throwable) {
                dismissProgressDialog()
                showErrorMessagev1(getString(R.string.try_again_msg))
            }

        })
    }

    private fun setadapter(courselist: List<CoursesItem>) {
        bbc_course_list.adapter = CourseListAdapter(applicationContext, courselist, object : CourseListAdapter.CourseOnClick{
            override fun onclick(course: CoursesItem) {
                val gson = Gson()
                val json = gson.toJson(course)
                val intent = Intent(applicationContext, BBCsubscribeActivity::class.java)
                intent.putExtra(getString(R.string.selectedCourse), json)
                applicationContext.startActivity(intent)
            }

        })
    }

    fun Context.toast(context: Context = applicationContext, message: String, toastDuration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, toastDuration).show()
    }


}
