package com.cloudwell.paywell.services.activity.reg.nidOCR

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.cloudwell.paywell.consumer.ui.nidRegistion.model.User
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.LanguagesBaseActivity
import com.cloudwell.paywell.services.activity.reg.EntryThirdActivity
import com.cloudwell.paywell.services.activity.reg.nidOCR.view.IInputNidListener
import com.cloudwell.paywell.services.activity.reg.nidOCR.viewModel.InputNidModelFactory
import com.cloudwell.paywell.services.activity.reg.nidOCR.viewModel.NidInputViewModel
import com.cloudwell.paywell.services.databinding.ActivityNidInfoBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.content_nid_info.*
import kotlinx.android.synthetic.main.content_nid_input.*
import org.jetbrains.anko.toast


class InputNidInfoActivity: LanguagesBaseActivity(), IInputNidListener {


    override fun openNextActivity(user: User) {


    }


    private var isNID: Boolean = false
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


        val string = intent.extras.getString("data")
        isNID = intent.extras.getBoolean("isNID", false)

        val user = Gson().fromJson(string, User::class.java);
        etNid.setText(user.nid)
        etUserName.setText(user.name)
        etUserUserFatherName.setText(user.fatherName)
        etUserUserMotherName.setText(user.motherName)
        etUserUserBirthday.setText(user.birthday)
        etUserAddress.setText(user.address)


        btFinished.setOnClickListener {


            val intent = Intent(applicationContext, EntryThirdActivity::class.java);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

            finish()

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

    override fun noInternetConnectionFound() {
        toast("No internet connection found!!!")
    }
    override fun setDefaultNIDImagInFirstNIDView() {

    }

    override fun setDefaultNIDImagInSecondNIDView() {

    }
}
