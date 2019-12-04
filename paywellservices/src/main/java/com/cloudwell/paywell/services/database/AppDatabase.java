package com.cloudwell.paywell.services.database;

import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.Airport;
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BusLocalDB;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BusSchedule;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Transport;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfo;
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu;
import com.cloudwell.paywell.services.activity.notification.model.NotificationDetailMessage;
import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.model.NotificationDetailMessageSync;
import com.cloudwell.paywell.services.activity.utility.banglalion.model.BanglalionHistory;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.model.DESCOHistory;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.model.DPDCHistory;
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.model.WasaHistory;
import com.cloudwell.paywell.services.activity.utility.electricity.westzone.model.WestZoneHistory;
import com.cloudwell.paywell.services.activity.utility.ivac.model.IvacHistory;
import com.cloudwell.paywell.services.activity.utility.karnaphuli.model.KarnaphuliHistory;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model.PallibidyutHistory;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
@Database(entities = {FavoriteMenu.class, NotificationDetailMessage.class, NotificationDetailMessageSync.class, Airport.class, Passenger.class, Transport.class, BusLocalDB.class, BusSchedule.class, TripScheduleInfo.class, DESCOHistory.class, DPDCHistory.class, WasaHistory.class, PallibidyutHistory.class, WestZoneHistory.class, IvacHistory.class, BanglalionHistory.class, KarnaphuliHistory.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoriteMenuDab mFavoriteMenuDab();

    public abstract NotificationDab mNotificationDab();

    public abstract AirtricketDab mAirtricketDab();

    public abstract BusTicketDab mBusTicketDab();

    public abstract UtilityDab mUtilityDab();
}
