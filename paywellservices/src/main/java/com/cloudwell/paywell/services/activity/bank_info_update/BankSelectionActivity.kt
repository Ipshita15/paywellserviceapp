package com.cloudwell.paywell.services.activity.bank_info_update

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.bank_info_update.model.BankListRequestPojo
import com.cloudwell.paywell.services.activity.bank_info_update.model.RemoveReqPojo
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator
import kotlinx.android.synthetic.main.activity_bank_selection.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BankSelectionActivity : BaseActivity() {

    private var mAppHandler: AppHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_selection)
        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(R.string.bank_btn)
        }

        mAppHandler = AppHandler.getmInstance(applicationContext)


        bank_link.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, BankINFO_MainActivity::class.java))

        })

        bank_list.setOnClickListener(View.OnClickListener {


            startActivity(Intent(this, BankListActivity::class.java))
        })
    }




}
