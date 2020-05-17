package com.cloudwell.paywell.services.activity.entertainment.Bongo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.entertainment.Bongo.PackegeSelectedActivity
import com.cloudwell.paywell.services.activity.entertainment.Bongo.adapter.BongoPackegeAdapter.BongoPackegeViewHolder
import com.cloudwell.paywell.services.activity.entertainment.Bongo.model.PackagesItem
import com.google.gson.Gson
import kotlinx.android.synthetic.main.bongo_package_item.view.*


/**
 * Created by Sepon on 4/27/2020.
 */
class BongoPackegeAdapter (val mContext : Context, var packageList : ArrayList<PackagesItem?>? ): RecyclerView.Adapter<BongoPackegeViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BongoPackegeViewHolder {
        val v: View = LayoutInflater.from(parent?.context)
                .inflate(R.layout.bongo_package_item,parent,false)

        return BongoPackegeViewHolder(v)
    }

    override fun getItemCount(): Int {
      return packageList!!.size
    }

    override fun onBindViewHolder(holder: BongoPackegeViewHolder, position: Int) {

        holder.title.setText(packageList!!.get(position)!!.title)
        holder.price.setText(packageList!!.get(position)!!.currency+" "+packageList!!.get(position)!!.price)
        holder.itemView.setOnClickListener(View.OnClickListener {
            val gson = Gson()
            val selected_package = gson.toJson(packageList?.get(position))
            val intent = Intent(mContext, PackegeSelectedActivity::class.java)
            intent.putExtra("selected_package", selected_package )
            mContext.startActivity(intent)

        })

    }


    class BongoPackegeViewHolder(v : View): RecyclerView.ViewHolder(v) {
       val title = itemView.bongo_package_title
        val price = itemView.bongo_price

    }
}