package com.cloudwell.paywell.services.activity.myFavorite.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
@Entity
class FavoriteMenu(
        @ColumnInfo(name = "name")
        var name: Int,

        @ColumnInfo(name = "category")
        var category: Int,

        @ColumnInfo(name = "icon")
        var icon: Int,

        @ColumnInfo(name = "status")
        var status: String

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0 // or foodId: Int? = null
}

