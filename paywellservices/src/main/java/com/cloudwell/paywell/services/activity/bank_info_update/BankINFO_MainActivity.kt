package com.cloudwell.paywell.services.activity.bank_info_update

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.bank_info_update.model.BankListRequestPojo
import com.cloudwell.paywell.services.activity.bank_info_update.model.BankPojo
import com.cloudwell.paywell.services.activity.bank_info_update.model.RemoveReqPojo
import com.cloudwell.paywell.services.activity.bank_info_update.spineer.*
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.refill.banktransfer.model.Branch
import com.cloudwell.paywell.services.activity.refill.banktransfer.model.DistrictData
import com.cloudwell.paywell.services.activity.refill.banktransfer.model.ReposeDistrictListerBankDeposit
import com.cloudwell.paywell.services.activity.refill.model.BranchData
import com.cloudwell.paywell.services.activity.refill.model.RequestBranch
import com.cloudwell.paywell.services.activity.refill.model.RequestDistrict
import com.cloudwell.paywell.services.activity.topup.TopupMainActivity
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.ImageUtility.getResizedBitmap
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator
import com.google.android.material.snackbar.Snackbar
import com.imagepicker.FilePickUtils.OnFileChoose
import kotlinx.android.synthetic.main.activity_bank_info_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class BankINFO_MainActivity : BaseActivity() {

    private var addNoFlag = 0
    private var bankAddLayout: LinearLayout? = null

    private var inflater: LayoutInflater? = null
    private val slideInAnim = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1F, Animation.RELATIVE_TO_PARENT, 0F,
            Animation.RELATIVE_TO_PARENT, 0F, Animation.RELATIVE_TO_PARENT, 0F)
    private val slideOutAnim = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0F, Animation.RELATIVE_TO_PARENT, 1F,
            Animation.RELATIVE_TO_PARENT, 0F, Animation.RELATIVE_TO_PARENT, 0F)

    private var mAppHandler: AppHandler? = null
    var bankAdd : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_info_main)
        bankAddLayout = findViewById(R.id.bankAddLayout)
        inflater = layoutInflater
        mAppHandler = AppHandler.getmInstance(applicationContext)
        slideInAnim.duration = 50
        slideOutAnim.duration = 50
        btnSubmit.setOnClickListener(View.OnClickListener {

            showCurrentBankinfo()

        })
        add_btn.setOnClickListener(View.OnClickListener {
            addAnotherBank()
        })

        addAnotherBank()



    }

    private fun addAnotherBank() {
        ++addNoFlag
        var bankId : String? = null
        var branchId : String? = null
        var districtId : String? = null
        var district_array: List<DistrictData>? = null
        var branch_array: List<Branch>? = null
        var bank_array :  List<BankDataItem>? = null
        val topUpView: View = inflater!!.inflate(R.layout.activity_bank_info_layout, null)
        val bank_spinner : Spinner = topUpView.findViewById(R.id.spinner_Bank)
        val district_spinner : Spinner = topUpView.findViewById(R.id.spinner_district)
        val branch_spinner : Spinner = topUpView.findViewById(R.id.spinner_branch)
        val removeBtn : Button = topUpView.findViewById(R.id.removeBankBtn)
        val check_book_btn : Button = topUpView.findViewById(R.id.check_book_btn)
        bankAdd = topUpView.findViewById(R.id.iamge)
        check_book_btn.setOnClickListener(View.OnClickListener {
            asked("ট্রেড লাইসেন্সের ছবি", "5")
        })
        if (addNoFlag == 1) {
            removeBtn.setVisibility(View.GONE)
        }
        if (isInternetConnection){
            getBank(object : BankCallBack {
                override fun onBankRecive(banks: List<BankDataItem>) {
                    bank_array = banks
                    bank_spinner.adapter = CustomBankAdapter(applicationContext, bank_array)

                }

            })
        }else{
            showErrorMessagev1(getString(R.string.internet_connection_error_msg))
        }

        removeBtn.setOnClickListener(View.OnClickListener {
            --addNoFlag
            slideOutAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    topUpView.postDelayed({ bankAddLayout?.removeView(topUpView) }, 100)
                }
            })
            topUpView.startAnimation(slideOutAnim)
        })

        bank_spinner.setOnItemSelectedListener(object : OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val bank_name : String = bank_array?.get(position)?.name.toString()
                bankId  = bank_array?.get(position)?.id.toString()
                Log.e(bank_name, bankId)
                if(isInternetConnection){
                    getDistricts(bankId!!, object : DistrictCallBack {
                        override fun onDataRecive(districts: List<DistrictData>) {
                            district_array = districts
                            district_spinner.adapter = CustomDistrictAdapter(applicationContext, district_array)
                        }
                    })
                }else{
                    showErrorMessagev1(getString(R.string.internet_connection_error_msg))
                }

            }

        })


        district_spinner.setOnItemSelectedListener(object : OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val dis_name : String = district_array?.get(position)?.district_name.toString()
                val dis_Id : String = district_array?.get(position)?.id.toString()
                Log.e(dis_name, dis_Id)

                if (isInternetConnection){

                }else{
                 showErrorMessagev1(getString(R.string.connection_error_msg))

                }
                getBranch(dis_Id, bankId, object : BranchCallBack {
                    override fun onBranchRecive(branches: List<Branch>) {
                        branch_array = branches
                        branch_spinner.adapter = CustomBranchAdapter(applicationContext, branch_array)
                    }
                })
            }

        })

        branch_spinner.setOnItemSelectedListener(object : OnItemSelectedListener{
          override fun onNothingSelected(parent: AdapterView<*>?) {

          }

          override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

             val branchName : String = branch_array?.get(position)?.name.toString()
              val branchId : String =branch_array?.get(position)?.id.toString()
              Log.e(branchName, branchId)
          }

      })


    bankAddLayout?.addView(topUpView)
    }

    private fun getBank(callback: BankCallBack){
            showProgressDialog()

        val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(applicationContext).rid)
            val pojo = GetBankPojo()
        pojo.username = mAppHandler?.userName
        pojo.ref_id = uniqueKey

        var bankresponse = BankResponse()

        ApiUtils.getAPIServiceV2().callBankDataAPI(pojo).enqueue(object : Callback<BankResponse>{
            override fun onFailure(call: Call<BankResponse>, t: Throwable) {
                dismissProgressDialog()
            }
            override fun onResponse(call: Call<BankResponse>, response: Response<BankResponse>) {
                dismissProgressDialog()
                if(response.isSuccessful){
                    bankresponse = response.body()!!
                    if (bankresponse.status == 200){
                        val list : List<BankDataItem>? = bankresponse.bankData
                        if (list != null) {
                            callback.onBankRecive(list)
                        }
                    }else{
                        showErrorMessagev1(bankresponse.message)
                    }

                }else{
                    showErrorMessagev1(getString(R.string.try_again_msg))
                }
            }

        })


    }

    private fun getBranch(disId: String, bankId: String?, callback: BranchCallBack) {
        showProgressDialog()
        val requestBranch = RequestBranch()
        var responseBranchData: BranchData? = null
        requestBranch.setmUsername("" + mAppHandler?.getUserName())
        requestBranch.setmBankId("" + bankId)
        requestBranch.setmDistrictId("" + disId)

        ApiUtils.getAPIServiceV2().callBranchDataAPI(requestBranch).enqueue(object : Callback<BranchData>{
            override fun onFailure(call: Call<BranchData>, t: Throwable) {
                dismissProgressDialog()
                showErrorMessagev1(getString(R.string.try_again_msg))
            }

            override fun onResponse(call: Call<BranchData>, response: Response<BranchData>) {
                dismissProgressDialog()
                if (response.code() == 200){
                    responseBranchData = response.body()
                    if (responseBranchData?.getStatus() == 200L) {
                        val list : List<Branch> = responseBranchData!!.branch
                        callback.onBranchRecive(list)

                    }else{
                        showErrorMessagev1(responseBranchData?.message)
                    }
                }else{
                    showErrorMessagev1(getString(R.string.try_again_msg))
                }
            }

        })

    }

    fun asked(title: String?, number: String) {

        val builder = AlertDialog.Builder(this)
        builder.setMessage(title)
                .setCancelable(true)
                .setPositiveButton("Camera") { dialog, id ->
                    dialog.dismiss()
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                        takePictureIntent.resolveActivity(packageManager)?.also {
                            startActivityForResult(takePictureIntent, 1)
                        }
                    }
                }
                .setNegativeButton("Gallery") { dialog, id ->
                    dialog.dismiss()
                    val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
                    } else {
                        galleryIntent()
                    }
                }
        val alert = builder.create()
        alert.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            bankAdd?.setImageBitmap(imageBitmap)
            encodeTobase64(imageBitmap)
            if (imageBitmap!=null){
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun galleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select File"), 1)
    }

    private fun getDistricts(bankId: String, callback: DistrictCallBack) {
        showProgressDialog()
        var list = ArrayList<String>()
        var requestDistrict = RequestDistrict()
        requestDistrict.mUsername = "" + mAppHandler!!.userName
        requestDistrict.mBankId = "" + bankId

        var mResponse : ReposeDistrictListerBankDeposit? = null

        ApiUtils.getAPIServiceV2().callDistrictDataAPI(requestDistrict).enqueue(object : Callback<ReposeDistrictListerBankDeposit>{
            override fun onFailure(call: Call<ReposeDistrictListerBankDeposit>, t: Throwable) {
                dismissProgressDialog()
                showErrorMessagev1(getString(R.string.try_again_msg))
            }

            override fun onResponse(call: Call<ReposeDistrictListerBankDeposit>, response: Response<ReposeDistrictListerBankDeposit>) {
             dismissProgressDialog()
                if (response.code() == 200){
                    mResponse =  response.body()!!
                    var m : ReposeDistrictListerBankDeposit = response.body()!!
                    if (m.status == 200){
                        callback.onDataRecive( m.districtData)
                    }else{
                        showErrorMessagev1(m.message)
                    }

                }else{
                    showErrorMessagev1(getString(R.string.try_again_msg))
                }
            }

        })

    }

    private val onFileChoose = OnFileChoose { fileUri, requestCode, size -> // here you will get captured or selected image<br>
        val selectedImage = BitmapFactory.decodeFile(fileUri)
        encodeTobase64(selectedImage)
        Log.e("", "")
    }

    fun encodeTobase64(image: Bitmap?) {
        val baos = ByteArrayOutputStream()
        val myBm: Bitmap = getResizedBitmap(image, 1000, 700)
        myBm.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val b = baos.toByteArray()
        val imageEncoded = Base64.encodeToString(b, Base64.DEFAULT).replace("[\n\r]".toRegex(), "")
        val strBuild = "xxCloud" + imageEncoded + "xxCloud"
        val img = resources.getDrawable(R.drawable.icon_seleted)

    }

    private fun showCurrentBankinfo() {
        if (bankAddLayout?.getChildCount()!! > 0) {
            val reqStrBuilder = StringBuilder()
            for (i in 0 until bankAddLayout!!.getChildCount()) {
                val singleTopUpView: View = bankAddLayout!!.getChildAt(i)
                if (singleTopUpView != null) {
                    val bank_sp = singleTopUpView.findViewById<Spinner>(R.id.spinner_Bank)
                    val distrrict_sp = singleTopUpView.findViewById<Spinner>(R.id.spinner_district)
                    val branch_sp = singleTopUpView.findViewById<Spinner>(R.id.spinner_branch)
                    val account_et = singleTopUpView.findViewById<EditText>(R.id.acNumber)
                    val prePostSelector = singleTopUpView.findViewById<RadioGroup>(R.id.prePostRadioGroup)

                    val accountNumberStr = account_et.text.toString()
                    val bank : BankDataItem  = bank_sp.selectedItem as BankDataItem
                    val bankName : String = bank.name.toString()
                    val district : DistrictData = distrrict_sp.selectedItem as DistrictData
                    val branch : Branch = branch_sp.selectedItem as Branch
                    val branchname : String = branch.name.toString()

                    val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(applicationContext)!!.rid)

                    val bankPojo = BankPojo()
                    bankPojo.accountNo  = accountNumberStr
                    bankPojo.bankId = bank.id
                    bankPojo.branchId = branch.id
                    bankPojo.districtId = district.id
                    bankPojo.refId = uniqueKey
                    bankPojo.checkBookSlip = "as"
                    bankPojo.password = "12345"
                    bankPojo.username = mAppHandler?.userName

                    uploadBankInfo(bankPojo   )

                    reqStrBuilder.append("""${i + 1}. ${"Bank"} $bankName
                        ${"branch"} $branchname${getString(R.string.tk)}
                        ${"AC"} : ${accountNumberStr}""")

                }
            }
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("Bank Info Update")
            builder.setMessage(reqStrBuilder)
            builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
                dialogInterface.dismiss()

                var pojo = BankListRequestPojo()
                val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(applicationContext).rid)
                pojo.refId = uniqueKey
                pojo.username = mAppHandler?.userName
                getBankInfoList(pojo)

                // askForPin()
            }
            val alert = builder.create()
            alert.show()


        } else {
           showErrorMessagev1("try again")
        }
    }

    private fun uploadBankInfo(bankPojo: BankPojo) {
        showProgressDialog()
        ApiUtils.getAPIServiceV2().uploadBankInfo(bankPojo).enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    dismissProgressDialog()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                dismissProgressDialog()
              if (  response.isSuccessful){






              }else{
                  showErrorMessagev1(getString(R.string.try_again_msg))
              }
            }

        })

    }

    private fun getBankInfoList(pojo : BankListRequestPojo){
        showProgressDialog()

        ApiUtils.getAPIServiceV2().getRetailerBankList(pojo).enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
               dismissProgressDialog()
            }

        })

    }

    private fun removeBnkInfo(pojo : RemoveReqPojo){
        showProgressDialog()

        ApiUtils.getAPIServiceV2().removeBankInfo(pojo).enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                dismissProgressDialog()
            }

        })
    }

    private fun askForPin() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle(R.string.pin_no_title_msg)
        val pinNoET = EditText(this)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        pinNoET.gravity = Gravity.CENTER_HORIZONTAL
        pinNoET.layoutParams = lp
        pinNoET.inputType = InputType.TYPE_CLASS_NUMBER or
                InputType.TYPE_TEXT_VARIATION_PASSWORD
        pinNoET.transformationMethod = PasswordTransformationMethod.getInstance()
        builder.setView(pinNoET)
        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
            val inMethMan = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inMethMan.hideSoftInputFromWindow(pinNoET.windowToken, 0)
            if (pinNoET.text.toString().length != 0) {
                dialogInterface.dismiss()
                val pin = pinNoET.text.toString()
                if (isInternetConnection) {

                    //uploadBankInfo()

                } else {
                    showErrorMessagev1(getString(R.string.connection_error_msg))
                }
            } else {
                showErrorMessagev1(getString(R.string.pin_no_error_msg))
//                snackbar.show()
            }
        }
        val alert = builder.create()
        alert.show()
    }


    private interface DistrictCallBack{
        fun onDataRecive(districts: List<DistrictData>)
    }

    private interface BranchCallBack{
        fun onBranchRecive(branches: List<Branch>)
    }

    private interface BankCallBack{
        fun onBankRecive(banks: List<BankDataItem>)
    }


    private interface CameraCallBack{
        fun onBranchRecive(image: Bitmap)
    }
}




