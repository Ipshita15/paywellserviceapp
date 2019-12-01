package com.cloudwell.paywell.services.database;

import com.cloudwell.paywell.services.activity.utility.electricity.desco.model.DESCOHistory;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.model.DPDCHistory;
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.model.WasaHistory;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */

@Dao
public interface UtilityDab {

    @Query("SELECT * FROM  DESCOHistory")
    List<DESCOHistory> getAllDESCOHistory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DESCOHistory descoHistory);

    @Query("SELECT * FROM  DPDCHistory")
    List<DPDCHistory> getAllDPDCHistory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDPDCHistory(DPDCHistory dpdcHistory);

    @Query("SELECT * FROM  WasaHistory")
    List<WasaHistory> getAllWasaHistory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWasaHistory(WasaHistory wasaHistory);

}
