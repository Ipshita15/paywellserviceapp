package com.cloudwell.paywell.services.activity.refill.nagad

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.app.AppHandler
import kotlinx.android.synthetic.main.balance_refill_ussd_fragment.view.*

class UssdRefillFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.balance_refill_ussd_fragment, container, false)


        val line1 = getFontStyle("*167#")+" ডায়াল করে পেমেন্টের 4 লিখে Send করুন।"
        view.tvLine1.text = Html.fromHtml(line1)


        val next2 = "Enter Merchant Number -এ  "+getFontStyle("01787679661")+"  লিখে Send" + "করুন।"
        view.tvLine2.text = Html.fromHtml(next2)

        val next3 = "Counter Number -এ 1 লিখে "+getFontStyle("Send")+" করুন।"
        view.tvLine3.text = Html.fromHtml(next3)


        val rid = AppHandler.getmInstance(activity?.applicationContext).rid.toString()
        val ridLast5Digit = rid.substring(rid.lastIndex - 4);


        val next4 = "Reference Number -এ আপনার RID এর শেষ ৫ ডিজিট " + getFontStyle("$ridLast5Digit") + " লিখে Send করুন।\n" + "\nপ্রদত্ত তথ্যগুলো ঠিক থাকলে নগদ PIN এর দিয়ে পেমেন্ট প্রক্রিয়া শেষ করুন।"

        view.tvLine4.text = Html.fromHtml(next4)

        val next5 = "কিছুক্ষণের মধ্যে স্বয়ংক্রিয়ভাবে পেওয়েল ব্যালেন্স যোগ হয়ে যাবে (১.২% সার্ভিস চার্জ প্রযোজ্য)।"
        view.tvLine5.text = Html.fromHtml(next5)

        val next6 = "কোন কারণে ব্যালেন্স যোগ না হলে পেওয়েল অ্যাপ এর ব্যালেন্স ক্লেইম অপশন এ গিয়ে আপনার নগদ একাউন্ট নম্বর/ট্রানজেকশন আইডি ও টাকার পরিমাণ  দিয়ে ক্লেইম প্রক্রিয়া সম্পন্ন করুন।"
        view.tvLine6.text = Html.fromHtml(next6)

        val next7 = "৫ মিনিটের মধ্যে ব্যালেন্স অ্যাড না হলে "+getFontStyle("কল")+" করুন।"
        view.tvLine7.text = Html.fromHtml(next7)


        return view
    }

    private fun getFontStyle(input: String): String {
        val textColor = "#5aac40"
        return "<b><font color='$textColor'>$input</font></b>"
    }
}
