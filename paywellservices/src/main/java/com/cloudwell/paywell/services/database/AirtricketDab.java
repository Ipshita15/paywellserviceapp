package com.cloudwell.paywell.services.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger;

import java.util.List;


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
@Dao
public interface AirtricketDab {

    @Query("SELECT * FROM passenger ")
    List<Passenger> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Passenger passenger);

    @Update
    int update(Passenger task);

}
