package com.cloudwell.paywell.services.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Bus;

import java.util.List;


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
@Dao
public interface BusTicketDab {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<Bus> passenger);


}
