package com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View.OnFocusChangeListener
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.fragment.GenderBottomSheetDialog
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.model.MyCountry
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.view.PassgerAddViewStatus
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.viewmodel.AddPassengerViewModel
import com.cloudwell.paywell.services.utils.AssetHelper
import com.google.gson.Gson
import com.mukesh.countrypicker.Country
import com.mukesh.countrypicker.CountryPicker
import com.mukesh.countrypicker.listeners.OnCountryPickerListener
import kotlinx.android.synthetic.main.contant_add_passenger.*


class AddPassengerActivity : AirTricketBaseActivity() {


    private lateinit var viewMode: AddPassengerViewModel
    lateinit var touchHelper: ItemTouchHelper

    // ui

    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    var isEmailValid = false
    var countryCode = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_add_passenger)

        setToolbar(getString(com.cloudwell.paywell.services.R.string.title_add_passenger))

        initializationView()


        initViewModel()


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
        etCountry.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
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
        })



        etGender.setOnFocusChangeListener(OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
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


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.menu_add_passenger, menu)
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
        val title = this.etTitle.text.toString().trim()
        val firstName = this.etFirstName.text.toString().trim()
        val lastName = this.etLastName.text.toString().trim()
        val country = this.etCountry.text.toString().trim()
        val gender = this.etGender.text.toString().trim()
        val contactNumber = this.etContactNumber.text.toString().trim()
        val emailAddress = this.etEmail.text.toString().trim()

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

        if (!isEmailValid) {
            textInputLayoutEmail.error = "invalid email"
            return
        } else {
            textInputLayoutEmail.error = ""
        }


        var nationality = "";
        val countriesString = AssetHelper().loadJSONFromAsset(applicationContext, "countries.json")
        val countries = Gson().fromJson(countriesString, Array<MyCountry>::class.java)
        countries.forEach {
            if (it.en_short_name.equals(country)) {
                nationality = it.nationality
            }
        }


        val passenger = Passenger(false)
        passenger.title = title
        passenger.firstName = firstName
        passenger.lastName = lastName
        passenger.gender = gender
        passenger.countryCode = countryCode
        passenger.nationality = nationality
        passenger.contactNumber = contactNumber
        passenger.email = emailAddress


        viewMode.addPassenger(passenger)

    }

}
