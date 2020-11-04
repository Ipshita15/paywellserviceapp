package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.WebViewActivity
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import kotlinx.android.synthetic.main.new_apps_video_preview.view.*


class NewAppsVideoPreviewDialog() : BaseDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.new_apps_video_preview, null)

        isCancelable = false

        view.tvSkip.setOnClickListener {
            dismiss()
        }

        view.errorIV.setOnClickListener {
            openLink()
        }

        view.tvPlayVideo.setOnClickListener {
            openLink()
        }

        return view
    }

    private fun openLink() {
        dismiss()

        val link = "https://www.youtube.com/watch?v=vx0mhpuN2K4&feature=youtu.be"
        val mIntent: Intent = activity!!.getPackageManager().getLaunchIntentForPackage("com.google.android.youtube")
        if (mIntent != null) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.google.android.youtube")
            startActivity(intent)
        } else {
            WebViewActivity.TAG_LINK = link
            startActivity(Intent(activity, WebViewActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.getWindow().setLayout(width, height)
        }
    }


}