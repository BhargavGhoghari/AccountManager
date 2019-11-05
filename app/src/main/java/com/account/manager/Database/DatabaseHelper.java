/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:09 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/9/19 4:02 PM
 *
 */

package com.account.manager.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.account.manager.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper databaseHelper;

    // All Static variables
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = Config.DATABASE_NAME;

    // Constructor
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static DatabaseHelper getInstance(Context context) {
        if(databaseHelper==null){
            synchronized (DatabaseHelper.class) {
                if(databaseHelper==null)
                    databaseHelper = new DatabaseHelper(context);
            }
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tables SQL execution
        String CREATE_STUDENT_TABLE = "CREATE TABLE " + Config.TABLE_CLIENT + "("
                + Config.COLUMN_CLIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_CLIENT_NAME + " TEXT NOT NULL, "
                + Config.COLUMN_CLIENT_REGISTRATION + " INTEGER NOT NULL UNIQUE, "
                + Config.COLUMN_CLIENT_PHONE + " TEXT" //nullable
                + ")";

        String CREATE_SUBJECT_TABLE = "CREATE TABLE " + Config.TABLE_PRODUCT + "("
                + Config.COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_PRODUCT_NUMBER + " INTEGER NOT NULL, "
                + Config.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + Config.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                + Config.COLUMN_PRODUCT_PRICE + " REAL, " //nullable
                + "FOREIGN KEY (" + Config.COLUMN_PRODUCT_NUMBER + ") REFERENCES " + Config.TABLE_CLIENT + "(" + Config.COLUMN_CLIENT_REGISTRATION + ") ON UPDATE CASCADE ON DELETE CASCADE, "
                + "CONSTRAINT " + Config.CLIENT_SUB_CONSTRAINT + " UNIQUE (" + Config.COLUMN_PRODUCT_NUMBER + "," + Config.COLUMN_PRODUCT_QUANTITY + ")"
                + ")";

        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_SUBJECT_TABLE);

        Logger.d("DB created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_CLIENT);
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_PRODUCT);

        // Create tables again
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        //enable foreign key constraints like ON UPDATE CASCADE, ON DELETE CASCADE
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

}
