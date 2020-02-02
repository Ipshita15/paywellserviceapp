package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.text.Html
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.*
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model.BillDatum
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model.PalliBidyutBillPayRequest
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model.PalliBidyutBillPayResponse
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model.PallibidyutHistory
import com.cloudwell.paywell.services.activity.utility.pallibidyut.model.REBNotification
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.database.DatabaseClient
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.ConnectionDetector
import com.cloudwell.paywell.services.utils.DateUtils
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator
import com.cloudwell.paywell.services.utils.UniversalRecyclerViewAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pbbill_pay_new.*
import kotlinx.android.synthetic.main.pallibidyut_bill_details_view.view.*
import kotlinx.android.synthetic.main.pallibidyut_billpay_response_dialog.view.*
import kotlinx.android.synthetic.main.pallibidyut_billpay_view.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PBBillPayNewActivity : BaseActivity() {


    lateinit var cd: ConnectionDetector
    private var addNoFlag: Int = 0
    lateinit var universalRecyclerViewAdapter:UniversalRecyclerViewAdapter
    var billPayList:ArrayList<BillDatum> =ArrayList<BillDatum>()
    private val slideInAnim = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1f, Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f)
    private val slideOutAnim = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 1f,
            Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f)
    lateinit var appHandler: AppHandler
    var isCreateActivity=true
    private var getAllPallibidyutHistoryAsyncTask: AsyncTask<Void, Void, Void>? = null
    private var insertPallibidyutHistoryAsyncTask: AsyncTask<Void, Void, Void>? = null
    internal var billNumberList: MutableList<String> = java.util.ArrayList()
    private lateinit var adapter: ArrayAdapter<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pbbill_pay_new)

        if (supportActionBar != null) {
            supportActionBar!!.setTitle(R.string.home_utility_pb_billpay_title)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }


        cd = ConnectionDetector(AppController.getContext())
        appHandler=AppHandler.getmInstance(this)
        imageAddIV.setOnClickListener(View.OnClickListener {

            if (addNoFlag < AppHandler.MULTIPLE_TOPUP_LIMIT) {
                addAnotherNo()
            } else {
                val snackbar = Snackbar.make(PBBillPayMain, R.string.bill_limit_msg, Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        })
        submitButton.setOnClickListener {
            if (cd.isConnectingToInternet()) {
                showCurrentTopupLog()
            } else {
                val snackbar = Snackbar.make(PBBillPayMain, R.string.connection_error_msg, Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        }





        try {
            val data = intent.getStringExtra("REBNotification")
            if (data != "") {
                val gson = Gson()
                val REBNotification = gson.fromJson(data, REBNotification::class.java)
//                mBillNo.setText("" + TrxData.BillNo)
//                mAmount.setText("" + TrxData.BillAmount)
//                mComfirm.setText("" + getString(R.string.re_submit_reb))

                addAnotherNoWithBillNumber(REBNotification)

            }
        } catch (e: Exception) {
            addAnotherNo()
        }


        getAllPallibidyutHistoryAsyncTask = object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void?): Void? {

                billNumberList.clear()
                val pallibidyutHistories = DatabaseClient.getInstance(applicationContext).appDatabase.mUtilityDab().allPallibidyutHistoryHistory
                for (i in pallibidyutHistories.indices) {
                    billNumberList.add(pallibidyutHistories[i].bilNumber)
                }
                return null
            }

            override fun onPreExecute() {
                super.onPreExecute()

            }

            override fun onPostExecute(@Nullable list: Void?) {
                super.onPostExecute(list)

                adapter.notifyDataSetChanged()

            }

        }.execute()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_pb_bill, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onResume() {
        super.onResume()
        refreshLanguage()
    }

    private fun addAnotherNo() {
        ++addNoFlag
        val billView = layoutInflater.inflate(R.layout.pallibidyut_billpay_view, null)

        if (addNoFlag == 1) {
            billView.billViewRemoveImage.visibility=View.GONE
        } else {

        }

        billView.billViewRemoveImage.setOnClickListener(View.OnClickListener {
            --addNoFlag
            slideOutAnim
                    .setAnimationListener(object : Animation.AnimationListener {

                        override fun onAnimationStart(animation: Animation) {}

                        override fun onAnimationRepeat(animation: Animation) {}

                        override fun onAnimationEnd(animation: Animation) {
                            billView.postDelayed({ billMainLL.removeView(billView) }, 100)
                        }
                    })
            billView.startAnimation(slideOutAnim)
        })

        adapter= ArrayAdapter<String>(this@PBBillPayNewActivity, android.R.layout.select_dialog_item, billNumberList)
        billView.pbBillNumberET.setThreshold(1)
        billView.pbBillNumberET.setAdapter(adapter)
        billView.pbBillNumberET.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus -> billView.pbBillNumberET.showDropDown() })
        billView.pbBillNumberET.setOnTouchListener({ v, event ->
            billView.pbBillNumberET.showDropDown()
            false
        })

        billMainLL.addView(billView)
        billView.startAnimation(slideInAnim)
    }

    private fun addAnotherNoWithBillNumber(rebNotification: REBNotification) {
        ++addNoFlag
        val billView = layoutInflater.inflate(R.layout.pallibidyut_billpay_view, null)
        billView.pbBillNumberET.setText(""+rebNotification.TrxData.BillNo)
        billView.pbBillNumberET.setFocusable(false)
        billView.pbBillAmountET.setText(""+rebNotification.TrxData.BillAmount)


        submitButton.setText("" + getString(R.string.re_submit_reb))
        imageAddIV.visibility = View.GONE


        if (addNoFlag == 1) {
            billView.billViewRemoveImage.visibility=View.GONE
        } else {

        }

        billView.billViewRemoveImage.setOnClickListener(View.OnClickListener {
            --addNoFlag
            slideOutAnim
                    .setAnimationListener(object : Animation.AnimationListener {

                        override fun onAnimationStart(animation: Animation) {}

                        override fun onAnimationRepeat(animation: Animation) {}

                        override fun onAnimationEnd(animation: Animation) {
                            billView.postDelayed({ billMainLL.removeView(billView) }, 100)
                        }
                    })
            billView.startAnimation(slideOutAnim)
        })

        adapter= ArrayAdapter<String>(this@PBBillPayNewActivity, android.R.layout.select_dialog_item, billNumberList)
        billView.pbBillNumberET.setThreshold(1)
        billView.pbBillNumberET.setAdapter(adapter)
        billView.pbBillNumberET.setOnTouchListener({ v, event ->
            billView.pbBillNumberET.showDropDown()
            false
        })

        billMainLL.addView(billView)
        billView.startAnimation(slideInAnim)


    }

    private fun showCurrentTopupLog() {
        billPayList.clear()
        if (billMainLL.getChildCount() > 0) {
            val reqStrBuilder = StringBuilder()
            for (i in 0 until billMainLL.getChildCount()) {
                val singleBillView = billMainLL.getChildAt(i)
                if (singleBillView != null) {

                    val billNoET = singleBillView!!.pbBillNumberET
                    val amountET = singleBillView!!.pbBillAmountET

                    val billNoString = billNoET.getText().toString()
                    val amountString = amountET.getText().toString()

                    if (billNoString.length < 5) {
                        billNoET.setError(Html.fromHtml("<font color='red'>" + getString(R.string.bill_number_length) + "</font>"))
                        return
                    }
                    if (amountString.length < 1) {
                        amountET.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"))
                        return
                    }else if (amountString.toDouble() < 1) {
                        amountET.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_limit_error_msg) + "</font>"))
                        return
                    }
                    reqStrBuilder.append((i + 1).toString() + ". " + getString(R.string.bill_number)+ " " + billNoString
                            + "\n " + getString(R.string.amount_des) + " " + amountString + getString(R.string.tk)+"\n\n")


                    val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(this).rid)


                    billPayList.add(BillDatum(amountString.toDouble(),billNoString,uniqueKey))


                }
            }
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.conf_bill_title_msg)
            builder.setMessage(reqStrBuilder)
            builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
                dialogInterface.dismiss()
                askForPin()
            }
            val alert = builder.create()
            alert.show()

        } else {
            val snackbar = Snackbar.make(PBBillPayMain, R.string.add_number_amount_msg, Snackbar.LENGTH_LONG)
            snackbar.setActionTextColor(Color.parseColor("#ffffff"))
            val snackBarView = snackbar.view
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
            snackbar.show()
        }
    }

    private fun askForPin() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.pin_no_title_msg)

        val pinNoET = EditText(this)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        pinNoET.gravity = Gravity.CENTER_HORIZONTAL
        pinNoET.layoutParams = lp
        pinNoET.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_VARIATION_PASSWORD
        pinNoET.transformationMethod = PasswordTransformationMethod.getInstance()
        builder.setView(pinNoET)

        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
            val inMethMan = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inMethMan.hideSoftInputFromWindow(pinNoET.windowToken, 0)

            if (pinNoET.text.toString().length != 0) {
                dialogInterface.dismiss()

                if (cd.isConnectingToInternet) {
                      sendBillDataToServer(billPayList,"json",pinNoET.text.toString(),appHandler.imeiNo)
                } else {
                    val snackbar = Snackbar.make(PBBillPayMain, R.string.connection_error_msg, Snackbar.LENGTH_LONG)
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                    val snackBarView = snackbar.view
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                    snackbar.show()
                }
            } else {
                val snackbar = Snackbar.make(PBBillPayMain, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        }
        val alert = builder.create()
        alert.show()
    }

    private fun sendBillDataToServer(billListData: List<BillDatum>, format: String, pinCode: String, userName: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        ApiUtils.getAPIService().postPalliBidyutBills(PalliBidyutBillPayRequest(billListData,format,pinCode,userName)).enqueue(object : Callback<PalliBidyutBillPayResponse>{
            override fun onFailure(call: Call<PalliBidyutBillPayResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(applicationContext,"Server error!!!",Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()

            }

            override fun onResponse(call: Call<PalliBidyutBillPayResponse>, response: Response<PalliBidyutBillPayResponse>) {

                if(response.isSuccessful){
                    val palliBidyutBillPayResponse:PalliBidyutBillPayResponse=response.body() as PalliBidyutBillPayResponse
                    showBillResponseDialog(palliBidyutBillPayResponse)
                }
                progressDialog.dismiss()
            }

        })

    }

    private fun showBillResponseDialog(palliBidyutBillPayResponse: PalliBidyutBillPayResponse) {



        val alertDialog=AlertDialog.Builder(this@PBBillPayNewActivity)
        val view=layoutInflater.inflate(R.layout.pallibidyut_billpay_response_dialog,null)
        val linearLayout=view.billViewLL
        for (x in 0 until palliBidyutBillPayResponse.responseDetails.size){
            val  billDetailsView=layoutInflater.inflate(R.layout.pallibidyut_bill_details_view,null)
            billDetailsView.billStatusTV.text=palliBidyutBillPayResponse.responseDetails.get(x).statusName
            if(palliBidyutBillPayResponse.responseDetails.get(x).status.toInt()==200){
                billDetailsView.billStatusTV.setTextColor(resources.getColor(R.color.colorPrimary))
                billDetailsView.serviceChargeTV.setText("Tk. "+palliBidyutBillPayResponse.responseDetails.get(x).extraCharge.toString())
                billDetailsView.commissionTV.setText("Tk. "+palliBidyutBillPayResponse.responseDetails.get(x).retailerCommission.toString())
                insertDataToDatabase()
            }else{
                billDetailsView.billStatusTV.setTextColor(resources.getColor(R.color.color_red))
                billDetailsView.serviceChargeLL.visibility=View.GONE
                billDetailsView.commissionLL.visibility=View.GONE
            }
            billDetailsView.serialNumTV.text=(x+1).toString()+"."
            billDetailsView.billTransactionTV.text=palliBidyutBillPayResponse.responseDetails.get(x).trxId
            billDetailsView.billNumTV.text=palliBidyutBillPayResponse.responseDetails.get(x).billNo
            billDetailsView.billAmountTV.text="Tk. "+palliBidyutBillPayResponse.responseDetails.get(x).billAmount.toString()
            linearLayout.addView(billDetailsView)
            val view=View(this@PBBillPayNewActivity)
            view.layoutParams= ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,2)
            view.setBackgroundColor(Color.parseColor("#edece8"))
            linearLayout.addView(view)


        }
        alertDialog.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.dismiss()
                for (i in 0 until billMainLL.getChildCount()) {
                    val singleBillView = billMainLL.getChildAt(i)
                    if (singleBillView != null) {
                        for (x in 0 until palliBidyutBillPayResponse.responseDetails.size){

                            if((singleBillView.pbBillNumberET as TextView).text.toString().equals(palliBidyutBillPayResponse.responseDetails.get(x).billNo)){
                                if(palliBidyutBillPayResponse.responseDetails.get(x).status.toInt()==200){
                                    billMainLL.removeView(singleBillView)
                                    --addNoFlag
                                }else{
                                    isCreateActivity=false
                                }
                            }
                        }
                    }
                }
                if(billMainLL.childCount>0){
                    billMainLL.getChildAt(0).billViewRemoveImage.visibility=View.GONE
                }
                if(isCreateActivity){
                    startActivity(Intent(this@PBBillPayNewActivity,PBBillPayNewActivity::class.java))
                    finish()
                }
            }
        })
        alertDialog.setView(view)
        alertDialog.setCancelable(false)
        alertDialog.create().show()

    }

    private fun insertDataToDatabase() {
        insertPallibidyutHistoryAsyncTask = object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {

                for (i in 0 until billPayList.size) {
                    val currentDataAndTIme = DateUtils.currentDataAndTIme
                    val pallibidyutHistory = PallibidyutHistory()
                    pallibidyutHistory.bilNumber = billPayList.get(i).billNo
                    pallibidyutHistory.date = currentDataAndTIme
                    DatabaseClient.getInstance(applicationContext).appDatabase.mUtilityDab().insertPallibidyutHistory(pallibidyutHistory)
                }
                return null
            }
        }.execute()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.onBackPressed()

        }else if (item.itemId == R.id.action_info){

            showBillImageGobal(R.drawable.ic_poli_biddut_sms_bill_number)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (getAllPallibidyutHistoryAsyncTask != null) {
            getAllPallibidyutHistoryAsyncTask!!.cancel(true)

        }

        if (insertPallibidyutHistoryAsyncTask != null) {
            insertPallibidyutHistoryAsyncTask!!.cancel(true)
        }


        finish()
    }

    private fun refreshLanguage() {

        if (appHandler.getAppLanguage().equals("en", ignoreCase = true)) {
            imageAddIV.setBackgroundResource(R.drawable.add_en)
        } else {
            imageAddIV.setBackgroundResource(R.drawable.add_bn)
        }
    }


}
