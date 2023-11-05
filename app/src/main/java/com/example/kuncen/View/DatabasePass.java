package com.example.kuncen.View;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.kuncen.Model.DataModel;

import java.util.ArrayList;

public class DatabasePass extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "kuncen.db";
    public final String table_user = "user";
    public final String col_username = "username";
    public final String col_pass = "password";

    public final String table_admin = "admin";
    public final String col_admin_username = "username";
    public final String col_admin_pass = "password";

    public final String table_data = "data_pass";
    public final String col_website_name = "name_website";
    public final String col_website_username = "username";
    public final String col_website_pass = "password";

    private final String table_transaction = "subcription";
    private final String table_history = "history";
    private Context context;
    private ContentValues contentValues;
    private static final int DATABASE_VERSION = 1;

    public DatabasePass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS " + table_user + " (id_user integer primary key autoincrement, " + col_username + " text, " + col_pass + " text)");
        contentValues = new ContentValues();
        contentValues.put(col_username, "admin");
        contentValues.put(col_pass, "miminganteng");
        db.insert(table_user, null, contentValues);

        db.execSQL("create table IF NOT EXISTS " + table_admin + " (id_admin integer primary key autoincrement," + col_admin_username + " text," + col_admin_pass + " text)");
        contentValues = new ContentValues();
        contentValues.put(col_admin_username, "admin");
        contentValues.put(col_admin_pass, "admin");
        db.insert(table_admin, null, contentValues);

        db.execSQL("create table IF NOT EXISTS " + table_data + " (id_data integer primary key autoincrement," + "id_user integer," + col_website_name + " text," + col_website_username + " text, " + col_website_pass + " text," + "FOREIGN KEY (id_user) REFERENCES " + table_data + "(id_user))");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}