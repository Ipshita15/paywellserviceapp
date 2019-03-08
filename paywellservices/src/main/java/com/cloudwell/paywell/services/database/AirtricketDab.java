package com.cloudwell.paywell.services.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger;

import java.util.List;


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
@Dao
public interface AirtricketDab {

    @Query("SELECT * FROM passenger ")
    List<Passenger> getAll();

//    @Query("SELECT * FROM favoritemenu WHERE status ='unFavourite'")
//    Maybe<List<FavoriteMenu>> getAllUnFavoriteMenu();
//
//
//    @Query("SELECT * FROM favoritemenu WHERE status ='Favourite'")
//    Maybe<List<FavoriteMenu>> getAllFavoriteMenu();
//
//    @Insert
//    void insert(FavoriteMenu task);
//
//    @Insert
//    void insert(List<FavoriteMenu> task);
//
//    @Delete
//    void delete(FavoriteMenu task);
//
//    @Update
//    int update(FavoriteMenu task);
//
//    @Query("DELETE FROM favoritemenu")
//    public void deletedALl();
}
