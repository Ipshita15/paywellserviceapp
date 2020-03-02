package com.cloudwell.paywell.services.activity.BBC

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.BBC.model.RegistationInfo
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.ConnectionDetector
import kotlinx.android.synthetic.main.activity_bbc_main.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BBC_Main_Activity : BaseActivity(), View.OnClickListener {

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
               showEnquiryPrompt()
              //  toast(message = "Androidly Long Toasts", toastDuration = Toast.LENGTH_LONG)
            }
        }
    }




    fun Context.toast(context: Context = applicationContext, message: String, toastDuration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, toastDuration).show()
    }


    private fun showEnquiryPrompt() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.phone_no_title_msg)
        val enqNoET = EditText(this)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        enqNoET.layoutParams = lp
        enqNoET.inputType = InputType.TYPE_CLASS_NUMBER
        enqNoET.gravity = Gravity.CENTER_HORIZONTAL
        val FilterArray = arrayOfNulls<InputFilter>(1)
        FilterArray[0] = LengthFilter(11)
        enqNoET.filters = FilterArray
        builder.setView(enqNoET)
        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
            if (enqNoET.text.toString().length < 11) {
                showErrorMessagev1(getString(R.string.phone_no_error_msg))
                showEnquiryPrompt()
            } else {
                if (!isInternetConnection) {
                    AppHandler.showDialog(supportFragmentManager)
                } else {
                    getRegistationInfo(enqNoET.text.toString())
                }
                dialogInterface.dismiss()
            }
        }
        val alert = builder.create()
        alert.show()
    }


    private fun getRegistationInfo(mobile : String) {
        showProgressDialog()
        toast(message = "Androidly Long Toasts", toastDuration = Toast.LENGTH_LONG)
        val registation = RegistationInfo()
        registation.mobile = mobile
        registation.username = mAppHandler?.userName.toString()

        ApiUtils.getAPIServiceV2().getBBCregistationInfo(registation).enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dismissProgressDialog()
                showErrorMessagev1(getString(R.string.try_again_msg))

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    dismissProgressDialog()
                if (response.isSuccessful){
                    val js = JSONObject(response.body()?.string())
                    val status = js.getInt("status")
                    val msg = js.getString("message")

                    if (status == 200){



                    }else{
                        showErrorMessagev1(msg)
                    }

                   }else{
                    showErrorMessagev1(getString(R.string.try_again_msg))
                }

            }

        })
    }
}



