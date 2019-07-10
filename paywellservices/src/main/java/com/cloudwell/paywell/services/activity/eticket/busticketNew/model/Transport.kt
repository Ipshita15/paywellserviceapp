package com.cloudwell.paywell.services.activity.eticket.busticketNew.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

import com.google.gson.annotations.SerializedName


@Entity
class Transport {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Long = 0

    @ColumnInfo(name = "busid")
    @SerializedName("busid")
    var busid: String = ""

    @ColumnInfo(name = "busname")
    @SerializedName("busname")
    var busname: String = ""

    @ColumnInfo(name = "extraCharge")
    @SerializedName("extraCharge")
    var extraCharge: Double = 0.0

}
