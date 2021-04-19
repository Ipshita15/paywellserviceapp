@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.cloudwell.paywell.services.activity.healthInsurance

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.HealthInsuranceBaseActivity
import kotlinx.android.synthetic.main.activity_helth_info.*
import java.text.SimpleDateFormat
import java.util.*

class HelthInfoActivity : HealthInsuranceBaseActivity() {

    var idDoc : String? = null
    var mobileNumber : String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_helth_info)

        setToolbar(getString(R.string.user_details))


        val intent = getIntent();
        idDoc = intent.getStringExtra(getString(R.string.document))
        mobileNumber = intent.getStringExtra(getString(R.string.healthmobile))

        active_mobile.setText(mobileNumber)
        id_helth.setHint(idDoc+" "+getString(R.string.number_in_english))


        setGenderSpinner()
        setRelationSpinner()


        date_et.setOnClickListener(View.OnClickListener {
            showDepartDatePicker()
        })









    }

    private fun setGenderSpinner() {
        val country = arrayOf(getString(R.string.selectfrom),getString(R.string.male), getString(R.string.female),getString(R.string.other))
        val sp : Spinner = gender_spinner
        sp.onItemSelectedListener
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.spinner_item, country)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp.adapter = aa
        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }
        }

    }


    private fun setRelationSpinner(){


        val country = arrayOf(getString(R.string.selectfrom),getString(R.string.father), getString(R.string.mother),getString(R.string.husband_wife), getString(R.string.son),  getString(R.string.daughter))
        val sp : Spinner = relation_sp
        sp.onItemSelectedListener
        val aa: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.spinner_item, country)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp.adapter = aa
        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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
                        val date = calendar.getTime()

                        val nameOfDayOfWeek = SimpleDateFormat("EEE", Locale.ENGLISH).format(date)
                        val nameOfMonth = SimpleDateFormat("MMM", Locale.ENGLISH).format(calendar.getTime())

                        date_et.text = "$nameOfDayOfWeek, $day $nameOfMonth"
                        //tvDepartDate.setTextColor(Color.BLACK);


                    }, year, thismonth, dayOfMonth)
        }

        val calendarMin = Calendar.getInstance()
        datePickerDialog?.datePicker?.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog?.datePicker?.minDate = (calendarMin.timeInMillis - 10000)
        datePickerDialog?.show()



    }

}