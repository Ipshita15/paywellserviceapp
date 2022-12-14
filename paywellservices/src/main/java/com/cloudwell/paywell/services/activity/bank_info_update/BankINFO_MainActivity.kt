package com.cloudwell.paywell.services.activity.bank_info_update

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.bank_info_update.model.BankDialogPojo
import com.cloudwell.paywell.services.activity.bank_info_update.model.BankPojo
import com.cloudwell.paywell.services.activity.bank_info_update.model.ResposeAddLinkBankAccount
import com.cloudwell.paywell.services.activity.bank_info_update.spineer.*
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.refill.banktransfer.model.Branch
import com.cloudwell.paywell.services.activity.refill.banktransfer.model.DistrictData
import com.cloudwell.paywell.services.activity.refill.banktransfer.model.ReposeDistrictListerBankDeposit
import com.cloudwell.paywell.services.activity.refill.model.BranchData
import com.cloudwell.paywell.services.activity.refill.model.RequestBranch
import com.cloudwell.paywell.services.activity.refill.model.RequestDistrict
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.AskPinDialog
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.SuccessDialog
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.successInterface
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.cloudwell.paywell.services.utils.ImageUtility.getResizedBitmap
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_bank_info_main.*
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
    var bankAdd: ImageView? = null
    var bankcheck: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_info_main)
        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(R.string.bank_btn)
        }

        bankAddLayout = findViewById(R.id.bankAddLayout)
        inflater = layoutInflater
        mAppHandler = AppHandler.getmInstance(applicationContext)
        slideInAnim.duration = 50
        slideOutAnim.duration = 50
        btnSubmit.setOnClickListener(View.OnClickListener {

            showCurrentBankinfo()

        })
