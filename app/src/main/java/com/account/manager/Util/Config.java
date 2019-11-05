/*
 * *
 *  * Created by Bhargav Ghoghari on 11/5/19 5:11 PM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 9/9/19 4:01 PM
 *
 */

package com.account.manager.Util;

public class Config {

    public static final String DATABASE_NAME = "client-db";

    //column names of client table
    public static final String TABLE_CLIENT = "client";
    public static final String COLUMN_CLIENT_ID = "_id";
    public static final String COLUMN_CLIENT_NAME = "name";
    public static final String COLUMN_CLIENT_REGISTRATION = "registration_no";
    public static final String COLUMN_CLIENT_PHONE = "phone";

    //column names of product table
    public static final String TABLE_PRODUCT = "product";
    public static final String COLUMN_PRODUCT_ID = "_id";
    public static final String COLUMN_PRODUCT_NUMBER = "fk_product_no";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_QUANTITY = "product_code";
    public static final String COLUMN_PRODUCT_PRICE = "credit";
    public static final String CLIENT_SUB_CONSTRAINT = "client_sub_unique";

    //others for general purpose key-value pair data
    public static final String TITLE = "title";
    public static final String CREATE_CLIENT = "create_client";
    public static final String UPDATE_CLIENT = "update_client";
    public static final String CREATE_PRODUCT = "create_product";
    public static final String UPDATE_PRODUCT = "update_product";
    public static final String CLIENT_REGISTRATION = "client_registration";
}
