package com.cloudwell.paywell.services.activity.eticket.busticketNew.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-09.
 */

@Entity
class TripScheduleInfo(
        @ColumnInfo(name = "to_location")
        var To: String = "",

        @ColumnInfo(name = "from_location")
        var from: String = "",

        @ColumnInfo(name = "schedule_Id")
        var coachNo: String = ""


) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Long = 0
}