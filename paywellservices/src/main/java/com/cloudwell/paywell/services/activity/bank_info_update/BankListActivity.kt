package com.cloudwell.paywell.services.activity.bank_info_update

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.bank_info_update.model.BankListRequestPojo
import com.cloudwell.paywell.services.activity.bank_info_update.model.RemoveReqPojo
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.home.dialog.CommonDialogBtnInterface
import com.cloudwell.paywell.services.activity.home.dialog.CommonMessageDialog
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator
import kotlinx.android.synthetic.main.activity_bank_list.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BankListActivity : BaseActivity() {

    private var mAppHandler: AppHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_list)
        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(R.string.bank_list)
        }
        mAppHandler = AppHandler.getmInstance(applicationContext)

        getReadyToList()

        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,false)
        bank_list_recycler.layoutManager  = linearLayoutManager


    }

    private fun getReadyToList() {
        val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(applicationContext)!!.rid)
        val pojo : BankListRequestPojo = BankListRequestPojo()
        pojo.username = mAppHandler?.userName
        pojo.refId = uniqueKey
        if (isInternetConnection){
            getBankInfoList(pojo)
        }else{
            showErrorCallBackMessagev1(getString(R.string.internet_connection_error_msg))
        }
    }

    private fun getBankInfoList(pojo : BankListRequestPojo){
        showProgressDialog()

        ApiUtils.getAPIServiceV2().getRetailerBankList(pojo).enqueue(object : Callback<BankLinkedListResponsePojo> {
            override fun onFailure(call: Call<BankLinkedListResponsePojo>, t: Throwable) {
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<BankLinkedListResponsePojo>, response: Response<BankLinkedListResponsePojo>) {
                dismissProgressDialog()
                if (response.isSuccessful){

                    val listresponse : BankLinkedListResponsePojo = response.body()!!
                    if (listresponse.status == 200){
                        val bankDetailsList: List<BankDetailsListItem?>? = listresponse.bankDetailsList
                        val adapter = BankLink_ListAdapter(applicationContext, bankDetailsList, object : BankLink_ListAdapter.DelateInterface{
                            override fun OnDelate(bank: BankDetailsListItem?) {

                                val dialog : CommonMessageDialog = CommonMessageDialog(getString(R.string.bank_btn),
                                        getString(R.string.bank_delate),Color.RED, object : CommonDialogBtnInterface{
                                    override fun onclick() {
                                        val removePojo = RemoveReqPojo()
                                        removePojo.id = bank?.id
                                        val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(applicationContext)!!.rid)
                                        removePojo.refId = uniqueKey
                                        removePojo.username = mAppHandler?.userName
                                        removeBnkInfo(removePojo)
                                    }

                                    override fun onDismiss() {

                                    }
                                })
                            dialog.show(supportFragmentManager, "CONFIRMDIALOG")


                            }
                        })
                        bank_list_recycler.adapter = adapter

                    }else{
                        showErrorCallBackMessagev1(listresponse.message)
                    }

                }else{
                    showErrorCallBackMessagev1(getString(R.string.try_again_msg))
                }
            }

        })

    }


    private fun removeBnkInfo(pojo : RemoveReqPojo){
        showProgressDialog()

        ApiUtils.getAPIServiceV2().removeBankInfo(pojo).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                dismissProgressDialog()
                getReadyToList()

            }

        })
    }


}
