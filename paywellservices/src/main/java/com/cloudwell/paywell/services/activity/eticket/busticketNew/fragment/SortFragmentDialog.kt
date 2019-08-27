package com.cloudwell.paywell.services.activity.eticket.busticketNew.fragment

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.DialogFragment
import com.cloudwell.paywell.services.R
import kotlinx.android.synthetic.main.fragment_sort_fragment.view.*


class SortFragmentDialog : DialogFragment(), CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.radioButtonLowPrice -> {
                onSortClickListener?.buttonLowPrice()
                dismiss()
            }
            R.id.radioButtonHightPrices -> {
                onSortClickListener?.buttonHeightPrice()
                dismiss()
            }
            R.id.radioButtonLowDepartureTime -> {
                onSortClickListener?.buttonLowDepartureTime()
                dismiss()
            }

            R.id.radioButtonHighDepartureTime -> {
                onSortClickListener?.buttonHeightDepartureTime()
                dismiss()
            }
            R.id.radioButtonLowAvailableSeat -> {
                onSortClickListener?.buttonLowtAvailableSeat()
                dismiss()

            }
            R.id.radioButtonHightAvailableSeat -> {

                onSortClickListener?.buttonHeightAvailableSeat()
                dismiss()
            }

        }
    }

    private var onSortClickListener: OnSortClickListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun setOnClickListener(onSortClickListener: OnSortClickListener) {
        this.onSortClickListener = onSortClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_sort_fragment, container, false)
        v.radioButtonLowPrice.setOnCheckedChangeListener(this)
        v.radioButtonHightPrices.setOnCheckedChangeListener(this)
        v.radioButtonLowDepartureTime.setOnCheckedChangeListener(this)
        v.radioButtonHighDepartureTime.setOnCheckedChangeListener(this)
        v.radioButtonLowAvailableSeat.setOnCheckedChangeListener(this)
        v.radioButtonHightAvailableSeat.setOnCheckedChangeListener(this)
        return v
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    interface OnSortClickListener {
        fun buttonLowPrice()
        fun buttonHeightPrice()
        fun buttonLowDepartureTime()
        fun buttonHeightDepartureTime()
        fun buttonHeightAvailableSeat()
        fun buttonLowtAvailableSeat()

    }
}


