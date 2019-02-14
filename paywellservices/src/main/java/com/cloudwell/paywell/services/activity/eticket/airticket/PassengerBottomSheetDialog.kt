package com.cloudwell.paywell.services.activity.eticket.airticket


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.cloudwell.paywell.services.R

class PassengerBottomSheetDialog : BottomSheetDialogFragment() {

    lateinit var mListenerPsngr: PsngrBottomSheetListener
    lateinit var userType: ArrayList<UserTypeModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val adultNumber: String = arguments!!.getString("myAdult")
        val kidNumber: String = arguments!!.getString("myKid")
        val infantNumber: String = arguments!!.getString("myInfant")

        val view = inflater.inflate(R.layout.fragment_passenger_bottom_sheet_dialog, container, false)

        val listView = view.findViewById<ListView>(R.id.listViewAirTicketPassengers)

        userType = ArrayList()
        userType.add(UserTypeModel("Adults", 0))
        userType.add(UserTypeModel("Children", 0))
        userType.add(UserTypeModel("Infants", 0))

        userType.get(0).setSelectedPsngrCount(Integer.parseInt(adultNumber))
        userType.get(1).setSelectedPsngrCount(Integer.parseInt(kidNumber))
        userType.get(2).setSelectedPsngrCount(Integer.parseInt(infantNumber))

        val customAdapter = CustomAdapter(context, userType, mListenerPsngr)
        listView.adapter = customAdapter

        return view
    }

    interface PsngrBottomSheetListener {
        fun onAdultButtonClickListener(text: String)

        fun onKidButtonClickListener(text: String)

        fun onInfantButtonClickListener(text: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            if (context is PsngrBottomSheetListener)
                mListenerPsngr = context
            else {
                throw ClassCastException(context.toString()
                        + " must implement PsngrBottomSheetListener")
            }
        } catch (ex: ClassCastException) {
            throw ClassCastException(context.toString()
                    + " must implement PsngrBottomSheetListener")
        }
    }

    class CustomAdapter : BaseAdapter {

        var context: Context? = null
        var userList: List<UserTypeModel>
        var mListenerPsngr: PsngrBottomSheetListener

        constructor(context: Context?, userList: List<UserTypeModel>, listener: PsngrBottomSheetListener) {
            this.context = context
            this.userList = userList
            this.mListenerPsngr = listener
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = inflater.inflate(R.layout.psngr_list_view_item, parent, false)
            val holder = ViewHolder()
            holder.tvUserType = view.findViewById(R.id.tv_user_type)
            holder.ivMinus = view.findViewById(R.id.iv_minus)
            holder.tvUserCount = view.findViewById(R.id.tv_user_count)
            holder.ivPlus = view.findViewById(R.id.iv_plus)

            val model = userList.get(position)

            holder.tvUserType.setText(model.getSelectedUserType())
            holder.tvUserCount.setText("" + model.getSelectedPsngrCount())

            holder.ivMinus.setOnClickListener(View.OnClickListener {
                val count = model.getSelectedPsngrCount()
                if (count?.compareTo(0) == 1) {
                    val data = model.getSelectedPsngrCount()!!.minus(1)
                    model.setSelectedPsngrCount(data)

                    updateRecords(userList)
                    if (position.and(0) == 0)
                        mListenerPsngr.onAdultButtonClickListener("" + model.getSelectedPsngrCount())
                    if (position.and(1) == 0)
                        mListenerPsngr.onKidButtonClickListener("" + model.getSelectedPsngrCount())
                    if (position.and(2) == 0)
                        mListenerPsngr.onInfantButtonClickListener("" + model.getSelectedPsngrCount())
                }
            })

            holder.ivPlus.setOnClickListener(View.OnClickListener {
                val data = model.getSelectedPsngrCount()!!.plus(1)
                model.setSelectedPsngrCount(data)

                updateRecords(userList)
                Log.e("logtag", "" + position)
                if (position.and(0) == 0)
                    mListenerPsngr.onAdultButtonClickListener("" + model.getSelectedPsngrCount())
                if (position.and(1) == 0)
                    mListenerPsngr.onKidButtonClickListener("" + model.getSelectedPsngrCount())
                if (position.and(2) == 0)
                    mListenerPsngr.onInfantButtonClickListener("" + model.getSelectedPsngrCount())
            })
            return view
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return userList.size
        }

        fun updateRecords(user: List<UserTypeModel>) {
            this.userList = user

            notifyDataSetChanged()
        }

        class ViewHolder {
            lateinit var tvUserType: TextView
            lateinit var ivMinus: ImageView
            lateinit var tvUserCount: TextView
            lateinit var ivPlus: ImageView
        }
    }
}
