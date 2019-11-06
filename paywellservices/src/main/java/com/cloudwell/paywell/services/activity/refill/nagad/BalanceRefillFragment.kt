package com.cloudwell.paywell.services.activity.refill.nagad

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cloudwell.paywell.services.R
import kotlinx.android.synthetic.main.balance_refill_fragment.view.*

class BalanceRefillFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.balance_refill_fragment, container, false)


        val line1 = "নগদ অ্যাপ-এর মার্চেন্ট পে মেনুতে গিয়ে " + getFontStyle("QR") + " কোড স্ক্যান করুন বা ১১ ডিজিটের মোবাইল নাম্বারে " + getFontStyle("01787679661") + "  লিখে বাটনটি ট্যাপ করুন। টাকার পরিমাণ লিখে পরবর্তী ধাপে যান।"
        view.tvLine1.text = Html.fromHtml(line1)


        val next2 = "রেফারেন্স নম্বরে আপনার পেওয়েল RID এর শেষ পাঁচটি ডিজিট " + getFontStyle("(xxxxx)") + " লিখে নগদ এর PIN নম্বর দিয়ে পেমেন্ট সম্পন্ন করুন। কিছুক্ষণের মধ্যে স্বয়ংক্রিয়ভাবে পেওয়েল ব্যালেন্স যোগ হয়ে যাবে (১.২% সার্ভিস চার্জ প্রযোজ্য)। কোন কারণে ব্যালেন্স যোগ না হলে পেওয়েল অ্যাপ এর ব্যালেন্স ক্লেইম অপশন এ গিয়ে আপনার নগদ একাউন্ট নম্বর/ট্রানজেকশন আইডি ও টাকার পরিমাণ  দিয়ে ক্লেইম প্রক্রিয়া সম্পন্ন করুন। ৫ মিনিটের মধ্যে ব্যালেন্স অ্যাড না হলে কল করুন।"
        view.tvLine2.text = Html.fromHtml(next2)

        val next3 = "কিছুক্ষণের মধ্যে স্বয়ংক্রিয়ভাবে পেওয়েল ব্যালেন্স যোগ হয়ে যাবে (১.২% সার্ভিস চার্জ প্রযোজ্য)।"
        view.tvLine3.text = Html.fromHtml(next3)


        val next4 = "কোন কারণে ব্যালেন্স যোগ না হলে পেওয়েল অ্যাপ এর ব্যালেন্স " +getFontStyle("ক্লেইম অপশন")+ " এ গিয়ে আপনার নগদ একাউন্ট নম্বর/ট্রানজেকশন আইডি ও টাকার পরিমাণ  দিয়ে ক্লেইম প্রক্রিয়া সম্পন্ন করুন।"
        view.tvLine4.text = Html.fromHtml(next4)


        val next5 = "৫ মিনিটের মধ্যে ব্যালেন্স অ্যাড না হলে "+getFontStyle("কল")+" করুন।"
        view.tvLine5.text = Html.fromHtml(next5)

        return view
    }

    private fun getFontStyle(input: String): String {
        val textColor = "#5aac40"
        return "<b><font color='$textColor'>$input</font></b>"
    }


}// Required empty public constructor
