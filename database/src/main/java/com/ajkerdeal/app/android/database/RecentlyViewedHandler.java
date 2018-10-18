package com.ajkerdeal.app.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mitu on 9/7/16.
 */
public class RecentlyViewedHandler  extends SQLiteOpenHelper{
    private static final String TAG = "WishlistHandler";
    private SQLiteDatabase mDatabase;

    public static final String TABLE_RECENTLY_VIEWED = "recentlyviewed";
    public static final String RECENTLY_ID = "recently_Id";
    public static final String RECENTLY_VIEWD_DEALID = "recently_DealId";
    public static final int DATABASE_VERSION_FOR_RECENTLY_VIEWED = 2;
    public static final String DATABASE_NAME_RECENTLY_VIEWED  = "RecentlyViewed";
    private Context context;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_RECENTLY_VIEWED + " ( " +
                    RECENTLY_ID +  " INTEGER"+ " PRIMERY KEY, "
                    + RECENTLY_VIEWD_DEALID +  " INTEGER" + " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_RECENTLY_VIEWED;


    public RecentlyViewedHandler(Context context) {
        super(context, DATABASE_NAME_RECENTLY_VIEWED, null, DATABASE_VERSION_FOR_RECENTLY_VIEWED);
        this.context = context;
        Log.d("DBHELPER","Constructed");
        //  Toast.makeText(this.context,"Constructed",Toast.LENGTH_SHORT).show();
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

    public  void open(){
        mDatabase = getWritableDatabase();
    }

    public  void close(){
        close();
    }

    public void insertRecentlist(int dealid){

        this.open();

        ContentValues cv = new ContentValues();
        cv.put(RECENTLY_VIEWD_DEALID, dealid);
        mDatabase.insert(TABLE_RECENTLY_VIEWED,RECENTLY_VIEWD_DEALID,cv);

    }


    public boolean deleterecentlist(int dealId){
        this.open();
        mDatabase.delete(TABLE_RECENTLY_VIEWED, RECENTLY_VIEWD_DEALID + " = "+ dealId, null);
        return true;
    }


    public List<Integer> getAllRecentList(){

        this.open();

        List<Integer> recentViewedList = new ArrayList<>();


        Cursor cursor = mDatabase.query(TABLE_RECENTLY_VIEWED,null,null,null,null,null,null);

        if(cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++){
                int DealId = cursor.getInt(cursor.getColumnIndex(RECENTLY_VIEWD_DEALID));
                //Log.d("one4", "getAllRecentList i-> "+ i +": dealid ->"+DealId +" , id-> "+cursor.getInt(cursor.getColumnIndex(RECENTLY_ID)));
                recentViewedList.add(DealId);
                cursor.moveToNext();
            }
        }
        Collections.reverse(recentViewedList);
        return recentViewedList;
    }




    }


