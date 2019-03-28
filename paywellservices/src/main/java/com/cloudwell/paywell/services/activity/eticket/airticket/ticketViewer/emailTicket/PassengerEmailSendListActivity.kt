package com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.emailTicket

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.EmailAddFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.emailTicket.adapter.EmailListAdapterAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.model.EmailListViewStatus
import com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer.viewModel.EmailListViewModel
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.activity_ticket_email.*


class PassengerEmailSendListActivity : AirTricketBaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btSendEmail -> {
                val filterList = allPassegnerMutableList.filter {
                    it.isDefalt == false
                }

                var isItemSeleted = false
                var emailString = ""

                filterList.forEach {
                    if (it.isCheckEmail) {
                        isItemSeleted = true
                        emailString = emailString + it.email + ","
                    } else {
                        if (!isItemSeleted == true) {
                            isItemSeleted = false
                        }
                    }
                }

                if (!isItemSeleted) {
                    showSnackMessageWithTextMessage(getString(R.string.please_select_atleast_one_email_address))
                } else {
                    emailString = emailString.substring(0, emailString.lastIndexOf(","))

                    callSendInvoideAPI(emailString)
                }


            }
        }
    }


    lateinit var datum: Datum
    var allPassegnerMutableList = mutableListOf<Passenger>()
    lateinit var customAdapter: EmailListAdapterAdapter
    lateinit var viewMode: EmailListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_ticket_email)
        setToolbar(getString(com.cloudwell.paywell.services.R.string.title_email_list))

        datum = AppStorageBox.get(applicationContext, AppStorageBox.Key.BOOKING_STATUS_ITEM) as Datum

        initilization()

        initViewModel()


    }


    private fun initilization() {

        datum.passengers.forEach {
            it.isCheckEmail = true
        }

        val p = Passenger()
        p.setDefault(true)
        allPassegnerMutableList = datum.passengers.toMutableList()
        allPassegnerMutableList.add(p)


        setupAdapter()

        btSendEmail.setOnClickListener(this)


    }

    private fun initViewModel() {
        viewMode = ViewModelProviders.of(this).get(EmailListViewModel::class.java)

        viewMode.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewCommonStatus(it)
        })

        viewMode.mViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewStatus(it)
        })

    }

    private fun handleViewStatus(it: EmailListViewStatus?) {
        it.let {
            if (it!!.isShowProcessIndicatior) {
                showProgressDialog()
            } else {
                dismissProgressDialog()
            }

            if (!it.successMessage.equals("")) {

                showMsg(it.successMessage)
            }

            if (!it.noSerachFoundMessage.equals("")) {
                showSnackMessageWithTextMessage(it.noSerachFoundMessage)
            }
        }

    }

    private fun showMsg(msg: String) {
        val builder = android.support.v7.app.AlertDialog.Builder(this)
        builder.setTitle(R.string.message)
        builder.setMessage(msg)
        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id -> finish() }
        val alert = builder.create()
        alert.show()
    }

    private fun setupAdapter() {

        customAdapter = EmailListAdapterAdapter(allPassegnerMutableList, this.applicationContext, object : EmailListAdapterAdapter.ItemClickListener {
            override fun onStateChange(position: Int, b: Boolean) {
                val get = allPassegnerMutableList.get(position)
                get.isCheckEmail = b
                allPassegnerMutableList.set(position, get)

            }

            override fun onAddClick() {
                showEmailDialog()
            }
        })

        emailList.adapter = customAdapter
        val mLayoutManager = LinearLayoutManager(applicationContext)
        emailList.setLayoutManager(mLayoutManager)
        emailList.setItemAnimator(DefaultItemAnimator())
    }

    private fun showEmailDialog() {
        val tricketChooserFragment = EmailAddFragment()
        tricketChooserFragment.setOnClickHandlerTest(object : EmailAddFragment.OnClickHandler {
            override fun onClick(s: String, emailAddress: String) {
                if (s.equals("email")) {

                    val find = allPassegnerMutableList.find {
                        it.email.equals(emailAddress)
                    }

                    if (find != null) {
                        showSnackMessageWithTextMessage(getString(R.string.email_address_already_exists))
                        return
                    }


                    val lastItem = allPassegnerMutableList.get(allPassegnerMutableList.lastIndex)
                    allPassegnerMutableList.removeAt(allPassegnerMutableList.lastIndex)

                    val p = Passenger()
                    p.email = emailAddress
                    p.isCheckEmail = true

                    allPassegnerMutableList.add(p)
                    allPassegnerMutableList.add(lastItem)

                    setupAdapter()


                }

            }

        })

        tricketChooserFragment.show(supportFragmentManager, "dialog")
    }

    private fun callSendInvoideAPI(emailString: String) {

        viewMode.callSendInvoiceAPI(datum.bookingId, emailString, isInternetConnection)

    }


}


