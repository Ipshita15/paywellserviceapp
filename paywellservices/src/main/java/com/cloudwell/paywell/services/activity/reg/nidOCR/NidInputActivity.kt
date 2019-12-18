package com.cloudwell.paywell.services.activity.reg.nidOCR

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.cloudwell.paywell.consumer.ui.nidRegistion.model.User
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.reg.nidOCR.view.IInputNidListener
import com.cloudwell.paywell.services.activity.reg.nidOCR.viewModel.InputNidModelFactory
import com.cloudwell.paywell.services.activity.reg.nidOCR.viewModel.NidInputViewModel
import com.cloudwell.paywell.services.databinding.ActivityOldNidPageBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.content_nid_input.*
import org.jetbrains.anko.toast


class NidInputActivity: BaseActivity(), IInputNidListener {

    private var isNewNID = false;
    private val authViewModelFactory: InputNidModelFactory? = null


    lateinit var  inputViewModel: NidInputViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityOldNidPageBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_old_nid_page
        )

        inputViewModel = ViewModelProviders.of(this, authViewModelFactory).get(NidInputViewModel::class.java)
        binding.viewmode = inputViewModel
        inputViewModel.iView = this

        setToolbar("Test")

        isNewNID =  intent.getBooleanExtra("date", false);

        ivNidFirst.setOnClickListener {
            inputViewModel.isFirstPage = true
            openCropActivity()

        }

        ivNidSecound.setOnClickListener {
            inputViewModel.isFirstPage = false
            openCropActivity()
        }

        btNext.setOnClickListener {
            inputViewModel.onNextClick(applicationContext, isNewNID)
        }

    }

    private fun openCropActivity() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                    if (report.areAllPermissionsGranted()) {
                        CropImage.activity(inputViewModel.photoURI1)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(this@NidInputActivity)
                    } else {

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                }
            }).check()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                val resultUri = result.uri
                if ( inputViewModel.isFirstPage){

                    inputViewModel.firstPageUri = resultUri
                    ivNidFirst.setImageURI(resultUri)
                }else{
                    inputViewModel.secoundPageUri = resultUri
                    ivNidSecound.setImageURI(resultUri)
                }



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
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

    override fun noInternetConnectionFound() {
        toast("No internet connection found!!!")
    }

    override fun openNextActivity(user: User) {
//        val intent = Intent(applicationContext, InputNidInfoActivity::class.java)
//        intent.putExtra("data", Gson().toJson(user))
//        startActivity(intent)

    }

    override fun setDefaultNIDImagInFirstNIDView() {
        ivNidFirst.setImageResource(R.drawable.nid)
    }

    override fun setDefaultNIDImagInSecondNIDView() {
        ivNidSecound.setImageResource(R.drawable.nid)

    }
}
