package com.cloudwell.paywell.services.activity.eticket.airticket


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.cloudwell.paywell.services.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PassengerBottomSheetDialog : BottomSheetDialogFragment() {

    lateinit var mListenerPsngr: PsngrBottomSheetListener
    lateinit var userType: ArrayList<UserTypeModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val adultNumber: String? = arguments?.getString("myAdult")
        val kidNumber: String? = arguments?.getString("myKid")
        val infantNumber: String? = arguments?.getString("myInfant")

        val view = inflater.inflate(R.layout.fragment_passenger_bottom_sheet_dialog, container, false)

        val listView = view.findViewById(R.id.listViewAirTicketPassengers) as ListView
        val ivClose = view.findViewById<ImageView>(R.id.btnCloseAirTicket)
        val tvDone = view.findViewById<TextView>(R.id.tvDone)

        userType = ArrayList()
        userType.add(UserTypeModel("Adults", 0))
        userType.add(UserTypeModel("Children", 0))
        userType.add(UserTypeModel("Infants", 0))

        userType.get(0).setSelectedPsngrCount(Integer.parseInt(adultNumber.toString()))
        userType.get(1).setSelectedPsngrCount(Integer.parseInt(kidNumber.toString()))
        userType.get(2).setSelectedPsngrCount(Integer.parseInt(infantNumber.toString()))

        val customAdapter = CustomAdapter(context, userType, mListenerPsngr)
        listView.adapter = customAdapter


        ivClose.setOnClickListener {
            dismiss()
        }
        tvDone.setOnClickListener {
            mListenerPsngr.onAdultButtonClickListener("" + userType.get(0).getSelectedPsngrCount())
            mListenerPsngr.onKidButtonClickListener("" + userType.get(1).getSelectedPsngrCount())
            mListenerPsngr.onInfantButtonClickListener("" + userType.get(2).getSelectedPsngrCount())
            dismiss()
        }

        return view
    }

    fun setmListenerPsngr(psngrBottomSheetListener: PsngrBottomSheetListener) {
        mListenerPsngr = psngrBottomSheetListener
    }

    interface PsngrBottomSheetListener {
        fun onAdultButtonClickListener(text: String)

        fun onKidButtonClickListener(text: String)

        fun onInfantButtonClickListener(text: String)
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


                if (position == 0) {
                    if (model.getSelectedPsngrCount() <= 1) {

                    } else {
                        val data = model.getSelectedPsngrCount().minus(1)
                        model.setSelectedPsngrCount(data)

                        updateRecords(userList)
                    }
                } else {
                    if (count.compareTo(0) == 1) {
                        val data = model.getSelectedPsngrCount().minus(1)
                        model.setSelectedPsngrCount(data)

                        updateRecords(userList)
                    }

                }
            })

            holder.ivPlus.setOnClickListener(View.OnClickListener {
                val data = model.getSelectedPsngrCount().plus(1)
                model.setSelectedPsngrCount(data)

                updateRecords(userList)
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

    override fun onDestroy() {

        mListenerPsngr.onAdultButtonClickListener("" + userType.get(0).getSelectedPsngrCount())
        mListenerPsngr.onKidButtonClickListener("" + userType.get(1).getSelectedPsngrCount())
        mListenerPsngr.onInfantButtonClickListener("" + userType.get(2).getSelectedPsngrCount())

        super.onDestroy()
    }
}
