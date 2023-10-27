package com.example.kuncen.View;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabasePass extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "kuncen.db";
    public final String table_user = "user";
    public final String col_username = "username";
    public final String col_pass = "password";
    private final String table_admin = "admin";
    public final String table_data = "data_pass";
    public final String col_name_website = "name_website";

    private final String table_transaction = "subcription";
    private final String table_history = "history";
    private Context context;
    private static final int DATABASE_VERSION = 1;

    public DatabasePass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS " + table_user + " (id_user integer primary key autoincrement, " + col_username + " text, " + col_pass + " text)");
        db.execSQL("create table IF NOT EXISTS " + table_data + " (id_admin integer primary key autoincrement," + col_name_website + " text," + col_username + " text, " + col_pass + " text)");
        System.out.println("finish create table");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}