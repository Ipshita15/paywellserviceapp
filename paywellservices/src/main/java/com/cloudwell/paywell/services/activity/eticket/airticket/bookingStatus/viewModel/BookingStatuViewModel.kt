package com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.viewModel

import com.cloudwell.paywell.services.activity.base.newBase.BaseViewState
import com.cloudwell.paywell.services.activity.base.newBase.SingleLiveEvent
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketBaseViewMode
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model.BookingStatuViewStatus

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 28/3/19.
 */
class BookingStatuViewModel : AirTicketBaseViewMode() {

    var responseList = SingleLiveEvent<BookingList>()
    val mViewStatus = SingleLiveEvent<BookingStatuViewStatus>()

    fun getBookingStatus(internetConnection: Boolean, limit: Int) {
        if (!internetConnection) {
            baseViewStatus.value = BaseViewState(isNoInternectConnectionFoud = true)
        } else {

            mViewStatus.value = BookingStatuViewStatus(isShowProcessIndicatior = true)

            mAirTicketRepository.callGetBookingStatusAPI(limit).observeForever {
                mViewStatus.value = BookingStatuViewStatus(isShowProcessIndicatior = false)

                val checkNetworkAndStatusCode = isOkNetworkAndStatusCode(it)
                if (checkNetworkAndStatusCode) {
                    mViewStatus.value = BookingStatuViewStatus(isShowProcessIndicatior = false)
                    responseList.value = it
                }

            }
        }
    }

    fun isOkNetworkAndStatusCode(it: BookingList?): Boolean {
        it?.let {
            if (it.throwable != null) {
                baseViewStatus.value = it.throwable!!.message.let { it1 ->
                    BaseViewState(errorMessage = it1.toString())
                }

                val bookingStatuViewStatus = BookingStatuViewStatus(isShowProcessIndicatior = false)


                mViewStatus.value = bookingStatuViewStatus

                return false
            } else if (it.status == 200) {
                return true
            }

        }
        return false
    }

}