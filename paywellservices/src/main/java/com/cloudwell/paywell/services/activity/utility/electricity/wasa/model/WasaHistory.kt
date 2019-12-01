package com.cloudwell.paywell.services.activity.utility.electricity.wasa.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * Created by Yasin Hosain on 12/1/2019.
 * yasinenubd5@gmail.com
 */
@Entity(indices = [Index(value = ["bill_number","payer_phone_number"], unique = true)])
class WasaHistory(@PrimaryKey(autoGenerate = true)
                  @ColumnInfo(name = "Id")
                  var id: Long = 0,
                  @ColumnInfo(name = "bill_number")
                  var bilNumber: String = "",

                  @ColumnInfo(name = "payer_phone_number")
                  var payerPhoneNumber: String = "",

                  @ColumnInfo(name = "date")
                  var date: String = "")