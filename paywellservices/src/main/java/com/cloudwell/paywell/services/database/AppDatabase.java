package com.cloudwell.paywell.services.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.Airport;
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Bus;
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu;
import com.cloudwell.paywell.services.activity.notification.model.NotificationDetailMessage;
import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.model.NotificationDetailMessageSync;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
@Database(entities = {FavoriteMenu.class, NotificationDetailMessage.class, NotificationDetailMessageSync.class, Airport.class, Passenger.class, Bus.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoriteMenuDab mFavoriteMenuDab();

    public abstract NotificationDab mNotificationDab();

    public abstract AirtricketDab mAirtricketDab();

    public abstract BusTicketDab mBusTicketDab();
}
