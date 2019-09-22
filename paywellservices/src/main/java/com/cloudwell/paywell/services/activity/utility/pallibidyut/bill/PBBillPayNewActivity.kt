package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.topup.adapter.MyRecyclerViewAdapter
import com.cloudwell.paywell.services.activity.topup.model.MobileOperator
import com.cloudwell.paywell.services.activity.utility.ivac.DrawableClickListener
import com.cloudwell.paywell.services.utils.UniversalRecyclerViewAdapter
import com.cloudwell.paywell.services.utils.UniversalRecyclerViewAdapter.OnRecyclerViewItemDualViewSet
import kotlinx.android.synthetic.main.activity_merchant_type_verify.*
import kotlinx.android.synthetic.main.activity_pbbill_pay_new.*
import kotlinx.android.synthetic.main.activity_pbbill_pay_new.view.*
import kotlinx.android.synthetic.main.pallibidyut_action_view.view.*
import kotlinx.android.synthetic.main.pallibidyut_billpay_view.*
import kotlinx.android.synthetic.main.pallibidyut_billpay_view.view.*
import java.util.*
import kotlin.collections.ArrayList

class PBBillPayNewActivity : AppCompatActivity() {
    private var addNoFlag: Int = 0
    lateinit var universalRecyclerViewAdapter:UniversalRecyclerViewAdapter
    var billPayList:ArrayList<Any> =ArrayList<Any>()
    private val slideInAnim = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1f, Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f)
    private val slideOutAnim = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 1f,
            Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pbbill_pay_new)
//        pbBillPayRV.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true)
//        pbBillPayRV.layoutManager=LinearLayoutManager(this)
//        universalRecyclerViewAdapter= UniversalRecyclerViewAdapter(this,billPayList,object:UniversalRecyclerViewAdapter.OnRecyclerViewItemDualViewSet{
//            override fun setViewType(position: Int): Int {
//
//                if (billPayList.get(position) is String){
//                    return 1
//                }else{
//                    return 2
//                }
//            }
//
//            override fun onCreateView(viewGroup: ViewGroup?, viewType: Int): UniversalRecyclerViewAdapter.UniversalViewHolder {
//                if (viewType==1){
//                    return UniversalRecyclerViewAdapter.UniversalViewHolder(LayoutInflater.from(application).inflate(R.layout.pallibidyut_action_view, viewGroup, false))
//                }else{
//                    return UniversalRecyclerViewAdapter.UniversalViewHolder(LayoutInflater.from(application).inflate(R.layout.pallibidyut_billpay_view, viewGroup, false))
//                }
//            }
//
//            override fun onItemViewSet(universalViewHolder: UniversalRecyclerViewAdapter.UniversalViewHolder?, itemData: Any?, position: Int) {
//                if (itemData is String){
//                    universalViewHolder?.itemView?.imageAddIV?.setOnClickListener(View.OnClickListener {
//
//                        billPayList.add(BillPayModel("",""))
//                        sortFooterToLAst(billPayList)
//                        universalRecyclerViewAdapter.notifyDataSetChanged()
//                        pbBillPayRV.smoothScrollToPosition(billPayList.size-1)
//
//
//                    })
//                    universalViewHolder?.itemView?.submitButton?.setOnClickListener(View.OnClickListener {
//
//                    })
//
//
//                }else{
//                    universalViewHolder?.itemView?.pbBillNumberET?.setText((itemData as BillPayModel).billNumber)
//                    universalViewHolder?.itemView?.pbBillAmountET?.setText((itemData as BillPayModel).billAmount)
//                }
//            }
//
//        })
//        pbBillPayRV.adapter=universalRecyclerViewAdapter
//        billPayList.add("")
//        universalRecyclerViewAdapter.notifyDataSetChanged()
        addAnotherNo()
        addAnotherNo()
        addAnotherNo()
        addAnotherNo()
        addAnotherNo()
        addAnotherNo()
    }

    private fun addAnotherNo() {
        ++addNoFlag
        val topUpView = layoutInflater.inflate(R.layout.pallibidyut_billpay_view, null)



        if (addNoFlag == 1) {
            billViewRemoveImage.setVisibility(View.GONE)
        } else {

        }



        billMainLL.addView(topUpView)
        topUpView.startAnimation(slideInAnim)
    }


}
