package com.example.kuncen.Handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.kuncen.View.DatabasePass;
import com.example.kuncen.View.MainActivity;

public class DataPasswordHandler extends MainActivity {
    private ContentValues contentValues;
    private DatabasePass databasePass;
    private SQLiteDatabase sqLiteDatabase;

    public DataPasswordHandler(Context context) {
        databasePass = new DatabasePass(context);
    }

    public long insertDataPass(String website, String username, String password) {
        contentValues = new ContentValues();
        contentValues.put(databasePass.col_name_website, website);
        contentValues.put(databasePass.col_username, username);
        contentValues.put(databasePass.col_pass, password);
        sqLiteDatabase = databasePass.getWritableDatabase();
        return sqLiteDatabase.insert(databasePass.table_data, null, contentValues);
    }
}
