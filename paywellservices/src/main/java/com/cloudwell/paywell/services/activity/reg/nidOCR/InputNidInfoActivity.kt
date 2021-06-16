package com.cloudwell.paywell.services.activity.reg.nidOCR

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.cloudwell.paywell.consumer.ui.nidRegistion.model.User
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.LanguagesBaseActivity
import com.cloudwell.paywell.services.activity.reg.EntryMainActivity.regModel
import com.cloudwell.paywell.services.activity.reg.EntryThirdActivity
import com.cloudwell.paywell.services.activity.reg.missing.MissingMainActivity
import com.cloudwell.paywell.services.activity.reg.nidOCR.view.IInputNidListener
import com.cloudwell.paywell.services.activity.reg.nidOCR.viewModel.InputNidModelFactory
import com.cloudwell.paywell.services.activity.reg.nidOCR.viewModel.NidInputViewModel
import com.cloudwell.paywell.services.databinding.ActivityNidInfoBinding
import com.cloudwell.paywell.services.utils.ImageUtility
import com.google.gson.Gson
import kotlinx.android.synthetic.main.content_nid_info.*
import kotlinx.android.synthetic.main.content_nid_input.*
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream


class InputNidInfoActivity: LanguagesBaseActivity(), IInputNidListener {


    override fun openNextActivity(user: User) {


    }


    private var isNID: Boolean = false
    private var isMissingPage: Boolean = false
    private val authViewModelFactory: InputNidModelFactory? = null


    lateinit var  inputViewModel: NidInputViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityNidInfoBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_nid_info
        )

        setToolbar("Test");

        inputViewModel = ViewModelProviders.of(this, authViewModelFactory).get(NidInputViewModel::class.java)
        binding.viewmode = inputViewModel
        inputViewModel.iView = this


        val string = intent.extras?.getString("data")
        isNID = intent.extras?.getBoolean("isNID", false) ?: false
        isMissingPage = intent.extras?.getBoolean("isMissingPage", false) ?: false

        val user = Gson().fromJson(string, User::class.java);
        etNid.setText(user.nid)
        etUserName.setText(user.name)
        etUserUserFatherName.setText(user.fatherName)
        etUserUserMotherName.setText(user.motherName)
        etUserUserBirthday.setText(user.birthday)
        etUserAddress.setText(user.address)

        if (isNID) {
            textInputLayoutUserNameEnglish.visibility = View.VISIBLE
            etUserNameEnglish.visibility = View.VISIBLE
            etUserNameEnglish.setText(user.nameEnglish)
        }else{
            textInputLayoutUserNameEnglish.visibility = View.GONE
            etUserNameEnglish.visibility = View.GONE
        }


        btFinished.setOnClickListener {


            if (isNID){
                regModel.nidNumber = ""+etNid.text.toString().trim()
                regModel.nidName = ""+etUserName.text
                regModel.nidFatherName = ""+etUserUserFatherName.text
                regModel.nidMotherName = ""+etUserUserMotherName.text
                regModel.nidBirthday = ""+etUserUserBirthday.text
                regModel.nidAddress = ""+etUserAddress.text
                regModel.nidNameEngish = ""+etUserNameEnglish.text
            }else{
                regModel.smartCardNumber = ""+etNid.text.toString().trim()
                regModel.smartCardName = ""+etUserName.text
                regModel.smartCardFatherName = ""+etUserUserFatherName.text
                regModel.smartCardMotherName = ""+etUserUserMotherName.text
                regModel.smartCardBirthday = ""+etUserUserBirthday.text
                regModel.smartCardAddress = ""+etUserAddress.text

            }


            if (isMissingPage){

                val intent = Intent(applicationContext, MissingMainActivity::class.java);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.putExtra("isMissingFlow", true)
                startActivity(intent)

                finish()
            }else{
                val intent = Intent(applicationContext, EntryThirdActivity::class.java);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

                finish()
            }
        }
    }
    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hiddenProgress() {
        progressBar.visibility = View.GONE
    }


    override fun onFailure(message: String) {
        toast(message)
    }

    override fun openPreviousActivity() {

    }

    override fun noInternetConnectionFound() {
        toast("No internet connection found!!!")
    }

    override fun setDefaultNIDImagInFirstNIDView() {

    }

    override fun setDefaultNIDImagInSecondNIDView() {

    }

    fun encodeTobase64(image: Bitmap?): String {
        val baos = ByteArrayOutputStream()
        val myBm: Bitmap = ImageUtility.getResizedBitmap(image, 1000, 700)
        myBm.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val b = baos.toByteArray()
        val imageEncoded = Base64.encodeToString(b, Base64.DEFAULT).replace("[\n\r]".toRegex(), "")
        val strBuild = "xxCloud" + imageEncoded + "xxCloud"
        return strBuild
    }
}
