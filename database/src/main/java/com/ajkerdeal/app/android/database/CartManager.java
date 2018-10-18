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
import java.util.ServiceConfigurationError;

/**
 * Created by mitu on 6/10/17.
 */

public class CartManager {
    private SQLiteDatabase sqLiteDatabase;
    private CartItemHelper cartItemHelper;

    public CartManager(Context context) {
        cartItemHelper=new CartItemHelper(context);
    }

    private void open() {
        sqLiteDatabase = cartItemHelper.getWritableDatabase();
    }

    private void close() {
        cartItemHelper.close();
    }


    public boolean insertItem(CartItemModel cartItemModel){

            this.open();

            ContentValues cv = new ContentValues();
            cv.put(CartItemHelper.CART_DEALID,cartItemModel.getDealID());
            cv.put(CartItemHelper.CART_DEALPRICE,cartItemModel.getDealPrice());
            cv.put(CartItemHelper.CART_DEALSIZE,cartItemModel.getDealSize());
            cv.put(CartItemHelper.CART_DCHARGE,cartItemModel.getDeliveryCharge());
            cv.put(CartItemHelper.CART_DCHARGEOUT,cartItemModel.getDeliveryChargeOutSide());
            cv.put(CartItemHelper.CART_QTN,cartItemModel.getQtn());
            long inserted = sqLiteDatabase.insert(CartItemHelper.TABLE_ADDTOCART, null, cv);
            this.close();
        Log.e("insert", "insertItem: " +inserted);
            sqLiteDatabase.close();
            if (inserted > 0) {
                return true;
            } else return false;

        }


    public ArrayList<Integer> getDealId(){

        this.open();
        ArrayList<Integer> contactList = new ArrayList<>();
        String sort_order=CartItemHelper.CART_DEALID+"  ASC";

        String[] projection = {
                CartItemHelper.CART_DEALID
        };
        Cursor cursor = sqLiteDatabase.query(CartItemHelper.TABLE_ADDTOCART, projection, null, null, null, null, sort_order);

        while (cursor.moveToNext()){

            contactList.add(cursor.getInt(cursor.getColumnIndex(CartItemHelper.CART_DEALID)));
        }
        Collections.reverse(contactList);
        return contactList;
    }



    public List<CartItemModel> getAllItem() {
        this.open();
        List<CartItemModel> cartItemModelsList = new ArrayList<>();
        String sort_order=CartItemHelper.CART_DEALID+"  ASC";

        Cursor cursor = sqLiteDatabase.query(CartItemHelper.TABLE_ADDTOCART, null, null, null, null, null, sort_order);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                int id = cursor.getInt(cursor.getColumnIndex(CartItemHelper.CART_ID));
                int dealId = cursor.getInt(cursor.getColumnIndex(CartItemHelper.CART_DEALID));
                int dealPrice = cursor.getInt(cursor.getColumnIndex(CartItemHelper.CART_DEALPRICE));
                String dealSize=cursor.getString(cursor.getColumnIndex(CartItemHelper.CART_DEALSIZE));
                double deliveryCharge=cursor.getDouble(cursor.getColumnIndex(CartItemHelper.CART_DCHARGE));
                double deliveryChargeOut=cursor.getDouble(cursor.getColumnIndex(CartItemHelper.CART_DCHARGEOUT));
                int quantity=cursor.getInt(cursor.getColumnIndex(CartItemHelper.CART_QTN));
                CartItemModel cartItemModel=new CartItemModel(id,dealId,dealPrice,dealSize,deliveryCharge,deliveryChargeOut,quantity);
                cartItemModelsList.add(cartItemModel);
                cursor.moveToNext();
            }
            this.close();
            sqLiteDatabase.close();
        }
        return cartItemModelsList;
    }

    public boolean updateCartItem(int id,int price,int count){
        this.open();
        String[] projection = {
                CartItemHelper.CART_DEALID };
        ContentValues cv = new ContentValues();
        cv.put(CartItemHelper.CART_DEALPRICE,price);
        cv.put(CartItemHelper.CART_QTN,count);
        int updated=sqLiteDatabase.update(CartItemHelper.TABLE_ADDTOCART,cv, CartItemHelper.CART_DEALID+ " = "+id, null);
        this.close();
        if (updated > 0) {
            Log.d("pony", "updateCartItem: 1" );
            return true;
        } else {
            Log.d("pony", "updateCartItem: 2" );
            return false;
        }

    }

    public int isExist(int dealId){

        this.open();
        int a  = -1;
        String[] projection = {
                CartItemHelper.CART_ID };
        String[] whereValues = {
                String.valueOf(dealId)};
        Cursor cursor = sqLiteDatabase.query(CartItemHelper.TABLE_ADDTOCART, projection, CartItemHelper.CART_DEALID +" = ?", whereValues, null, null, null);

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


    public boolean deleteCartItem(int id) {
        this.open();
        int deleted=sqLiteDatabase.delete(CartItemHelper.TABLE_ADDTOCART,CartItemHelper.CART_DEALID+ " = "+id,null);
        this.close();
        if (deleted > 0) {
            return true;
        } else {
            return false;
        }

    }
    public boolean deleteall(){
        this.open();
        int deleted=sqLiteDatabase.delete(CartItemHelper.TABLE_ADDTOCART,null,null);
        this.close();
        if(deleted>0){
            return true;
        }
        else {
            return false;
        }

    }
    public int getProductCount() {
        this.open();
        String countQuery = "SELECT  * FROM " + CartItemHelper.TABLE_ADDTOCART;
//        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        this.close();
        return cnt;
    }
}






