package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * Created by Yasin Hosain on 12/1/2019.
 * yasinenubd5@gmail.com
 */
@Entity(indices = [Index(value = ["bill_number"], unique = true)])
class PallibidyutHistory(@PrimaryKey(autoGenerate = true)
                       @ColumnInfo(name = "Id")
                       var id: Long = 0,
                         @ColumnInfo(name = "bill_number")
                       var bilNumber: String = "",

                         @ColumnInfo(name = "date")
                       var date: String = "")