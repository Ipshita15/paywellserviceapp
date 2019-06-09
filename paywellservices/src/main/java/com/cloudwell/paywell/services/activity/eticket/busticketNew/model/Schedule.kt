package com.cloudwell.paywell.services.activity.eticket.busticketNew.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-09.
 */

@Entity
class Schedule(

        @ColumnInfo(name = "schedule_time_id")
        var schedule_time_id: String = "",

        @ColumnInfo(name = "schedule_time")
        var scheduleTime: String = "",

        @ColumnInfo(name = "bus_id")
        var busId: String = "",

        @ColumnInfo(name = "coach_no")
        var coachNo: String = "",

        @ColumnInfo(name = "schedule_type")
        var scheduleType: String = "",

        @ColumnInfo(name = "validity_date")
        var validityDate: String = "",

        @ColumnInfo(name = "ticket_price")
        var ticketPrice: String = "",

        @ColumnInfo(name = "ticket_price_validity_date")
        var ticketPriceValidityDate: String = "",

        @ColumnInfo(name = "allowed_seat_numbers")
        var allowedSeatNumbers: String = ""

) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Long = 0
}