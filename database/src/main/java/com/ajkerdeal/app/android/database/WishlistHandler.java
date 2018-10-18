package com.ajkerdeal.app.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
/**
 * Created by piash on 6/13/16.
 */
public class WishlistHandler {



    private static final String TAG = "WishlistHandler";
    private WishlistContract mWishlistContract;
    private SQLiteDatabase mDatabase;
    public WishlistHandler(final Context context) {

        mWishlistContract = new WishlistContract(context);
    }
    public  void open(){
        mDatabase=mWishlistContract.getWritableDatabase();
    }

    public  void close(){
        mWishlistContract.close();
    }

    public long insertWishlist(int dealId){
        this.open();
        long rowAdded = 1L;

        ContentValues cv = new ContentValues();
        cv.put(WishlistContract.WishlistTableColumns._ID, dealId);
        rowAdded += mDatabase.insert(WishlistContract.WishlistTableColumns.TABLE_NAME, null, cv);
        return rowAdded;
    }
    public long insertWishlistTractForDeletedItem(int dealId){

        this.open();
        long rowAdded = 1L;


        ContentValues cv = new ContentValues();
        cv.put(WishlistContract.WishlistTableColumns.DELETED_DEALID, dealId);
        rowAdded += mDatabase.insert(WishlistContract.WishlistTableColumns.TABLE_NAME, null, cv);
        return rowAdded;
    }

    public boolean delete(int dealId){
        this.open();
        mDatabase.delete(WishlistContract.WishlistTableColumns.TABLE_NAME, WishlistContract.WishlistTableColumns._ID + " = "+ dealId, null);
        return true;
    }
    public boolean deleteWishlistTrackForDeletedItem(int dealId){
        this.open();
        mDatabase.delete(WishlistContract.WishlistTableColumns.TABLE_NAME, WishlistContract.WishlistTableColumns.DELETED_DEALID + " = "+ dealId, null);
        return true;
    }

    public ArrayList<Integer> getAllWishlistData(){

        this.open();
        ArrayList<Integer> contactList = new ArrayList<>();

        String[] projection = {
                WishlistContract.WishlistTableColumns._ID
        };
        Cursor cursor = mDatabase.query(WishlistContract.WishlistTableColumns.TABLE_NAME, projection, null, null, null, null, null);

        while (cursor.moveToNext()){

            contactList.add(cursor.getInt(0));
        }
        Collections.reverse(contactList);
        return contactList;
    }

    public ArrayList<Integer> getAllWishlistDeletedData(){

        this.open();
        ArrayList<Integer> contactList = new ArrayList<>();

        String[] projection = {
                WishlistContract.WishlistTableColumns.DELETED_DEALID
        };
        Cursor cursor = mDatabase.query(WishlistContract.WishlistTableColumns.TABLE_NAME, projection, null, null, null, null, null);

        while (cursor.moveToNext()){

            if(cursor.getInt(0) > 0 )
                contactList.add(cursor.getInt(0));
        }

        return contactList;
    }


    public int isFavourite(int dealId){

        this.open();
        int a  = -1;
        String[] projection = {
                WishlistContract.WishlistTableColumns._ID };
        String[] whereValues = {
                String.valueOf(dealId)};
        Cursor cursor = mDatabase.query(WishlistContract.WishlistTableColumns.TABLE_NAME, projection, WishlistContract.WishlistTableColumns._ID +" = ?", whereValues, null, null, null);

        if(cursor == null){
            return 0;
        }
        else {

           while (cursor.moveToNext()){
                a = cursor.getInt(0);
           }
            return a;
        }


    }

    public int isFavouriteDeleteTrack(int dealId){

        this.open();
        int a  = -1;
        String[] projection = {
                WishlistContract.WishlistTableColumns.DELETED_DEALID };
        String[] whereValues = {
                String.valueOf(dealId)};
        Cursor cursor = mDatabase.query(WishlistContract.WishlistTableColumns.TABLE_NAME, projection, WishlistContract.WishlistTableColumns.DELETED_DEALID +" = ?", whereValues, null, null, null);

        if(cursor == null){
            return 0;
        }
        else {

            while (cursor.moveToNext()){
                a = cursor.getInt(0);
            }
            return a;
        }


    }

    public int getAllWishlistDataSize(){


        ArrayList<Integer> contactList = new ArrayList<>();
        Cursor cursor = mDatabase.query(WishlistContract.WishlistTableColumns.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()){
            contactList.add(cursor.getInt(0));
        }
        return contactList.size();
    }

    public void databaseClear(){

        this.open();
        mDatabase.delete(WishlistContract.WishlistTableColumns.TABLE_NAME, null, null);
    }



}
