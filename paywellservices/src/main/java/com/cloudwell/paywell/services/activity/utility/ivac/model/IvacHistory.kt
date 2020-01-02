package com.cloudwell.paywell.services.activity.utility.ivac.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * Created by Yasin Hosain on 12/1/2019.
 * yasinenubd5@gmail.com
 */
@Entity(indices = [Index(value = ["payer_phone_number"], unique = true)])
class IvacHistory(@PrimaryKey(autoGenerate = true)
                @ColumnInfo(name = "Id")
                var id: Long = 0,

                  @ColumnInfo(name = "payer_phone_number")
                var payerPhoneNumber: String = "",

                  @ColumnInfo(name = "date")
                var date: String = "")