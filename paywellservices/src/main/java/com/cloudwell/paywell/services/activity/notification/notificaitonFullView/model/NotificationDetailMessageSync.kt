package com.cloudwell.paywell.services.activity.notification.notificaitonFullView.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 10/2/19.
 */
@Entity
class NotificationDetailMessageSync(
        @PrimaryKey
        @ColumnInfo(name = "message_id")
        @SerializedName("message_id")
        var messageId: String

)