//        add_btn.setOnClickListener(View.OnClickListener {
//            addAnotherBank()
//        })

        addAnotherBank()


    }


    private fun takeCameraImage() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                                takePictureIntent.resolveActivity(packageManager)?.also {
                                    startActivityForResult(takePictureIntent, addNoFlag - 1)
                                }
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    private fun addAnotherBank() {
        ++addNoFlag
        Log.e("Flag", addNoFlag.toString())
        var bankId: String? = null
        var district_array: List<DistrictData>? = null
        var branch_array: List<Branch>? = null
        var bank_array: List<BankDataItem>? = null
        val topUpView: View = inflater!!.inflate(R.layout.activity_bank_info_layout, null)
        val bank_spinner: Spinner = topUpView.findViewById(R.id.spinner_Bank)
        val district_spinner: Spinner = topUpView.findViewById(R.id.spinner_district)
        val branch_spinner: Spinner = topUpView.findViewById(R.id.spinner_branch)
        val removeBtn: Button = topUpView.findViewById(R.id.removeBankBtn)
        val check_book_btn: Button = topUpView.findViewById(R.id.check_book_btn)
        val add_view_btn: TextView = topUpView.findViewById(R.id.add_another_btn)
        val bankCounter: TextView = topUpView.findViewById(R.id.counter)
        bankcheck = topUpView.findViewById(R.id.check_check)
        bankCounter.text = addNoFlag.toString()
        bankAdd = topUpView.findViewById(R.id.iamge)
        add_view_btn.setOnClickListener(View.OnClickListener {
            addAnotherBank()
        })
        check_book_btn.setOnClickListener(View.OnClickListener {
            takeCameraImage()
        })
        if (addNoFlag == 1) {
            removeBtn.visibility = View.GONE
        }
        if (isInternetConnection) {
            getBank(object : BankCallBack {
                override fun onBankRecive(banks: List<BankDataItem>) {
                    bank_array = banks
                    bank_spinner.adapter = CustomBankAdapter(applicationContext, bank_array)

                }

            })
        } else {
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

        bank_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val bank_name: String = bank_array?.get(position)?.name.toString()
                bankId = bank_array?.get(position)?.id.toString()
                Log.e(bank_name, bankId)
                if (isInternetConnection) {
                    getDistricts(bankId!!, object : DistrictCallBack {
                        override fun onDataRecive(districts: List<DistrictData>) {
                            district_array = districts
                            district_spinner.adapter = CustomDistrictAdapter(applicationContext, district_array)
                        }
                    })
                } else {
                    showErrorMessagev1(getString(R.string.internet_connection_error_msg))
                }

            }

        }


        district_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val dis_name: String = district_array?.get(position)?.district_name.toString()
                val dis_Id: String = district_array?.get(position)?.id.toString()
                Log.e(dis_name, dis_Id)

                if (isInternetConnection) {

                } else {
                    showErrorMessagev1(getString(R.string.connection_error_msg))

                }
                getBranch(dis_Id, bankId, object : BranchCallBack {
                    override fun onBranchRecive(branches: List<Branch>) {
                        branch_array = branches
                        branch_spinner.adapter = CustomBranchAdapter(applicationContext, branch_array)
                    }
                })
            }

        }

        branch_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val branchName: String = branch_array?.get(position)?.name.toString()
                val branchId: String = branch_array?.get(position)?.id.toString()
                Log.e(branchName, branchId)
            }

        }


        bankAddLayout?.addView(topUpView)
    }

    private fun getBank(callback: BankCallBack) {
        showProgressDialog()

        val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(applicationContext).rid)
        val pojo = GetBankPojo()
        pojo.username = mAppHandler?.userName
        pojo.ref_id = uniqueKey

        var bankresponse = BankResponse()

        ApiUtils.getAPIServiceV2().callBankDataAPI(pojo).enqueue(object : Callback<BankResponse> {
            override fun onFailure(call: Call<BankResponse>, t: Throwable) {
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<BankResponse>, response: Response<BankResponse>) {
                dismissProgressDialog()
                if (response.isSuccessful) {
                    bankresponse = response.body()!!
                    if (bankresponse.status == 200) {
                        val list: List<BankDataItem>? = bankresponse.bankData
                        if (list != null) {
                            callback.onBankRecive(list)
                        }
                    } else {
                        showErrorMessagev1(bankresponse.message)
                    }

                } else {
                    showErrorMessagev1(getString(R.string.try_again_msg))
                }
            }

        })


    }

    private fun getBranch(disId: String, bankId: String?, callback: BranchCallBack) {
        showProgressDialog()
        val requestBranch = RequestBranch()
        var responseBranchData: BranchData? = null
        requestBranch.setmUsername("" + mAppHandler?.userName)
        requestBranch.setmBankId("" + bankId)
        requestBranch.setmDistrictId("" + disId)

        ApiUtils.getAPIServiceV2().callBranchDataAPI(requestBranch).enqueue(object : Callback<BranchData> {
            override fun onFailure(call: Call<BranchData>, t: Throwable) {
                dismissProgressDialog()
                showErrorMessagev1(getString(R.string.try_again_msg))
            }

            override fun onResponse(call: Call<BranchData>, response: Response<BranchData>) {
                dismissProgressDialog()
                if (response.code() == 200) {
                    responseBranchData = response.body()
                    if (responseBranchData?.status == 200L) {
                        val list: List<Branch> = responseBranchData!!.branch
                        callback.onBranchRecive(list)

                    } else {
                        showErrorMessagev1(responseBranchData?.message)
                    }
                } else {
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
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                    } else {
                        galleryIntent()
                    }
                }
        val alert = builder.create()
        alert.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            bankcheck?.visibility = View.VISIBLE
            val stringImage: String = encodeTobase64(imageBitmap)
            Log.e("imageString", "" + stringImage)
            Log.e("requestCode", "" + requestCode)

            val singleTopUpView: View = bankAddLayout!!.getChildAt(requestCode)
            val image: ImageView = singleTopUpView.findViewById(R.id.check_book)
            image.setImageBitmap(imageBitmap)

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

        var mResponse: ReposeDistrictListerBankDeposit? = null

        ApiUtils.getAPIServiceV2().callDistrictDataAPI(requestDistrict).enqueue(object : Callback<ReposeDistrictListerBankDeposit> {
            override fun onFailure(call: Call<ReposeDistrictListerBankDeposit>, t: Throwable) {
                dismissProgressDialog()
                showErrorMessagev1(getString(R.string.try_again_msg))
            }

            override fun onResponse(call: Call<ReposeDistrictListerBankDeposit>, response: Response<ReposeDistrictListerBankDeposit>) {
                dismissProgressDialog()
                if (response.code() == 200) {
                    mResponse = response.body()!!
                    var m: ReposeDistrictListerBankDeposit = response.body()!!
                    if (m.status == 200) {
                        callback.onDataRecive(m.districtData)
                    } else {
                        showErrorMessagev1(m.message)
                    }

                } else {
                    showErrorMessagev1(getString(R.string.try_again_msg))
                }
            }

        })

    }

    fun encodeTobase64(image: Bitmap?): String {
        val baos = ByteArrayOutputStream()
        val myBm: Bitmap = getResizedBitmap(image, 1000, 700)
        myBm.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val b = baos.toByteArray()
        val imageEncoded = Base64.encodeToString(b, Base64.DEFAULT).replace("[\n\r]".toRegex(), "")
        val strBuild = "xxCloud" + imageEncoded + "xxCloud"
        val img = resources.getDrawable(R.drawable.icon_seleted)

        return strBuild
    }

    private fun showCurrentBankinfo() {
        var bankList: ArrayList<BankPojo> = ArrayList()
        var dialogList: ArrayList<BankDialogPojo> = ArrayList()
        if (bankAddLayout?.childCount!! == 1) {
            val singleTopUpView: View = bankAddLayout!!.getChildAt(0)
            if (singleTopUpView != null) {
                val bank_sp = singleTopUpView.findViewById<Spinner>(R.id.spinner_Bank)
                val distrrict_sp = singleTopUpView.findViewById<Spinner>(R.id.spinner_district)
                val branch_sp = singleTopUpView.findViewById<Spinner>(R.id.spinner_branch)
                val account_name = singleTopUpView.findViewById<EditText>(R.id.acName)
                val account_et = singleTopUpView.findViewById<EditText>(R.id.acNumber)
                val prePostSelector = singleTopUpView.findViewById<RadioGroup>(R.id.prePostRadioGroup)
                val accountNameStr = account_name.text.toString()
                val accountNumberStr = account_et.text.toString()
                val bank: BankDataItem = bank_sp.selectedItem as BankDataItem
                val bankName: String = bank.name.toString()
                val district: DistrictData = distrrict_sp.selectedItem as DistrictData
                val districtName: String = district.district_name
                val branch: Branch = branch_sp.selectedItem as Branch
                val branchname: String = branch.name.toString()
                val checkView: ImageView = singleTopUpView.findViewById(R.id.check_book)
                var imageBitmap: Bitmap? = null
                if (checkView.drawable != null) {
                    imageBitmap = (checkView.drawable as BitmapDrawable).bitmap
                }

                val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(applicationContext)!!.rid)
                val stringpojo = BankDialogPojo()
                stringpojo.accountName = accountNameStr
                stringpojo.account = accountNumberStr
                stringpojo.bank = bankName
                stringpojo.branch = branchname
                stringpojo.district = districtName
                dialogList.add(stringpojo)

                if (accountNumberStr.length < 8) {
                    showDialogMessage(getString(R.string.input_error_msg_account_number))
                } else if (accountNameStr.equals("")) {
                    showDialogMessage(getString(R.string.input_error_msg_account_name))
                } else {
                    askPinNumber(dialogList, accountNumberStr, accountNameStr, bank, branch, district, uniqueKey, imageBitmap)
                }


            }
        } else if (bankAddLayout?.childCount!! > 1) {        // Multiple Bank Info


            val reqStrBuilder = StringBuilder()
            var bankList: ArrayList<BankPojo> = ArrayList()
            var dialogList: ArrayList<BankDialogPojo> = ArrayList()
            for (i in 0 until bankAddLayout!!.childCount) {
                val singleTopUpView: View = bankAddLayout!!.getChildAt(i)
                if (singleTopUpView != null) {
                    val bank_sp = singleTopUpView.findViewById<Spinner>(R.id.spinner_Bank)
                    val distrrict_sp = singleTopUpView.findViewById<Spinner>(R.id.spinner_district)
                    val branch_sp = singleTopUpView.findViewById<Spinner>(R.id.spinner_branch)
                    val account_et = singleTopUpView.findViewById<EditText>(R.id.acNumber)
                    val prePostSelector = singleTopUpView.findViewById<RadioGroup>(R.id.prePostRadioGroup)

                    val accountNumberStr = account_et.text.toString()
                    val bank: BankDataItem = bank_sp.selectedItem as BankDataItem
                    val bankName: String = bank.name.toString()
                    val district: DistrictData = distrrict_sp.selectedItem as DistrictData
                    val districtName: String = district.district_name
                    val branch: Branch = branch_sp.selectedItem as Branch
                    val branchname: String = branch.name.toString()

                    val uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(applicationContext)!!.rid)

                    val bankPojo = BankPojo()
                    bankPojo.accountNo = accountNumberStr
                    bankPojo.bankId = bank.id
                    bankPojo.branchId = branch.id
                    bankPojo.districtId = district.id
                    bankPojo.refId = uniqueKey
                    bankPojo.checkBookSlip = "as"
                    bankPojo.password = "12345"
                    bankPojo.username = mAppHandler?.userName
                    bankList.add(bankPojo)

                    //uploadBankInfo(bankPojo)//for single

                    val stringpojo = BankDialogPojo()
                    stringpojo.account = accountNumberStr
                    stringpojo.bank = bankName
                    stringpojo.branch = branchname
                    stringpojo.district = districtName
                    dialogList.add(stringpojo)

                }
            }

            val d = BankInfoDialog(applicationContext, dialogList, object : OKInterface {
                override fun onclick() {
                    //askForPin()
                    val pinDialog = AskPinDialog(object : AskPinDialog.getPinInterface {
                        override fun onclick(pinNumber: String) {

                        }

                    }, object : AskPinDialog.GetFinisedInterface {
                        override fun onclick() {

                        }

                    })
                    pinDialog.show(supportFragmentManager, "PIN")
                }
            })
            d.show(supportFragmentManager, "BANKINFO")

        } else {
            showErrorMessagev1("try again")
        }
    }

    private fun askPinNumber(dialogList: ArrayList<BankDialogPojo>, accountNumberStr: String, accountNameStr: String, bank: BankDataItem, branch: Branch, district: DistrictData, uniqueKey: String?, imageBitmap: Bitmap?) {
        val d = BankInfoDialog(applicationContext, dialogList, object : OKInterface {
            override fun onclick() {
                //askForPin()
                val pinDialog = AskPinDialog(object : AskPinDialog.getPinInterface {
                    override fun onclick(pinNumber: String) {
                        val bankPojo = BankPojo()
                        bankPojo.accountNo = accountNumberStr
                        bankPojo.accountName = accountNameStr

                        bankPojo.bankId = bank.id
                        bankPojo.branchId = branch.id
                        bankPojo.districtId = district.id
                        bankPojo.refId = uniqueKey
                        bankPojo.checkBookSlip = encodeTobase64(imageBitmap)
                        bankPojo.password = pinNumber
                        bankPojo.username = mAppHandler?.userName
                        uploadBankInfo(bankPojo)//for single

                    }

                }, object : AskPinDialog.GetFinisedInterface {
                    override fun onclick() {


                    }

                })
                pinDialog.show(supportFragmentManager, "PIN")
            }
        })
        d.show(supportFragmentManager, "BANKINFO")
    }

    private fun uploadBankInfo(bankPojo: BankPojo) {
        showProgressDialog()
        ApiUtils.getAPIServiceV2().uploadBankInfo(bankPojo).enqueue(object : Callback<ResposeAddLinkBankAccount> {
            override fun onFailure(call: Call<ResposeAddLinkBankAccount>, t: Throwable) {
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<ResposeAddLinkBankAccount>, response: Response<ResposeAddLinkBankAccount>) {
                dismissProgressDialog()
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        val r = response.body()
                        r.let {
                            if (it?.status ?: 0 == 200L) {
                                val successdialog: SuccessDialog = SuccessDialog(getString(R.string.bank_btn), getString(R.string.bank_added), object : successInterface {
                                    override fun onclick() {
                                        finish()
                                    }
                                })
                                successdialog.show(supportFragmentManager, "BAnkAddedsuccess")
                            } else {
                                showErrorMessagev1(it?.message)
                            }
                        }


                    } else {
                        showErrorMessagev1(getString(R.string.try_again_msg))
                    }

                } else {
                    showErrorMessagev1(getString(R.string.try_again_msg))
                }
            }

        })

    }


    private interface DistrictCallBack {
        fun onDataRecive(districts: List<DistrictData>)
    }

    private interface BranchCallBack {
        fun onBranchRecive(branches: List<Branch>)
    }

    private interface BankCallBack {
        fun onBankRecive(banks: List<BankDataItem>)
    }


    private interface CameraCallBack {
        fun onImageRecive(image: Bitmap)
    }
}




