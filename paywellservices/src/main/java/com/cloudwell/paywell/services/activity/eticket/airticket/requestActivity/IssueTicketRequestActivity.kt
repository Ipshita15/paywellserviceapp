package com.cloudwell.paywell.services.activity.eticket.airticket.requestActivity

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.base.TransitionLogBaseActivity
import com.cloudwell.paywell.services.constant.AllConstant
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_issue_ticket_request.*

class IssueTicketRequestActivity : TransitionLogBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_issue_ticket_request)

        setToolbar(getString(R.string.title_issue_ticket))

        cancelBookingBtn.setOnClickListener(View.OnClickListener {
            if (!bookingIdET.getText().toString().isEmpty()) {
                askForPin(bookingIdET.getText().toString(), AllConstant.Action_IsisThicket)
            } else {
                hideUserKeyboard()
                val snackbar = Snackbar.make(cancelMainLayout, "Please Enter all the fields first", Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }
}