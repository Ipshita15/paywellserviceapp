package com.cloudwell.paywell.services.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BoothInfo;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Bus;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BusLocalDB;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Schedule;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfo;

import java.util.List;


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
@Dao
public interface BusTicketDab {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<Bus> passenger);


    @Query("DELETE FROM Bus")
    public void clearBus();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertLocalBus(List<BusLocalDB> busLocalDBS);

    @Query("DELETE FROM BusLocalDB")
    public void clearLocalBusDB();


    @Query("DELETE FROM Schedule")
    public void clearSchedule();


    @Query("DELETE FROM TripScheduleInfo")
    public void clearTripScheduleInfo();

    @Query("DELETE FROM BoothInfo")
    public void clearBoothInfo();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertSchedule(List<Schedule> all);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertBoothInfo(List<BoothInfo> all);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTripScheduleInfo(List<TripScheduleInfo> all);

    @Query("SELECT * FROM  TripScheduleInfo where to_location = :toLocation   AND from_location = :fromLocation;")
    public List<TripScheduleInfo> searchTrip(String toLocation, String fromLocation);
}
