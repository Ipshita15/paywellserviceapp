package com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
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
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.WindowManager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.ResposeAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.fragment.GenderBottomSheetDialog
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.fragment.NameTitleSheetDialog
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.fragment.PassengerTypeSheetDialog
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.model.MyCountry
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.view.PassgerAddViewStatus
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.viewmodel.AddPassengerViewModel
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.constant.AllConstant.emailPattern
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
import java.text.SimpleDateFormat
import java.util.*


class AddPassengerActivity : AirTricketBaseActivity() {


    val REQUEST_IMAGE = 100

    private lateinit var viewMode: AddPassengerViewModel
    lateinit var touchHelper: ItemTouchHelper

    lateinit var resposeAirPriceSearch: ResposeAirPriceSearch
    var passportMadatory1 = false


    var isEmailValid = false
    var countryCode = ""
    var passportNationalityCountryCode = ""

    var isEditFlag = false


    private lateinit var oldPassenger: Passenger
    private var passportImagePath = ""
    var passportMadatory = false
    var isFistTime = true

    companion object {
        var KEY_PASSPORT_NATIONALITY = "KEY_PASSPORT_NATIONALITY"
        var KEY_COUNTRY = "KEY_COUNTRY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_passenger)

        setToolbar(getString(R.string.title_add_passenger))

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

        resposeAirPriceSearch = AppStorageBox.get(applicationContext, AppStorageBox.Key.ResposeAirPriceSearch) as ResposeAirPriceSearch
        passportMadatory1 = resposeAirPriceSearch.data?.results?.get(0)?.passportMadatory!!

        if (passportMadatory1) {
            etNidorPassportNumber.setHint(getString(R.string.hit_passport_number) + "*")
            etPassportExpiryDate.setHint(getString(R.string.passport_expiry_date) + "*")
            etpassportNationality.setHint(getString(R.string.passport_nationality_mannotory) + "*")
        }


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
                etPassportExpiryDate.setText(oldPassenger.passportExpiryDate)
                etpassportNationality.setText(oldPassenger.passportNationality)

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

                if (oldPassenger.isLeadPassenger) {
                    isLeadPassenger.isChecked = true
                    isLeadPassenger.visibility = View.VISIBLE

                } else {
                    if (oldPassenger.paxType.equals(getString(R.string.adult))) {
                        isLeadPassenger.visibility = View.VISIBLE
                        isLeadPassenger.isChecked = false
                    } else {
                        isLeadPassenger.visibility = View.GONE
                        isLeadPassenger.isChecked = false
                    }
                }




                this.countryCode = oldPassenger.countryCode
                this.passportNationalityCountryCode = oldPassenger.passportNationalityCountryCode

                btn_add.setText(getString(R.string.edit))


            }
        } catch (e: Exception) {

        }

        btn_add.setOnClickListener {
            addPassenger()
        }


        etPassengerType.setOnClickListener {

            handlePassengerType()
        }

        etPassengerType.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                handlePassengerType()
            }
        })


        etCountry.setOnClickListener {
            handleCountry(KEY_COUNTRY)
        }
        etCountry.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                handleCountry(KEY_COUNTRY)
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

        etpassportNationality.setOnClickListener {
            handleCountry(KEY_PASSPORT_NATIONALITY)
        }

        etpassportNationality.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                handleCountry(KEY_PASSPORT_NATIONALITY)
            }
        })





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

        etPassportExpiryDate.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                showPassportExpiryDate()

            }
        })


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

    private fun handleCountry(key: String) {
        hideUserKeyboard()
        val builder = CountryPicker.Builder().with(applicationContext)
                .listener(object : OnCountryPickerListener {
                    override fun onSelectCountry(country: Country) {
                        if (KEY_COUNTRY.equals(key)) {
                            etCountry.setText("" + country.name)
                            countryCode = country.code
                        } else if (KEY_PASSPORT_NATIONALITY.equals(key)) {
                            etpassportNationality.setText("" + country.name)
                            passportNationalityCountryCode = country.code


                            var nationality = "";
                            val countriesString = AssetHelper().loadJSONFromAsset(applicationContext, "countries.json")
                            val countries = Gson().fromJson(countriesString, Array<MyCountry>::class.java)
                            countries.forEach {
                                if (it.en_short_name.equals(country.name)) {
                                    nationality = it.nationality
                                }
                            }

                            etpassportNationality.setText("" + nationality)


                        }


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
                    if (text.equals(getString(R.string.adult))) {
                        isLeadPassenger.visibility = View.VISIBLE
                        isLeadPassenger.isChecked = true
                    } else {
                        isLeadPassenger.visibility = View.GONE
                        isLeadPassenger.isChecked = false
                    }

                    etPassengerType.setText(text)
                }

            })

            bottomSheet.arguments = b
            bottomSheet.show(supportFragmentManager, "genderBottomSheet")
        } else {
            isFistTime = false
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
        val passportExpiryDate = this.etPassportExpiryDate.text.toString().trim()
        val passportNationalityCountry = this.etpassportNationality.text.toString().trim()
        val nationalIDNumber = this.etNationalIDNumber.text.toString().trim()


        if (passengerType.equals("")) {
            textInputLayoutPassengerType.error = getString(R.string.invalid_passenger_type)
            return
        } else {
            textInputLayoutPassengerType.error = ""
        }

        if (title.equals("")) {
            textInputLayoutTitle.error = getString(R.string.invalid_title)
            return
        } else {
            textInputLayoutTitle.error = ""
        }

        if (firstName.equals("")) {
            textInputLayoutFirstName.error = getString(R.string.invalid_first_name)
            return
        } else {
            textInputLayoutFirstName.error = ""
        }

        if (lastName.equals("")) {
            textInputLayoutLastName.error = getString(R.string.invalid_last_name)
            return
        } else {
            textInputLayoutLastName.error = ""
        }

        if (country.equals("")) {
            textInputLayoutCountry.error = getString(R.string.invalid_country_name)
            return
        } else {
            textInputLayoutCountry.error = ""
        }

        if (gender.equals("")) {
            textInputLayoutGender.error = getString(R.string.invalid_gender)
            return
        } else {
            textInputLayoutGender.error = ""
        }

        if (contactNumber.equals("")) {
            textInputLayoutContactNumber.error = getString(R.string.invalid_contact_name)
            return
        } else {
            textInputLayoutContactNumber.error = ""
        }


        if (emailAddress.matches(emailPattern.toRegex()) && emailAddress.length > 0) {
            isEmailValid = true
            textInputLayoutEmail.error = ""

        } else {
            isEmailValid = false
            textInputLayoutEmail.error = getString(R.string.invalid_email)
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


//        var passportNationality = ""
//        countries.forEach {
//            if (it.en_short_name.equals(passportNationalityCountry)) {
//                passportNationality = it.nationality
//            }
//        }


        if (passportMadatory1) {
            if (passportNumber.equals("")) {
                textInputLayoutPassport.error = getString(R.string.Passport_number_mandatory)
                return
            }

            if (passportExpiryDate.equals("")) {
                textInputLayoutPassportExpiryDate.error = getString(R.string.passport_expiry_date_mandatory)
                return
            }

            if (passportNationalityCountry.equals("")) {
                textLayoutPassportNationality.error = getString(R.string.passport_expiry_date_mandatory)
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
        passenger.passportExpiryDate = passportExpiryDate
        passenger.passportNationality = passportNationalityCountry
        passenger.passportNationalityCountryCode = passportNationalityCountryCode
        passenger.isPassengerSleted = true
        passenger.passportImagePath = passportImagePath
        passenger.nIDnumber = nationalIDNumber
        passenger.isLeadPassenger = isLeadPassenger.isChecked


        if (!passportImagePath.equals("")) {
            val lastIndexOf = passportImagePath.lastIndexOf('.')
            if (lastIndexOf > 0) {
                val extension = passportImagePath.substring(lastIndexOf + 1);
                passenger.file_extension = extension
            }

        }

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

    private fun showPassportExpiryDate() {

        val calendar = Calendar.getInstance()


        val year = calendar.get(Calendar.YEAR)
        val thismonth = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)

                    val mMonth = month + 1
                    val androidSystemdate = "${year}-${mMonth}-${day}"
                    val fdepTimeFormatDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(androidSystemdate) as Date
                    val humanReadAbleDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).format(fdepTimeFormatDate)
                    etPassportExpiryDate.setText(humanReadAbleDate)


                }, year, thismonth, dayOfMonth)

        val calendarMin = Calendar.getInstance()
        datePickerDialog.datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))


        datePickerDialog.datePicker.minDate = calendarMin.timeInMillis




        datePickerDialog.show()


    }


}
