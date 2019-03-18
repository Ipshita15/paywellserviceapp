package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.viewModel.view

import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.ResAirPreBooking

data class AllSummaryStatus(val noSerachFoundMessage: String, val isShowProcessIndicatior: Boolean, var resAirPreBooking: ResAirPreBooking? = null, var isBooking: Boolean = false) {


}
