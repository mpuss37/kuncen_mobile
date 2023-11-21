package com.example.kuncen.Handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kuncen.View.DatabasePass;
import com.example.kuncen.View.MainActivity;

import java.time.LocalDate;

public class SubscriptionHandler extends MainActivity {
    private static ContentValues contentValues;
    private static DatabasePass databasePass;
    private static SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;

    public SubscriptionHandler(Context context) {
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

    public static long insertDataSubs(int id_user, String id_code, String code, String dateStart, String dateEnd) {
        contentValues = new ContentValues();
        contentValues.put("id_user", id_user);
        contentValues.put(databasePass.col_id_code, id_code);
        contentValues.put(databasePass.col_code_subscription, code);
        contentValues.put(databasePass.col_date_start, dateStart);
        contentValues.put(databasePass.col_date_end, dateEnd);
        return sqLiteDatabase.insert(databasePass.table_subscription, null, contentValues);
    }

    public int readId(int id_user) {
        int id_subs = -1;
        cursor = sqLiteDatabase.rawQuery("SELECT id_subscription FROM " + databasePass.table_subscription + " WHERE id_user = '" + id_user + "'", null);
        if (cursor.moveToFirst()) {
            id_subs = cursor.getInt(0);
        }
        cursor.close();
        return id_subs;
    }

    public LocalDate readDateEnd(int id_user){
        LocalDate localDateNow = LocalDate.now();
        cursor = sqLiteDatabase.rawQuery("SELECT "+databasePass.col_date_end+" FROM " + databasePass.table_subscription + " WHERE id_user = '" + id_user + "'", null);
        if (cursor.moveToFirst()) {
            localDateNow = LocalDate.parse(cursor.getString(0));
        }
        cursor.close();
        return localDateNow;
    }

    public void deleteSubs(int id_user){
        sqLiteDatabase.delete(databasePass.table_subscription," id_user = '" + id_user + "'", null);
    }

}
