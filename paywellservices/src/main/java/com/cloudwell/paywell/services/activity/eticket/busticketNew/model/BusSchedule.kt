package com.cloudwell.paywell.services.activity.eticket.busticketNew.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-09.
 */

@Entity
class BusSchedule(
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "_schedule_Id")
        var schedule_time_id: String = "",

        @ColumnInfo(name = "schedule_time")
        var scheduleTime: String = "",

        @ColumnInfo(name = "bus_id")
        var busId: String = "",

        @ColumnInfo(name = "coach_no")
        var coachNo: String = "",

        @ColumnInfo(name = "schedule_type")
        var scheduleType: String = "",

        @ColumnInfo(name = "_validity_date")
        var validityDate: String = "",

        @ColumnInfo(name = "ticket_price")
        var ticketPrice: String = "",

        @ColumnInfo(name = "allowed_seat_numbers")
        var allowedSeatNumbers: String = ""

)