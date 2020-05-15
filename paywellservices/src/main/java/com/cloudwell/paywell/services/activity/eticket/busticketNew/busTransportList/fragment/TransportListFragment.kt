package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.BusHosttActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.adapter.BusTripListAdapter
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.adapter.OnClickListener
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.model.SearchFilter
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel.BusTransportViewModel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResSeatCheckBookAPI
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResSeatInfo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.RequestScheduledata
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.scheduledata.ScheduleDataItem
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm.ResposeTicketConfirm
import kotlinx.android.synthetic.main.bus_advance_filter.view.*
import kotlinx.android.synthetic.main.bus_advance_filter.view.radioGroupJounryType
import kotlinx.android.synthetic.main.fragment_transport_list.*
import kotlinx.android.synthetic.main.layout_filter.view.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TransportListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class TransportListFragment(val requestScheduledata: RequestScheduledata, val isRetrunTriple: Boolean) : Fragment(), IbusTransportListView {
    private lateinit var adapter: BusTripListAdapter
    private lateinit var viewMode: BusTransportViewModel
    private var listener: OnFragmentInteractionListener? = null
    lateinit var layoutManager: CustomGridLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_transport_list, container, false)

        initilization(view)

        return view
    }

    private fun initilization(view: View) {
        view.ivFilterUpDown.setImageResource(0)
        view.ivFilterUpDown.setImageResource(R.drawable.icon_down)

        view.llCustomFilterHeader.setOnClickListener {
            hiddenAndShowFilter(view)
        }

        view.ivUp.setOnClickListener {
            hiddenAndShowFilter(view)
        }

        view.viewSearch.setOnClickListener {
            hiddenAndShowFilter(view)

            val m = SearchFilter()

            val checkedRadioButtonId = view.radioGroupJounyTime.checkedRadioButtonId
            val departingTime = when (checkedRadioButtonId) {
                R.id.radioJourneyTimeAll -> "Any"
                R.id.radioJourneyTimeAllMorning -> "Morning"
                R.id.radioJourneyTimeAllEvening -> "Evening"
                R.id.radioJourneyTimeAfterNoon -> "AfterNoon"
                R.id.radioJourneyTimeAllNight -> "Night"

                else -> "Any"
            }
            m.departingTime = departingTime


            val id = view.radioGroupJounryType.checkedRadioButtonId
            val coachType = when (id) {
                R.id.radioBtmAll -> "All"
                R.id.radioBtmAC -> "Ac"
                R.id.radioBtmNonAC -> "NonAc"
                else -> "All"
            }
            m.coachType = coachType


            val radioGroupSortByid = view.radioGroupSortBy.checkedRadioButtonId
            val sortBy = when (radioGroupSortByid) {
                R.id.radioLowPrice -> "Low Price"
                R.id.radioHighPrice -> "HighPrice"
                else -> "Low Price"
            }
            m.sortBy = sortBy
            viewMode.onSort(m, isRetrunTriple)


        }
    }

    private fun hiddenAndShowFilter(view: View) {

        var size = 0
        if (!isRetrunTriple) {
            size = viewMode.singleTripTranportListMutableLiveData.value?.size ?: 0
        } else {
            size = viewMode.returnTripTransportListMutableLiveData.value?.size ?: 0
        }
        if (size > 0) {

            if (view.llAdvaceSerach.visibility == View.VISIBLE) {
                view.llAdvaceSerach.visibility = View.GONE
                layoutManager.isScrollEnabled = true
                view.ivFilterUpDown.setImageResource(0)
                view.ivFilterUpDown.setImageResource(R.drawable.icon_down)
            } else {
                view.llAdvaceSerach.visibility = View.VISIBLE
                layoutManager.isScrollEnabled = false
                view.ivFilterUpDown.setImageResource(0)
                view.ivFilterUpDown.setImageResource(R.drawable.ic_up)
            }
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
    }

    override fun onResume() {
        super.onResume()

        val busHosttActivity = activity as BusHosttActivity
        if (!isRetrunTriple) {
            busHosttActivity.setToolbar("Departure Ticket", resources.getColor(R.color.bus_ticket_toolbar_title_text_color))
        } else {
            busHosttActivity.setToolbar("Return Ticket", resources.getColor(R.color.bus_ticket_toolbar_title_text_color))
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)

        fun onItemCLick(position: Int, retrunTriple: Boolean)
    }




    override fun showNoTripFoundUI() {
        shimmer_recycler_view.visibility = View.INVISIBLE
        layoutNoSerachFound.visibility = View.VISIBLE
    }

    override fun showErrorMessage(message: String) {

    }

    override fun showSeatCheckAndBookingRepose(it: ResSeatCheckBookAPI) {
    }

    override fun showShowConfirmDialog(it: ResposeTicketConfirm) {
    }

    override fun setBoardingPoint(allBoothNameInfo: MutableSet<String>) {
    }

    override fun saveRequestScheduledata(p: RequestScheduledata) {
    }

    override fun saveExtraCharge(double: Double) {

    }

    override fun showFilterList(filterTypeDepartingTime: List<ScheduleDataItem>) {
        setAdapter(filterTypeDepartingTime)

    }

    override fun updateListData(position: Int, itemCount: Int) {
        adapter.notifyItemChanged(position)
        adapter.notifyItemRangeChanged(position, itemCount)

    }

    fun setAdapter(data: List<ScheduleDataItem>) {
        layoutNoSerachFound.visibility = View.INVISIBLE

        shimmer_recycler_view.visibility = View.VISIBLE
        layoutManager = CustomGridLayoutManager(activity, true)
        shimmer_recycler_view.layoutManager = layoutManager
        shimmer_recycler_view.adapter = data.let { it1 ->

            adapter = BusTripListAdapter(data, activity!!.applicationContext, viewMode.extraCharge.value
                    ?: 0.0, object : OnClickListener {
                override fun onUpdateData(position: Int, resSeatInfo: ResSeatInfo) {


                }

                override fun needUpdateData(position: Int, model: ScheduleDataItem) {
                    viewMode.needToUpdateData(position, model, isRetrunTriple)
                }

                override fun onClick(position: Int) {
                    // do whatever
                    listener?.onItemCLick(position, isRetrunTriple)

                }
            })
            adapter
        }

    }


    override fun showProgress() {
        shimmer_recycler_view.showShimmerAdapter()

    }

    override fun hiddenProgress() {
        shimmer_recycler_view.hideShimmerAdapter()
    }

    override fun showNoInternetConnectionFound() {

    }


    private fun initViewModel() {

        viewMode = ViewModelProviders.of(activity!!).get(BusTransportViewModel::class.java)
        viewMode.setIbusTransportListView(this)
        viewMode.init(requestScheduledata, isRetrunTriple)


    }

    class CustomGridLayoutManager(context: Context?, var isScrollEnabled: Boolean) : LinearLayoutManager(context) {

        override fun canScrollVertically(): Boolean {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically()
        }
    }


}
