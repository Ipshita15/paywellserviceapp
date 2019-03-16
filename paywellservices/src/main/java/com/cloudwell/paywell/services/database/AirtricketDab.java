package com.cloudwell.paywell.services.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger;
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.model.Airport;

import java.util.List;


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
@Dao
public interface AirtricketDab {

    @Query("SELECT * FROM passenger ")
    List<Passenger> getAll();

    @Query("SELECT * FROM passenger WHERE Passenger.Id IN (:ids)")
    List<Passenger> getAll(List<String> ids);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Passenger passenger);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertRecentAirport(Airport airport);

    @Update
    int update(Passenger passenger);


    @Delete
    int deleted(Passenger passenger);


    @Query("SELECT * FROM Airport WHERE status ='recent'")
    List<Airport> getRecentSearches();


}
