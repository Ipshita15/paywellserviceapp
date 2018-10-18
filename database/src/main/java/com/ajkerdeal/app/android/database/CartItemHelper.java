package com.ajkerdeal.app.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mitu on 6/10/17.
 */

public class CartItemHelper extends SQLiteOpenHelper {
    private static final String TAG = "CartItemHandler";
    private SQLiteDatabase mDatabase;
    private Context context;

    public static final String DATABASE_NAME_CART_ITEM  = "ADDTOCART";
    public static final int DATABASE_VERSION_FOR_CART = 1;
    public static final String TABLE_ADDTOCART = "addToCart";
    public static final String CART_ID = "id";
    public static final String CART_DEALID = "DealID";
    public static final String CART_DEALPRICE="DealPrice";
    public static final String CART_DEALSIZE="DealSize";
    public static final String CART_DCHARGE="DeliveryCharge";
    public static final String CART_DCHARGEOUT="DeliveryChargeOutSide";
    public static final String CART_QTN="Qtn";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_ADDTOCART + "("
                    + CART_ID + " INTEGER PRIMARY KEY," + CART_DEALID + " INTEGER," +CART_DEALPRICE + " INTEGER," + CART_DEALSIZE + " TEXT,"
                    + CART_DCHARGE + " TEXT, " + CART_DCHARGEOUT + " TEXT," + CART_QTN + " INTEGER" + ")";




    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_ADDTOCART;


    public CartItemHelper(Context context) {
        super(context, DATABASE_NAME_CART_ITEM, null, DATABASE_VERSION_FOR_CART);
        this.context = context;
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
}
