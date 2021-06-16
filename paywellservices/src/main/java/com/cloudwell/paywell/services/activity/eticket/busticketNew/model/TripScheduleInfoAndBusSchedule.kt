package com.cloudwell.paywell.services.activity.eticket.busticketNew.model


import androidx.room.Embedded
import androidx.room.Ignore


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-20.
 */
data class TripScheduleInfoAndBusSchedule(@Embedded
                                          var tripScheduleInfo: TripScheduleInfo? = null,

                                          @Embedded
                                          var busSchedule: BusSchedule? = null,

                                          @Embedded
                                          var busLocalDB: BusLocalDB? = null


) {


    @Ignore
    var resSeatInfo: ResSeatInfo? = null

}