package com.cloudwell.paywell.services.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.util.Log;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
public class DatabaseClient {

    private static DatabaseClient mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            Log.e("migrate", "Start");
            database.execSQL("CREATE TABLE IF NOT EXISTS `NotificationDetailMessage` (`added_datetime` TEXT, `balance_return_data` TEXT, `image_url` TEXT, `message` TEXT, `message_id` TEXT NOT NULL, `message_sub` TEXT, `status` TEXT, `type` TEXT, `message_expiry_time` TEXT, PRIMARY KEY(`message_id`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `NotificationDetailMessageSync` (`message_id` TEXT NOT NULL, PRIMARY KEY(`message_id`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `Airport` (`id` TEXT NOT NULL, `airport_name` TEXT NOT NULL, `city` TEXT NOT NULL, `country` TEXT NOT NULL, `iata` TEXT NOT NULL, `icao` TEXT NOT NULL, `iso` TEXT NOT NULL, `state` TEXT NOT NULL, `time_zone` TEXT NOT NULL, `status` TEXT NOT NULL, PRIMARY KEY(`id`))");
            database.execSQL("CREATE TABLE IF NOT EXISTS `Passenger` (`Title` TEXT NOT NULL, `FirstName` TEXT NOT NULL, `LastName` TEXT NOT NULL, `PaxType` TEXT NOT NULL, `DateOfBirth` TEXT NOT NULL, `Gender` TEXT NOT NULL, `PassportNumber` TEXT NOT NULL, `PassportExpiryDate` TEXT NOT NULL, `PassportNationality` TEXT NOT NULL, `CountryCode` TEXT NOT NULL, `Nationality` TEXT NOT NULL, `ContactNumber` TEXT NOT NULL, `Email` TEXT NOT NULL, `IsLeadPassenger` INTEGER NOT NULL, `Id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `isPassengerSleted` INTEGER NOT NULL, `Country` TEXT NOT NULL, `passportImagePath` TEXT NOT NULL, `file_extension` TEXT NOT NULL, `visa_extension` TEXT NOT NULL, `visa_content` TEXT NOT NULL, `nid_number` TEXT NOT NULL, `isDefault` INTEGER NOT NULL)");
            Log.e("migrate", "END");

        }
    };

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
    private DatabaseClient(Context mCtx) {
        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, DatabaseConstant.KEY_PARNELL_DATABASE_NAME)
                .addMigrations(MIGRATION_1_2)
                .build();
    }
}