package com.cloudwell.paywell.services.activity.home.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.cloudwell.paywell.services.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initilation()
    }

    private fun initilation() {
        etAccountID.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changeButtonColorStatus()

            }
        })

        etPinNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changeButtonColorStatus()
            }
        })

    }

    private fun changeButtonColorStatus() {
        val accountIDString = etAccountID.text.toString().trim()
        val pinNumberString = etPinNumber.text.toString().trim()
        if (!accountIDString.isEmpty() && !pinNumberString.isEmpty()) {
            btSingIn.setBackgroundResource(R.drawable.rounded_button_sign_in_enable)
        } else {
            btSingIn.setBackgroundResource(R.drawable.rounded_button_sign_in_disable)
        }
    }
}