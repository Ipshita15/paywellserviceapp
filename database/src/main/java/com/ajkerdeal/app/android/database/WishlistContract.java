package com.ajkerdeal.app.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by piash on 6/13/16.
 */
public class WishlistContract extends DatabaseHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WishlistTableColumns.TABLE_NAME + " ( " +
                    WishlistTableColumns._ID +  " INTEGER"+ " PRIMERY KEY, "
                    + WishlistTableColumns.DELETED_DEALID +  " INTEGER" + " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WishlistTableColumns.TABLE_NAME;

    public WishlistContract(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public static abstract class WishlistTableColumns implements BaseColumns {
        public static final String TABLE_NAME = "Wishlist";
        public static final String _ID = "Id";
        public static final String DELETED_DEALID = "DealId";
    }
}
