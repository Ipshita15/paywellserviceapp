@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.cloudwell.paywell.services.activity.healthInsurance

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.HealthInsuranceBaseActivity
import com.cloudwell.paywell.services.activity.education.PaywellPinDialog
import com.cloudwell.paywell.services.activity.healthInsurance.model.ActivePakage
import com.cloudwell.paywell.services.activity.healthInsurance.model.ActiveResponse
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import kotlinx.android.synthetic.main.activity_helth_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class HelthInfoActivity : HealthInsuranceBaseActivity() {

    var idDoc : String? = null
    var mobileNumber : String? = null
    var healthPackage : String?  = null
    var memberName : String? = null
    var birth_date : String? = null
    var gender : String? = null
    var memberidcard : String? = null
    var otherNumber : String?  = null
    var relation : String? = null
    var payWellPin : Int? = null
    var idType : String? = null



    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_helth_info)

        setToolbar(getString(R.string.user_details))
        mAppHandler = AppHandler.getmInstance(applicationContext)



        val intent = getIntent();
        idDoc = intent.getStringExtra(getString(R.string.document))
        mobileNumber = intent.getStringExtra(getString(R.string.healthmobile))
        healthPackage = intent.getStringExtra(getString(R.string.selected_helth_p))


        active_mobile.setText(mobileNumber)
        if (idDoc == "1"){
            id_helth.setHint(getString(R.string.nid_paper) + " " + getString(R.string.number_in_english))
            idType = "NID"
        }else if (idDoc == "2"){
            id_helth.setHint(getString(R.string.passport) + " " + getString(R.string.number_in_english))
            idType = "Passport"

        }else if (idDoc == "3"){
            id_helth.setHint(getString(R.string.helth_birth_certificate) + " " + getString(R.string.number_in_english))
            idType = "BirthCertificate"
        }

        setGenderSpinner()
        setRelationSpinner()


        date_et.setOnClickListener(View.OnClickListener {
            showDepartDatePicker()
        })



        active_package.setOnClickListener(View.OnClickListener {


            getAllinputData()



        })






    }

    private fun getAllinputData() {


        memberName = member_name.text.toString()
        memberidcard = id_helth.text.toString()
        otherNumber = other_number.text.toString()



        if (memberName.isNullOrEmpty()){
            member_name.setError("Required")
        }else if (memberidcard.isNullOrEmpty()){
            id_helth.setError("Required")

        }else if (gender.isNullOrEmpty()){
            showErrorMessagev1(getString(R.string.select)+" "+getString(R.string.gender))

        }else if (birth_date.isNullOrEmpty()){
            getString(R.string.select)+" "+getString(R.string.date_of_birth)
        }else{

            //show pin dialog
            val askingPinDialog = PaywellPinDialog("", object : PaywellPinDialog.IonClickInterface {
                override fun onclick(pin: String) {
                    payWellPin = pin.toInt()
                    callActive()

                }
            })
            askingPinDialog.show(supportFragmentManager, "Pin Dialog")
        }

    }

    private fun callActive() {
        showProgressDialog()

        val activePakage = ActivePakage()

        activePakage.dateOfBirth = birth_date
        activePakage.gender = gender
        activePakage.identificationType = idType
        activePakage.memberName = memberName
        activePakage.mobile = mobileNumber
        activePakage.nomineePhoneNumber = otherNumber
        activePakage.packagesId = healthPackage
        activePakage.password = payWellPin
        activePakage.username = mAppHandler.userName
        activePakage.identificationNumber = memberidcard



        ApiUtils.getAPIServiceV2().activatePackage(activePakage).enqueue(object : Callback<ActiveResponse>{
            override fun onResponse(call: Call<ActiveResponse>, response: Response<ActiveResponse>) {
                dismissProgressDialog()

                if (response.isSuccessful){



                    val body = response.body()
                    val msg  = response.message()

                    if(response.body()?.statusCode == 200){
                        showSuccessDialog("Health Insurence", response.body()!!.message)
                    }else{
                        showErrorCallBackMessagev1(response.body()?.message)

                    }




                }else{
                    showErrorMessagev1(response.message())
                }



            }

            override fun onFailure(call: Call<ActiveResponse>, t: Throwable) {
               dismissProgressDialog()
                showErrorMessagev1(t.message)
            }
        })




    }


    private fun setGenderSpinner() {
        val country = arrayOf(getString(R.string.please_select),"Male", "Female", "Other")
      //  val country = arrayOf(getString(R.string.please_select), )
        val sp : Spinner = gender_spinner
        sp.onItemSelectedListener
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.spinner_item, country)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp.adapter = aa
        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position != 0){
                    gender = country.get(position)

                }

            }
        }

    }


    private fun setRelationSpinner(){


        val country = arrayOf(getString(R.string.please_select), "Father", "Mother", "Husband/Wife", "Son" ,"Daughter")
        val sp : Spinner = relation_sp
        sp.onItemSelectedListener
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.spinner_item, country)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp.adapter = aa
        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if(position != 0){
                    relation = country.get(position)
                }

            }
        }


    }

    private fun showDepartDatePicker() {

        val calendar = Calendar.getInstance()


        val year = calendar.get(Calendar.YEAR)
        val thismonth = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = this?.let {
            DatePickerDialog(it,
                    DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(Calendar.DAY_OF_MONTH, day)

                        val mMonth = month + 1;
                        birth_date = "$year /$mMonth /$day"
                        date_et.text = birth_date


                    }, year, thismonth, dayOfMonth)
        }

        val calendarMin = Calendar.getInstance()
        datePickerDialog?.datePicker?.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.setMaxDate(System.currentTimeMillis() - 568025136000L)
        datePickerDialog?.show()



    }

    private fun getAge(year: Int, month: Int, day: Int): String? {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        val ageInt = age
        return ageInt.toString()
    }

}