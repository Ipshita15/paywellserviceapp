package com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import kotlinx.android.synthetic.main.fragment_tricket_chooser.view.*


class TricketChooserFragment : DialogFragment() {

    lateinit var datum: Datum
    lateinit var onClickHandler: OnClickHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun setOnClickHandlerTest(onClickHandler: OnClickHandler) {
        this.onClickHandler = onClickHandler
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_tricket_chooser, container, false)
        v.btCencel.setOnClickListener {
            dismiss()
            onClickHandler.onClick("view")
        }

        if (datum.invoiceUrlWithFare != null) {
            if (!datum.invoiceUrlWithFare.equals("")) {
                v.btTicketViewWithPare.visibility = View.VISIBLE
                v.btTicketViewWithPare.setOnClickListener {
                    dismiss()
                    onClickHandler.onClick("view_with_fare")
                }
            } else {
                v.btTicketViewWithPare.visibility = View.GONE
            }

        } else {
            v. btTicketViewWithPare.visibility = View.GONE
        }



        v.btSendEmail.setOnClickListener {
            dismiss()
            onClickHandler.onClick("email")
        }
        return v
    }


    interface OnClickHandler {
        fun onClick(s: String);

    }

}
