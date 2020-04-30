package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.adapter.BusTripListAdapter
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.adapter.OnClickListener
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel.BusTransportViewModel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResPaymentBookingAPI
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResSeatCheckBookAPI
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResSeatInfo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.RequestScheduledata
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.scheduledata.ScheduleDataItem
import kotlinx.android.synthetic.main.fragment_transport_list.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TransportListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class TransportListFragment(val requestScheduledata: RequestScheduledata) : Fragment(), IbusTransportListView {
    private lateinit var busTripListAdapter: BusTripListAdapter
    private lateinit var viewMode: BusTransportViewModel
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transport_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
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

        fun onItemCLick(position: Int)
    }




    override fun showNoTripFoundUI() {
        shimmer_recycler_view.visibility = View.INVISIBLE
        layoutNoSerachFound.visibility = View.VISIBLE
    }

    override fun showErrorMessage(message: String) {

    }

    override fun showSeatCheckAndBookingRepose(it: ResSeatCheckBookAPI) {
    }

    override fun showShowConfirmDialog(it: ResPaymentBookingAPI) {
    }

    override fun setBoardingPoint(allBoothNameInfo: MutableSet<String>) {
    }

    override fun saveRequestScheduledata(p: RequestScheduledata) {
    }

    override fun saveExtraCharge(double: Double) {

    }

    fun setAdapter(data: List<ScheduleDataItem>) {
        layoutNoSerachFound.visibility = View.INVISIBLE
       // viewMode.cancelAllRequest()

       // Fresco.initialize(applicationContext);


        shimmer_recycler_view.visibility = View.VISIBLE
        shimmer_recycler_view.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
        shimmer_recycler_view.adapter = data.let { it1 ->



             busTripListAdapter = BusTripListAdapter(data, activity!!.applicationContext, requestScheduledata, viewMode.extraCharge.value?:0.0, object : OnClickListener {
                override fun onUpdateData(position: Int, resSeatInfo: ResSeatInfo) {

                    val m = viewMode.mutableBusLiveDataList.value?.get(position)
                    m?.resSeatInfo = resSeatInfo
                    m?.let {
                        viewMode.mutableBusLiveDataList.value?.set(position, it)
                        busTripListAdapter.notifyItemRangeChanged(position, viewMode.mutableBusLiveDataList.value!!.size);
                        busTripListAdapter.notifyDataSetChanged()

                    }

                }


                override fun onClick(position: Int) {
                    // do whatever
                    listener?.onItemCLick(position)

                }
            })
            busTripListAdapter
        }
    }


    override fun showProgress() {
        progressBar2.visibility = View.VISIBLE
        shimmer_recycler_view.showShimmerAdapter()

    }

    override fun hiddenProgress() {
        progressBar2.visibility = View.GONE
        shimmer_recycler_view.hideShimmerAdapter()
    }

    override fun showNoInternetConnectionFound() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun initViewModel() {

        viewMode = ViewModelProviders.of(activity!!).get(BusTransportViewModel::class.java)
        viewMode.setIbusTransportListView(this)
        viewMode.search(requestScheduledata)
        viewMode.mutableBusLiveDataList.observe(this, object : Observer<List<ScheduleDataItem>> {
            override fun onChanged(t: List<ScheduleDataItem>?) {

                t?.let { setAdapter(it) };

            }

        });


    }


}
