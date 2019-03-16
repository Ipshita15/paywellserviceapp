package com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.Nullable
import android.support.v7.app.AlertDialog
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.WindowManager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.fragment.GenderBottomSheetDialog
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.fragment.NameTitleSheetDialog
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.fragment.PassengerTypeSheetDialog
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.model.MyCountry
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.view.PassgerAddViewStatus
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.viewmodel.AddPassengerViewModel
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.libaray.imagePickerAndCrop.ImagePickerActivity
import com.cloudwell.paywell.services.utils.AssetHelper
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mukesh.countrypicker.Country
import com.mukesh.countrypicker.CountryPicker
import com.mukesh.countrypicker.listeners.OnCountryPickerListener
import kotlinx.android.synthetic.main.contant_add_passenger.*
import java.io.IOException


class AddPassengerActivity : AirTricketBaseActivity() {


    val REQUEST_IMAGE = 100

    private lateinit var viewMode: AddPassengerViewModel
    lateinit var touchHelper: ItemTouchHelper


    // ui
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    var isEmailValid = false
    var countryCode = ""

    var isEditFlag = false


    private lateinit var oldPassenger: Passenger
    private var passportImagePath = ""
    var passportMadatory = false
    var isFistTime = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_add_passenger)

        setToolbar(getString(com.cloudwell.paywell.services.R.string.title_add_passenger))

        initializationView()


        initViewModel()

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)


    }

    private fun initViewModel() {
        viewMode = ViewModelProviders.of(this).get(AddPassengerViewModel::class.java)

        viewMode.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewCommonStatus(it)
        })


        viewMode.mViewStatus.observe(this, Observer {
            it?.let { it1 -> handleViewStatus(it1) }
        })


    }

    private fun handleViewStatus(it: PassgerAddViewStatus) {

        if (it.isPassengerAddSuccessful) {

            finish()
        }

    }


    private fun initializationView() {

        try {
            isEditFlag = intent.extras.getBoolean("isEditFlag", false)

            if (isEditFlag) {
                oldPassenger = AppStorageBox.get(applicationContext, AppStorageBox.Key.AIRTRICKET_EDIT_PASSENGER) as Passenger
                etPassengerType.setText(oldPassenger.paxType)
                etTitle.setText(oldPassenger.title)
                etFirstName.setText(oldPassenger.firstName)
                etLastName.setText(oldPassenger.lastName)
                etGender.setText(oldPassenger.gender)
                etCountry.setText(oldPassenger.country)
                etContactNumber.setText(oldPassenger.contactNumber)
                etEmail.setText(oldPassenger.email)
                etNidorPassportNumber.setText(oldPassenger.passportNumber)

                if (!oldPassenger.passportImagePath.equals("")) {
                    passportImagePath = oldPassenger.passportImagePath
                    ivPassportPageUpload.setImageResource(R.drawable.ic_passport_unseleted)
                } else {
                    ivPassportPageUpload.visibility = View.GONE
                    textInputLayoutPassport.visibility = View.GONE
                }

                if (!oldPassenger.nIDnumber.equals("")) {
                    etNationalIDNumber.setText(oldPassenger.nIDnumber)
                } else {
                    textInputLayoutNId.visibility = View.GONE
                }

            }
        } catch (e: Exception) {
            //todo
        }


//        try {
//            val resposeAirPriceSearch = AppStorageBox.get(applicationContext, AppStorageBox.Key.ResposeAirPriceSearch) as ResposeAirPriceSearch
//            passportMadatory = resposeAirPriceSearch.data?.results?.get(0)?.passportMadatory!!
//            if (passportMadatory!!) {
//                textInputLayoutPassport.visibility = View.VISIBLE
//                ivPassportPageUpload.visibility = View.VISIBLE
//
//                textInputLayoutNId.visibility = View.GONE
//
//
//            } else {
//                textInputLayoutPassport.visibility = View.GONE
//                ivPassportPageUpload.visibility = View.GONE
//
//                textInputLayoutNId.visibility = View.VISIBLE
//            }
//        } catch (e: java.lang.Exception) {
//
//        }

        etPassengerType.setOnClickListener {

            handlePassengerType()
        }

        etPassengerType.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                handlePassengerType()
            }
        })


        etCountry.setOnClickListener {
            handleCountry()
        }
        etCountry.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                handleCountry()
            }
        })


        etGender.setOnClickListener {
            handleGender()
        }
        etGender.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                handleGender()

            }
        })

        etTitle.setOnClickListener {
            handleTitle()
        }

        etTitle.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                handleTitle()

            }
        })


        val email = etEmail.text.toString().trim()


        etEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

                if (s.matches(emailPattern.toRegex()) && s.length > 0) {
                    isEmailValid = true
                    textInputLayoutEmail.error = ""

                } else {
                    isEmailValid = false
                    textInputLayoutEmail.error = "invalid email"
                }

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // other stuffs
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // other stuffs
            }
        })


        ivPassportPageUpload.setOnClickListener {
            showImagePickerOptions()
        }


    }

    private fun handleTitle() {
        hideUserKeyboard()

        val b = Bundle()
        b.putString("title", etTitle.text.toString())

        val bottomSheet = NameTitleSheetDialog()
        bottomSheet.setOnClassListener(object : NameTitleSheetDialog.ClassBottomSheetListener {
            override fun onButtonClickListener(text: String) {

                etTitle.setText(text)
            }

        })

        bottomSheet.arguments = b
        bottomSheet.show(supportFragmentManager, "titleBottomSheet")
    }

    private fun handleGender() {
        hideUserKeyboard()

        val b = Bundle()
        b.putString("myGenderName", etGender.text.toString())

        val bottomSheet = GenderBottomSheetDialog()
        bottomSheet.setOnClassListener(object : GenderBottomSheetDialog.ClassBottomSheetListener {
            override fun onButtonClickListener(text: String) {

                etGender.setText(text)
            }

        })

        bottomSheet.arguments = b
        bottomSheet.show(supportFragmentManager, "genderBottomSheet")
    }

    private fun handleCountry() {
        hideUserKeyboard()
        val builder = CountryPicker.Builder().with(applicationContext)
                .listener(object : OnCountryPickerListener {
                    override fun onSelectCountry(country: Country) {
                        etCountry.setText("" + country.name)
                        countryCode = country.code
                    }

                })
        val picker = builder.build();
        picker.showDialog(this)
    }

    private fun handlePassengerType() {
        hideUserKeyboard()

        if (!isFistTime) {

            val b = Bundle()
            b.putString("passengerType", etGender.text.toString())

            val bottomSheet = PassengerTypeSheetDialog()
            bottomSheet.setOnClassListener(object : PassengerTypeSheetDialog.ClassBottomSheetListener {
                override fun onButtonClickListener(text: String) {

                    etPassengerType.setText(text)
                }

            })

            bottomSheet.arguments = b
            bottomSheet.show(supportFragmentManager, "genderBottomSheet")
        } else {
            isFistTime = false
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.menu_add_passenger, menu)

        if (isEditFlag == true) {
            menu?.findItem(com.cloudwell.paywell.services.R.id.add_passenger)?.setTitle("Edit")
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            com.cloudwell.paywell.services.R.id.add_passenger -> {

                addPassenger();

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun addPassenger() {
        val passengerType = this.etPassengerType.text.toString().trim()
        val title = this.etTitle.text.toString().trim()
        val firstName = this.etFirstName.text.toString().trim()
        val lastName = this.etLastName.text.toString().trim()
        val country = this.etCountry.text.toString().trim()
        val gender = this.etGender.text.toString().trim()
        val contactNumber = this.etContactNumber.text.toString().trim()
        val emailAddress = this.etEmail.text.toString().trim()
        val passportNumber = this.etNidorPassportNumber.text.toString().trim()
        val nationalIDNumber = this.etNationalIDNumber.text.toString().trim()


        if (passengerType.equals("")) {
            textInputLayoutPassengerType.error = "Invalid type"
            return
        } else {
            textInputLayoutPassengerType.error = ""
        }

        if (title.equals("")) {
            textInputLayoutTitle.error = "Invalid Title"
            return
        } else {
            textInputLayoutTitle.error = ""
        }

        if (firstName.equals("")) {
            textInputLayoutFirstName.error = "Invalid First Name"
            return
        } else {
            textInputLayoutFirstName.error = ""
        }

        if (lastName.equals("")) {
            textInputLayoutLastName.error = "Invalid Last Name"
            return
        } else {
            textInputLayoutLastName.error = ""
        }

        if (country.equals("")) {
            textInputLayoutCountry.error = "Invalid Country Name"
            return
        } else {
            textInputLayoutCountry.error = ""
        }

        if (gender.equals("")) {
            textInputLayoutGender.error = "Invalid Gender"
            return
        } else {
            textInputLayoutGender.error = ""
        }

        if (contactNumber.equals("")) {
            textInputLayoutContactNumber.error = "Invalid Country Name"
            return
        } else {
            textInputLayoutContactNumber.error = ""
        }


        if (emailAddress.matches(emailPattern.toRegex()) && emailAddress.length > 0) {
            isEmailValid = true
            textInputLayoutEmail.error = ""

        } else {
            isEmailValid = false
            textInputLayoutEmail.error = "invalid email"
            return
        }


        var nationality = "";
        val countriesString = AssetHelper().loadJSONFromAsset(applicationContext, "countries.json")
        val countries = Gson().fromJson(countriesString, Array<MyCountry>::class.java)
        countries.forEach {
            if (it.en_short_name.equals(country)) {
                nationality = it.nationality
            }
        }



        if (passportMadatory) {
            if (passportImagePath.equals("")) {
                showPassportAddDilog()
                return
            }
        }


        val passenger = Passenger(false)
        passenger.paxType = passengerType
        passenger.title = title
        passenger.firstName = firstName
        passenger.lastName = lastName
        passenger.gender = gender
        passenger.countryCode = countryCode
        passenger.nationality = nationality
        passenger.country = country
        passenger.contactNumber = contactNumber
        passenger.email = emailAddress
        passenger.passportNumber = passportNumber
        passenger.isPassengerSleted = true
        passenger.passportImagePath = passportImagePath
        passenger.nIDnumber = nationalIDNumber




        if (isEditFlag) {
            passenger.id = oldPassenger.id
            viewMode.updatePassenger(passenger)
        } else {
            viewMode.addPassenger(passenger)
        }

    }

    private fun showPassportAddDilog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.please_add_id_info_for_this_travaler))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), DialogInterface.OnClickListener { dialog, id ->

                    dialog.dismiss()
                })

        val alert = builder.create()
        alert.show()


    }


    // my button click function
    fun onProfileImageClick() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions()
                        } else {
                            // TODO - handle permission denied case
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, object : ImagePickerActivity.PickerOptionListener {
            override fun onChooseGallerySelected() {

                launchGalleryIntent();
            }

            override fun onTakeCameraSelected() {
                launchCameraIntent();

            }
        })
    }

    fun launchGalleryIntent() {
        val intent = Intent(this, ImagePickerActivity::class.java);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private fun launchCameraIntent() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE)

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)

        startActivityForResult(intent, REQUEST_IMAGE)
    }


    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri: Uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    var bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);


                    passportImagePath = uri.path
                    // loading profile image from local cache
                    ivPassportPageUpload.setImageResource(R.drawable.ic_passport_seleted)
                    //loadProfile(uri.toString());
                } catch (e: IOException) {
                    e.printStackTrace();
                }
            }
        }
    }


}
