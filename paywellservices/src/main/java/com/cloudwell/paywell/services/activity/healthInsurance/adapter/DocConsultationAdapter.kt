package com.cloudwell.paywell.services.activity.healthInsurance.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.healthInsurance.model.DoctorConsultationItem
import com.cloudwell.paywell.services.app.AppController
import kotlinx.android.synthetic.main.health_care_docconsultation_item.view.*


/**
 * Created by Sepon on 4/27/2020.
 */
public class DocConsultationAdapter(var packageList: List<DoctorConsultationItem>) : RecyclerView.Adapter<DocConsultationAdapter.PackegeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackegeViewHolder {
        val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.health_care_docconsultation_item, parent, false)

        return PackegeViewHolder(v)
    }

    override fun getItemCount(): Int {
        return packageList!!.size
    }


    override fun onBindViewHolder(holder: PackegeViewHolder, position: Int) {

        val m = packageList.get(position)



        Glide.with(AppController.getContext())
                .load(m.image)
//                .signature(ObjectKey(mImageUpdateVersionString)) //                .transform(new CenterCrop(), new RoundedCorners(roundEadius))
                .into(holder.tvPackageName)


        holder.name.text = m.name


    }

    class PackegeViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvPackageName = v.imageViewIcon
        val name = v.name


    }
}