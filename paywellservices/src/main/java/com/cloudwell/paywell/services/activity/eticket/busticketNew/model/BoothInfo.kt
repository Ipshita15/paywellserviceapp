package com.cloudwell.paywell.services.activity.eticket.busticketNew.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-09.
 */

@Entity
class BoothInfo(
        @ColumnInfo(name = "boothID")
        var boothID: String = "",

        @ColumnInfo(name = "schedule_time_id")
        var schedule_time_id: String = "",

        @ColumnInfo(name = "booth_name")
        var boothName: String = "",


        @ColumnInfo(name = "booth_departure_time")
        var boothDepartureTime: String = ""


) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Long = 0
}