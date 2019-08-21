package com.cloudwell.paywell.services.activity.myFavorite.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
@Entity
class FavoriteMenu(
        @ColumnInfo(name = "name")
        var name: String,

        @ColumnInfo(name = "category")
        var category: String,

        @ColumnInfo(name = "icon")
        var icon: String,

        @ColumnInfo(name = "status")
        var status: String,

        @ColumnInfo(name = "favorite_list_position")
        var favoriteListPosition: Int,

        @ColumnInfo(name = "alias_key")
        var alias: Int

) {


    @PrimaryKey(autoGenerate = true)
    var id: Int = 0 // or foodId: Int? = null


}

