package com.example.kuncen.Handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public static long insertDataPass(String website, String username, String password) {
        contentValues = new ContentValues();
        contentValues.put(databasePass.col_website_name, website);
        contentValues.put(databasePass.col_username, username);
        contentValues.put(databasePass.col_pass, password);
        return sqLiteDatabase.insert(databasePass.table_data, null, contentValues);
    }

    public ArrayList<DataModel> displayData() {
        sqLiteDatabase = databasePass.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + databasePass.table_data, null);
        ArrayList<DataModel> modelArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                modelArrayList.add(new DataModel(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modelArrayList;
    }

}
