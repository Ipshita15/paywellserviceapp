package com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.model.ResCancellationMapping
import com.cloudwell.paywell.services.constant.AllConstant
import kotlinx.android.synthetic.main.fragment_cancel_fee.view.*
import kotlinx.android.synthetic.main.fragment_prices_change.view.btActionIssueTicket


class UserAcceptDialogFragment : DialogFragment() {


    companion object {
        lateinit var resCencelMaping: ResCancellationMapping
        lateinit var type: String
    }

    lateinit var onClickHandler: OnClickHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun setOnClickHandlerTest(onClickHandler: OnClickHandler) {
        this.onClickHandler = onClickHandler
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_cancel_fee, container, false)
        v.btActionIssueTicket.text = getString(R.string.text_accept)
        v.btDecline.text = getString(R.string.text_decline)

        val count = resCencelMaping.bookingData.get(0).passengers.count()
        val void = resCencelMaping.getCancelData().void
        val reIssue = resCencelMaping.cancelData.reIssue
        val infoUpdate = resCencelMaping.cancelData.infoUpdate
        val refund = resCencelMaping.cancelData.refund

        if (type.equals(AllConstant.Action_reIssueTicket)) {

            v.tvFree.text = reIssue

        } else if (type.equals(AllConstant.Action_Void)) {

            v.tvFree.text = void

        } else if (type.equals(AllConstant.Action_Refund)) {

            v.tvFree.text = refund

        } else if (type.equals(AllConstant.Action_DOCS_UPDATE)) {

            v.tvFree.text = infoUpdate

        }

        v.btActionIssueTicket.setOnClickListener {
            dismiss()
            onClickHandler.onClickActionIssueTicket(type)
        }

        v.btDecline.setOnClickListener {
            dismiss()
        }


        return v
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }


    interface OnClickHandler {
        fun onClickActionIssueTicket(type: String)

    }

}
