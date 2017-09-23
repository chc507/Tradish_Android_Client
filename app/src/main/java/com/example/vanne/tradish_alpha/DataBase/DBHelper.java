package com.example.vanne.tradish_alpha.DataBase;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import com.example.vanne.tradish_alpha.Models.Cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vanne on 9/22/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    //Table Column Names
    public static final String COLUMN_USER_ID = "User_ID";
    public static final String COLUMN_ORDER_ID = "ORDER_ID";
    public static final String COLUMN_DISHES = "DISHES";
    public static final String DB_NAME = "Database";
    public String tableName;

    private HashMap hp;
    public DBHelper(Context context, String tableName){
        super(context, DB_NAME, null,DATABASE_VERSION);
        this.tableName = tableName;
    }

    /*Creating Table*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS" + tableName + "(" +
                            COLUMN_USER_ID + "INTEGER PRIMARY KEY," + COLUMN_ORDER_ID +  "INTEGER," + COLUMN_DISHES + "TEXT," + ")";
        db.execSQL(CREATE_TABLE);
    }

    /*Upgrading Database*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL ("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }

    /*All the CRUD Operation (Create, Read, Update and Delete) */
    public void addOrder(int user_ID, String OrderJson) {}


    // Getting All Orders
    public ArrayList<String> getAllOrders(int user_ID) {
        ArrayList<String> allOrders = new ArrayList<>();
        return allOrders;
    }
}
