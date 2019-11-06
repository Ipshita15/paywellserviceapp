package com.cloudwell.paywell.services.activity.refill.nagad;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;

import androidx.fragment.app.Fragment;

public class BalanceRefillFragment extends Fragment {

    public BalanceRefillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balance_refill_fragment, container, false);
        TextView tvView = view.findViewById(R.id.tvLine1);

        String next = "নগদ অ্যাপ-এর মার্চেন্ট পে মেনুতে গিয়ে "+getFontStyle("QR")+" কোড স্ক্যান করুন বা ১১ ডিজিটের মোবাইল নাম্বারে "+getFontStyle("01787679661")+"  লিখে বাটনটি ট্যাপ করুন। টাকার পরিমাণ লিখে পরবর্তী ধাপে যান।";
        tvView.setText(Html.fromHtml(next));



        return view;
    }

    private String getFontStyle(String input) {
        String textColor = "#5aac40";
        return "<b><font color='"+textColor+"'>"+input+"</font></b>";
    }


}
