package com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.constant.AllConstant.emailPattern
import kotlinx.android.synthetic.main.fragment_add_email.*
import kotlinx.android.synthetic.main.fragment_add_email.view.*


class EmailAddFragment : DialogFragment() {

    lateinit var onClickHandler: OnClickHandler


    fun setOnClickHandlerTest(onClickHandler: OnClickHandler) {
        this.onClickHandler = onClickHandler
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_add_email, container, false)

        v.etEmailAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

                if (s.matches(emailPattern.toRegex()) && s.length > 0) {
                    textInputLayoutEmail.error = ""
                } else {
                    textInputLayoutEmail.error = getString(R.string.invalid_email)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // other stuffs
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // other stuffs
            }
        })

        v.btAdd.setOnClickListener {
            val emailAddress = etEmailAddress.text.toString()

            if (emailAddress.matches(emailPattern.toRegex()) && emailAddress.length > 0) {
                textInputLayoutEmail.error = ""

                onClickHandler.onClick("email", emailAddress)
                dismiss()

            } else {
                textInputLayoutEmail.error = getString(R.string.invalid_email)

            }
        }

        v.btCencel.setOnClickListener {
            dismiss()
            onClickHandler.onClick("view", "emailAddress")
        }
        return v
    }


    interface OnClickHandler {
        fun onClick(s: String, emailAddress: String)
    }
}
