package com.cloudwell.paywell.services.database;

import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.Airport;
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


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


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAirportList(List<Airport> list);

    @Query("SELECT * FROM Airport ")
    List<Airport> getAirport();


    @Query("SELECT * FROM Airport WHERE iso = (:iso)")
    List<Airport> getAirport(String iso);


    @Query("SELECT * FROM Airport WHERE iata = (:iata)")
    Airport getAirportBy(String iata);


    @Query("DELETE FROM AIRPORT")
    void clearAirportsData();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertRecentAirport(Airport airport);

    @Update
    int update(Passenger passenger);


    @Delete
    int deleted(Passenger passenger);


    @Query("SELECT * FROM Airport WHERE status ='recent'")
    List<Airport> getRecentSearches();


}
