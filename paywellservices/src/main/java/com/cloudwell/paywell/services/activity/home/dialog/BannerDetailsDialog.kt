package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.signature.ObjectKey
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import com.cloudwell.paywell.services.app.AppController
import kotlinx.android.synthetic.main.banner_details.view.*


class BannerDetailsDialog(val image: String, val bannerDetail: String) : BaseDialogFragment() {

    private val roundEadius = 16


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.banner_details, null)

        Glide.with(AppController.getContext())
                .load(image)
                .signature(ObjectKey(image))
                .transform(CenterCrop(), RoundedCorners(roundEadius))
                .into(view.imageViewBanner)



       // val test = "<b> <p> প্রিয় এজেন্ট, </p></b> <p>আমরা অত্যন্ত আনন্দের সাথে জানাচ্ছি যে, পুরো ফেব্রুয়ারী মাস জুড়ে রিটেইলারদের জন্য ইন্ডিয়ান ভিসা পেমেন্টের উপর  পেওয়েল দিচ্ছে এক আকর্ষণীয় *মাতৃভাষা অফার*। </p> </br></br></br> <p>অফার মেয়াদকাল ১লা ফেব্রুয়ারী - ২৯শে ফেব্রুয়ারী ।</p> <p>ক্যাম্পেইন বিস্তারিত - </p> <p> চার ক্যাটাগরির বিজয়ী থাকবে। মেগা, এচিভারস, কোয়ালিফাইং ও লাকি ইউনার।</p> <p>ক্যাটাগরি - </p> <p> মেগা পুরস্কারঃ ১টি</p> <p>যোগ্যতা:</p> <p>- সর্বোচ্চ ভিসা ফি দাতা রিটেইলার এই ক্যাটাগরিতে অন্তর্ভুক্ত হবে।<p> <p> কমপক্ষে ৮০০০ ভিসা ফি দিতে হবে। </p> <p>যদি ফি প্রদানের পরিমাণ টার্গেট এর পরিমাণের চাইতে কম হয় তাহলে ঐ রিটেইলার মেগা পুরস্কার ক্যাটাগরি থেকে বাতিল বলে গণ্য হবে, বিলের পরিমাণ যাই হোক না কেন।</p> <p>ক্যাটেগরি - ২ </p>";



        view.tvBannerdetails.text =  HtmlCompat.fromHtml(bannerDetail, HtmlCompat.FROM_HTML_MODE_LEGACY)


        view.buttonDmiss.setOnClickListener {
            dismiss()
        }
        return view

    }


    public interface OnClickHandler {

        public fun onSubmit(mobileNumber: String, pin: String)
        public fun onForgetPinNumber();
    }

}