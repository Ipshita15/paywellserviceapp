package com.cloudwell.paywell.services.activity.healthInsurance.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.healthInsurance.model.MembershipPackagesItem
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.utils.FormatHelper
import kotlinx.android.synthetic.main.health_care_package_item.view.*


/**
 * Created by Sepon on 4/27/2020.
 */
public class PackegeAdapter(val mContext: Context, var packageList: List<MembershipPackagesItem>, val onclickInterface: OnclickInterface) : RecyclerView.Adapter<PackegeAdapter.PackegeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackegeViewHolder {
        val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.health_care_package_item, parent, false)

        return PackegeViewHolder(v)
    }

    override fun getItemCount(): Int {
        return packageList!!.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PackegeViewHolder, position: Int) {

        val m = packageList.get(position)

        Glide.with(AppController.getContext())
                .load(m.packageImage)
//                .signature(ObjectKey(mImageUpdateVersionString)) //                .transform(new CenterCrop(), new RoundedCorners(roundEadius))
                .into(holder.ivBannerSmallHI)


        holder.tvPackageName.text = FormatHelper.formatText(m.name)
        holder.tvPackageValidity.text = m.validity// + " "+ mContext.resources.getString(R.string.month_validity)//getString(R.string.month_validity)
        val cashbackFull = "<font color='#3d6fc4'>${m.cashBackAmount}</font>" + "<font color='#59595c'> ${m.cashBackMessage}</font>"

        holder.tvPackageCashback.text = FormatHelper.formatText(cashbackFull)

        val packageMemberFull = "<font color='#3d6fc4'>${m.member}</font>" + "<font color='#59595c'> ${m.memberMessage}</font>"
        holder.tvPackageMember.text = FormatHelper.formatText(packageMemberFull)

        holder.tvPageAmount.text = m.amount


        val adapter = DocConsultationAdapter(m.doctorConsultation)
        holder.rvDoctorConsultation.layoutManager = LinearLayoutManager(mContext)
        holder.rvDoctorConsultation.adapter = adapter


        holder.tvPackageActive.setOnClickListener {
            onclickInterface.onclick(m)

        }

    }


    class PackegeViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvPackageName = v.tvPackageName
        val tvPackageValidity = v.tvPackageValidity
        val tvPackageCashback = v.tvPackageCashback
        val tvPackageMember = v.tvPackageMember
        val tvAmountLogo = v.tvAmountLogo
        val tvPageAmount = v.tvPageAmount
        val ivBannerSmallHI = v.ivBannerSmallHI
        val rvDoctorConsultation = v.rvDoctorConsultation
        val tvPackageActive = v.tvPackageActive
//        val tvCashbackMessage = v.tvCashbackMessage
//        val tvMemberMessage = v.tvMemberMessage

    }


    interface OnclickInterface {
        fun onclick(m: MembershipPackagesItem)
    }
}