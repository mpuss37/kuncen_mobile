package com.example.kuncen.Handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.inputmethodservice.Keyboard;

import com.example.kuncen.View.DatabasePass;
import com.example.kuncen.View.MainActivity;

public class UserHandler extends MainActivity {
    private static DatabasePass databasePass;
    private SQLiteDatabase sqLiteDatabase;

    public UserHandler(Context context) {
        databasePass = new DatabasePass(context);
    }

    public void openWrite() {
        sqLiteDatabase = databasePass.getWritableDatabase();
    }

    public void close() {
        databasePass.close();
    }

    public long insertUser(String username, String password) {
        ContentValues values = new ContentValues();
        values.put(databasePass.col_username, username);
        values.put(databasePass.col_pass, password);
        return sqLiteDatabase.insert(databasePass.table_user, null, values);
    }

    //    public boolean readUser(String username) {
    public int readUser(String username) {
        int id_user = -1;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + databasePass.table_user + " WHERE " + databasePass.col_username + " = '" + username + "'", null);
//        if (cursor.getCount() > 0) {
//            cursor.close();
//            return true;
//        } else {
//            cursor.close();
//            return false;
//        }
        if (cursor.moveToFirst()) {
            id_user = cursor.getInt(0);
        }

        cursor.close();
        return id_user;
    }
}
