package com.cloudwell.paywell.services.database;

import com.cloudwell.paywell.services.activity.notification.model.NotificationDetailMessage;
import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.model.NotificationDetailMessageSync;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
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
