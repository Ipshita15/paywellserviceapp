package com.cloudwell.paywell.services.activity.notification.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity
class NotificationDetailMessage(

        @SerializedName("added_datetime")
        @ColumnInfo(name = "added_datetime")
        var addedDatetime: String?,

        @ColumnInfo(name = "balance_return_data")
        @SerializedName("balance_return_data")
        var balanceReturnData: String?,

        @ColumnInfo(name = "image_url")
        @SerializedName("image_url")
        var imageUrl: String?,


        @ColumnInfo(name = "message")
        @SerializedName("message")
        var message: String?,

        @PrimaryKey
        @ColumnInfo(name = "message_id")
        @SerializedName("message_id")
        var messageId: String,

        @ColumnInfo(name = "message_sub")
        @SerializedName("message_sub")
        var messageSub: String?,

        @ColumnInfo(name = "status")
        @SerializedName("status")
        var status: String?,

        @ColumnInfo(name = "type")
        @SerializedName("type")
        var type: String?,

        @ColumnInfo(name = "message_expiry_time")
        @SerializedName("message_expiry_time")
        var messageExpiryTime: String?

)


