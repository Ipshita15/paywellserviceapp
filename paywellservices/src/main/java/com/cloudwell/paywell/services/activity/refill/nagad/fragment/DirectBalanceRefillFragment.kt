package com.cloudwell.paywell.services.activity.refill.nagad.fragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.app.AppHandler
import kotlinx.android.synthetic.main.balance_refill_fragment.view.*

class DirectBalanceRefillFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.balance_refill_fragment, container, false)



        val line1 = "ডিরেক্ট ব্যালেন্স রিফিল অপশনটি সিলেক্ট করুন।"
        view.tvLine1.text = Html.fromHtml(line1)


        val rid = AppHandler.getmInstance(activity?.applicationContext).rid.toString()
        val ridLast5Digit = rid.substring(rid.lastIndex - 4)
        val next2 = "টাকার পরিমাণ লিখে \"Confirm\" করুন।"
        view.tvLine2.text = Html.fromHtml(next2)

        val next3 = "পেওয়েল পিন নম্বর দিন।"
        view.tvLine3.text = Html.fromHtml(next3)


        val next4 = "আপনার নগদ একাউন্ট নম্বর দিয়ে \"I agree to the terms and conditions\" সিলেক্ট করে \"Proceed\" করুন।"
        view.tvLine4.text = Html.fromHtml(next4)

        val next6 ="আপনার মোবাইলে নগদ থেকে এসএমএস এর মাধ্যমে প্রেরিত \"OTP\" নাম্বারটি দিয়ে \"Proceed\" করুন।"
        view.tvLine6.text = Html.fromHtml(next6)


        val next17 = "নগদের পিন নম্বর দিন।"
        view.textView17.text = Html.fromHtml(next17)

        val next7 ="সাথে সাথে পেওয়েল ব্যালেন্স অ্যাড হয়ে যাবে। (১.২ % সার্ভিস চার্জ প্রযোজ্য)"
        view.tvLine7.text = Html.fromHtml(next7)
        val next8 ="কোন কারণে ব্যালেন্স অ্যাড না হলে পেওয়েল হেল্প ডেস্ক-এ "+getFontStyle("কল")+" করুন।"
        view.tvLine8.text = Html.fromHtml(next8)

        view.tvLine8.setOnClickListener {
            val baseActivity = requireActivity() as BaseActivity
            baseActivity.callPreview(false, "")
        }



        return view
    }

    private fun getFontStyle(input: String): String {
        val textColor = "#5aac40"
        return "<b><font color='$textColor'>$input</font></b>"
    }


}// Required empty public constructor
