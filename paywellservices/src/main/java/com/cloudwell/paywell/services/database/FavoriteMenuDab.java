package com.cloudwell.paywell.services.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu;

import java.util.List;

import io.reactivex.Maybe;


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
@Dao
public interface FavoriteMenuDab {

    @Query("SELECT * FROM favoritemenu ")
    List<FavoriteMenu> getAll();

    @Query("SELECT * FROM favoritemenu WHERE status ='unFavourite'")
    Maybe<List<FavoriteMenu>> getAllUnFavoriteMenu();

    @Insert
    void insert(FavoriteMenu task);

    @Insert
    void insert(List<FavoriteMenu> task);

    @Delete
    void delete(FavoriteMenu task);

    @Update
    int update(FavoriteMenu task);

    @Query("DELETE FROM favoritemenu")
    public void deletedALl();
}
