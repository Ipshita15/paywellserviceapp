package com.cloudwell.paywell.services.activity.utility.electricity.desco.postpaid.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-11-25.
 */
@Entity(indices = [Index(value = ["bill_number","payer_phone_number","location"], unique = true)])
public class DPDCHistory {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Long = 0

    @ColumnInfo(name = "bill_number")
    var bilNumber: String = ""

    @ColumnInfo(name = "payer_phone_number")
    var payerPhoneNumber: String = ""

    @ColumnInfo(name = "location")
    var location: String = ""

    @ColumnInfo(name = "date")
    var date: String = ""

}