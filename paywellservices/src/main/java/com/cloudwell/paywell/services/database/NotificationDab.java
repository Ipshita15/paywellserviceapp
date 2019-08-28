package com.cloudwell.paywell.services.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.cloudwell.paywell.services.activity.notification.model.NotificationDetailMessage;
import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.model.NotificationDetailMessageSync;

import java.util.List;

import io.reactivex.Flowable;


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
@Dao
public interface NotificationDab {

    @Query("SELECT * FROM notificationDetailMessage ")
    Flowable<List<NotificationDetailMessage>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(NotificationDetailMessage detailMessage);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(List<NotificationDetailMessage> detailMessages);

    @Delete
    void delete(List<NotificationDetailMessage> task);

    @Update
    int update(NotificationDetailMessage task);

    @Query("DELETE FROM notificationdetailmessage")
    public void deletedALl();

    @Delete
    void deleteData(List<NotificationDetailMessage> notificationDetailMessagesList);


    // notification sync
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSync(NotificationDetailMessageSync detailMessageddjjj);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertSync(List<NotificationDetailMessageSync> detailMessages);

    @Query("SELECT * FROM NotificationDetailMessageSync ")
    List<NotificationDetailMessageSync> getAllLSyncData();


    @Query("DELETE FROM NotificationDetailMessageSync where message_id = :messageId ")
    void deleteSync(String messageId);


}
