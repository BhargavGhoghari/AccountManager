/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:10 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/17/19 3:01 PM
 *
 */

package com.account.manager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.account.manager.Features.ClientCRUD.CreateClient.Client;
import com.account.manager.Features.ProductCRUD.CreareProduct.Product;
import com.account.manager.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DatabaseQueryClass {

    private Context context;

    public DatabaseQueryClass(Context context){
        this.context = context;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public long insertClient(Client client){

        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_CLIENT_NAME, client.getName());
        contentValues.put(Config.COLUMN_CLIENT_REGISTRATION, client.getRegistrationNumber());
        contentValues.put(Config.COLUMN_CLIENT_PHONE, client.getPhoneNumber());

        try {
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_CLIENT, null, contentValues);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: Please enter valid customer no.", Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public List<Client> getAllClients(){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_CLIENT, null, null, null, null, null, null, null);

            /**
                 // If you want to execute raw query then uncomment below 2 lines. And comment out above line.

                 String SELECT_QUERY = String.format("SELECT %s, %s, %s, %s, %s FROM %s", Config.COLUMN_STUDENT_ID, Config.COLUMN_STUDENT_NAME, Config.COLUMN_CLIENT_REGISTRATION, Config.COLUMN_STUDENT_EMAIL, Config.COLUMN_STUDENT_PHONE, Config.TABLE_STUDENT);
                 cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<Client> clientList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_CLIENT_ID));
                        String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_CLIENT_NAME));
                        long registrationNumber = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_CLIENT_REGISTRATION));
                        String phone = cursor.getString(cursor.getColumnIndex(Config.COLUMN_CLIENT_PHONE));

                        clientList.add(new Client(id, name, registrationNumber, phone));
                    }   while (cursor.moveToNext());

                    return clientList;
                }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public Client getClientByRegNum(long registrationNum){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Client client = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_CLIENT, null,
                    Config.COLUMN_CLIENT_REGISTRATION + " = ? ", new String[]{String.valueOf(registrationNum)},
                    null, null, null);

            /**
                 // If you want to execute raw query then uncomment below 2 lines. And comment out above sqLiteDatabase.query() method.

                 String SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = %s", Config.TABLE_STUDENT, Config.COLUMN_CLIENT_REGISTRATION, String.valueOf(registrationNum));
                 cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
             */

            if(cursor.moveToFirst()){
                int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_CLIENT_ID));
                String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_CLIENT_NAME));
                long registrationNumber = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_CLIENT_REGISTRATION));
                String phone = cursor.getString(cursor.getColumnIndex(Config.COLUMN_CLIENT_PHONE));

                client = new Client(id, name, registrationNumber, phone);
            }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return client;
    }

    public long updateClientInfo(Client client){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_CLIENT_NAME, client.getName());
        contentValues.put(Config.COLUMN_CLIENT_REGISTRATION, client.getRegistrationNumber());
        contentValues.put(Config.COLUMN_CLIENT_PHONE, client.getPhoneNumber());

        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_CLIENT, contentValues,
                    Config.COLUMN_CLIENT_ID + " = ? ",
                    new String[] {String.valueOf(client.getId())});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public long deleteClientByRegNum(long registrationNum) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(Config.TABLE_CLIENT,
                                    Config.COLUMN_CLIENT_REGISTRATION + " = ? ",
                                    new String[]{ String.valueOf(registrationNum)});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deletedRowCount;
    }

    public boolean deleteAllClients(){
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(Config.TABLE_CLIENT, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_CLIENT);

            if(count==0)
                deleteStatus = true;

        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deleteStatus;
    }

    public long getNumberOfClient(){
        long count = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_CLIENT);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return count;
    }

    // products
    public long insertProduct(Product product, long registrationNo){
        long rowId = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_PRODUCT_NAME, product.getName());
        contentValues.put(Config.COLUMN_PRODUCT_QUANTITY, product.getQuantity());
        contentValues.put(Config.COLUMN_PRODUCT_PRICE, product.getPrice());
        contentValues.put(Config.COLUMN_PRODUCT_NUMBER, registrationNo);

        try {
            rowId = sqLiteDatabase.insertOrThrow(Config.TABLE_PRODUCT, null, contentValues);
        } catch (SQLiteException e){
            Logger.d(e);
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowId;
    }

    public Product getProductById(long productId){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Product product = null;

        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(Config.TABLE_PRODUCT, null,
                    Config.COLUMN_PRODUCT_ID + " = ? ", new String[] {String.valueOf(productId)},
                    null, null, null);

            if(cursor!=null && cursor.moveToFirst()){
                String productName = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PRODUCT_NAME));
                int productCode = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PRODUCT_QUANTITY));
                String productCredit = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PRODUCT_PRICE));

                product = new Product(productId, productName, productCode, productCredit);
            }
        } catch (SQLiteException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return product;
    }

    public long updateProductInfo(Product product){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_PRODUCT_NAME, product.getName());
        contentValues.put(Config.COLUMN_PRODUCT_QUANTITY, product.getQuantity());
        contentValues.put(Config.COLUMN_PRODUCT_PRICE, product.getPrice());

        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_PRODUCT, contentValues,
                    Config.COLUMN_PRODUCT_ID + " = ? ",
                    new String[] {String.valueOf(product.getId())});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public List<Product> getAllProductsByRegNo(long registrationNo){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        List<Product> productList = new ArrayList<>();
        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(Config.TABLE_PRODUCT,
                    new String[] {Config.COLUMN_PRODUCT_ID, Config.COLUMN_PRODUCT_NAME, Config.COLUMN_PRODUCT_QUANTITY, Config.COLUMN_PRODUCT_PRICE},
                    Config.COLUMN_PRODUCT_NUMBER + " = ? ",
                    new String[] {String.valueOf(registrationNo)},
                    null, null, null);

            if(cursor!=null && cursor.moveToFirst()){
                productList = new ArrayList<>();
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PRODUCT_ID));
                    String productName = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PRODUCT_NAME));
                    int productCode = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PRODUCT_QUANTITY));
                    String productCredit = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PRODUCT_PRICE));

                    productList.add(new Product(id, productName, productCode, productCredit));
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return productList;
    }

    public boolean deleteProductById(long productId) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        int row = sqLiteDatabase.delete(Config.TABLE_PRODUCT,
                Config.COLUMN_PRODUCT_ID + " = ? ", new String[]{String.valueOf(productId)});

        return row > 0;
    }

    public boolean deleteAllProductsByRegNum(long registrationNum) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        int row = sqLiteDatabase.delete(Config.TABLE_PRODUCT,
                Config.COLUMN_PRODUCT_NUMBER + " = ? ", new String[]{String.valueOf(registrationNum)});

        return row > 0;
    }

    public long getNumberOfProduct(){
        long count = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_PRODUCT);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return count;
    }

}