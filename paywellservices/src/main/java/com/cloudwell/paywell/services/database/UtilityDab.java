package com.cloudwell.paywell.services.database;

import com.cloudwell.paywell.services.activity.utility.banglalion.model.BanglalionHistory;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.model.DESCOHistory;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.model.DPDCHistory;
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.model.WasaHistory;
import com.cloudwell.paywell.services.activity.utility.electricity.westzone.model.WestZoneHistory;
import com.cloudwell.paywell.services.activity.utility.ivac.model.IvacHistory;
import com.cloudwell.paywell.services.activity.utility.karnaphuli.model.KarnaphuliHistory;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model.PallibidyutHistory;

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

    @Query("SELECT * FROM  PallibidyutHistory")
    List<PallibidyutHistory> getAllPallibidyutHistoryHistory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPallibidyutHistory(PallibidyutHistory pallibidyutHistory);

    @Query("SELECT * FROM  WestZoneHistory")
    List<WestZoneHistory> getAllWestZoneHistory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWestZoneHistory(WestZoneHistory westZoneHistory);

    @Query("SELECT * FROM  IvacHistory")
    List<IvacHistory> getAllIvacHistoryHistory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIvacHistory(IvacHistory ivacHistory);

    @Query("SELECT * FROM  BanglalionHistory")
    List<BanglalionHistory> getAllBanglalionHistoryHistory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBanglalionHistory(BanglalionHistory banglalionHistory);

    @Query("SELECT * FROM  KarnaphuliHistory")
    List<KarnaphuliHistory> getAllKarnaphuliHistoryHistory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertKarnaphuliHistory(KarnaphuliHistory karnaphuliHistory);

}
