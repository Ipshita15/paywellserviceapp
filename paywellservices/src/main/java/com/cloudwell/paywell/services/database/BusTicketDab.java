package com.cloudwell.paywell.services.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BusLocalDB;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BusSchedule;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Transport;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfo;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfoAndBusSchedule;

import java.util.List;


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
@Dao
public interface BusTicketDab {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<Transport> passenger);


    @Query("DELETE FROM Transport")
    public void clearBus();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertLocalBus(List<BusLocalDB> busLocalDBS);

    @Query("DELETE FROM BusLocalDB")
    public void clearLocalBusDB();


    @Query("DELETE FROM BusSchedule")
    public void clearSchedule();


    @Query("DELETE FROM TripScheduleInfo")
    public void clearTripScheduleInfo();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertSchedule(List<BusSchedule> all);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTripScheduleInfo(List<TripScheduleInfo> all);


    @Query("SELECT BusSchedule.*, BusLocalDB.* FROM BusSchedule INNER JOIN TripScheduleInfo ON BusSchedule._schedule_Id = TripScheduleInfo.schedule_Id INNER JOIN BusLocalDB ON BusLocalDB.busID = BusSchedule.bus_id Where TripScheduleInfo.to_location = :to AND TripScheduleInfo.from_location = :from  AND TripScheduleInfo.validity_date >= :date")
    List<TripScheduleInfoAndBusSchedule> search(String to, String from, String date);

    @Query("SELECT from_location as city_list FROM TripScheduleInfo UNION " +
            "SELECT to_location as city_list FROM TripScheduleInfo ;")
    public List<String> searchAvailableCityForBus();
}
