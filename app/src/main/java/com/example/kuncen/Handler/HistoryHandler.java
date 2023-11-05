package com.example.kuncen.Handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.inputmethodservice.Keyboard;

import com.example.kuncen.Model.DataModel;
import com.example.kuncen.Model.UserModel;
import com.example.kuncen.View.DatabasePass;
import com.example.kuncen.View.MainActivity;

import java.util.ArrayList;

public class HistoryHandler extends MainActivity {
    private static DatabasePass databasePass;
    private SQLiteDatabase sqLiteDatabase;
    private ContentValues contentValues;

    public HistoryHandler(Context context) {
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

    public long insertHistory(String username, String password) {
        contentValues = new ContentValues();
        contentValues.put(databasePass.col_username, username);
        contentValues.put(databasePass.col_pass, password);
        return sqLiteDatabase.insert(databasePass.table_history, null, contentValues);
    }

    public int deleteHistory(String username) {
        contentValues = new ContentValues();
        HistoryHandler.this.contentValues.put(databasePass.col_username, username);
        String whereClause = databasePass.col_history_website_username + " = '" + username + "'";
        return sqLiteDatabase.delete(databasePass.table_history, whereClause, null);
    }

    public ArrayList<UserModel> displayHistory() {
        sqLiteDatabase = databasePass.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + databasePass.table_history + "", null);
        ArrayList<UserModel> modelArrayList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                modelArrayList.add(new UserModel(cursor.getString(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modelArrayList;
    }
}
