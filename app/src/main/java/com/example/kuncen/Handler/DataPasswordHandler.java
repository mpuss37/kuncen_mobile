package com.example.kuncen.Handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Display;

import com.example.kuncen.Model.DataModel;
import com.example.kuncen.View.DatabasePass;
import com.example.kuncen.View.MainActivity;

import java.util.ArrayList;

public class DataPasswordHandler extends MainActivity {
    private static ContentValues contentValues;
    private static DatabasePass databasePass;
    private static SQLiteDatabase sqLiteDatabase;

    public DataPasswordHandler(Context context) {
        databasePass = new DatabasePass(context);
    }


    public void openWrite() {
        sqLiteDatabase = databasePass.getWritableDatabase();
    }

    public void openRead() {
        sqLiteDatabase = databasePass.getReadableDatabase();
    }

    public void close() {
        databasePass.close();
    }

    public static long insertDataPass(int id_user, String website, String username, String password) {
        contentValues = new ContentValues();
        contentValues.put("id_user", id_user);
        contentValues.put(databasePass.col_website_name, website);
        contentValues.put(databasePass.col_username, username);
        contentValues.put(databasePass.col_pass, password);
        return sqLiteDatabase.insert(databasePass.table_data, null, contentValues);
    }

    public boolean checkData(int id_user) {
        Cursor cursor = sqLiteDatabase.rawQuery("select id_data from " + databasePass.table_data + " where id_user = " + id_user, null);
        if (cursor.getCount() >= 3) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public long deleteData(int id_data) {
        return sqLiteDatabase.delete(databasePass.table_data, "id_data = " + id_data, null);
    }

    public ArrayList<DataModel> displayData(int id_user) {
        sqLiteDatabase = databasePass.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + databasePass.table_data + " where id_user = " + id_user, null);
        ArrayList<DataModel> modelArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                modelArrayList.add(new DataModel(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modelArrayList;
    }

}
