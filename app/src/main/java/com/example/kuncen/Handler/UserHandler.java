package com.example.kuncen.Handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kuncen.EncryptionKey.HashingKey;
import com.example.kuncen.Model.UserModel;
import com.example.kuncen.View.DatabasePass;
import com.example.kuncen.View.MainActivity;

import java.util.ArrayList;

public class UserHandler extends MainActivity {
    private static DatabasePass databasePass;
    private SQLiteDatabase sqLiteDatabase;
    private ContentValues contentValues;
    private HashingKey hashingKey;
    private String whereClause;
    private Cursor cursor;
    private ArrayList<UserModel> modelArrayList = new ArrayList<>();

    public UserHandler(Context context) {
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

    public long insertUser(String username, String password, String date) {
        contentValues = new ContentValues();
        contentValues.put(databasePass.col_username, username);
        contentValues.put(databasePass.col_pass, password);
        return sqLiteDatabase.insert( databasePass.table_user, null, contentValues);
    }

    //    public boolean readUser(String username) {
    public int readUser(String username, String password) {
        hashingKey = new HashingKey();
        int id_user = -1;
        String query;
        if (password == "null") {
            query = "select * from " + databasePass.table_user + " where " + databasePass.col_username + " = '" + username + "'";
        } else {
            query = "select * from " + databasePass.table_user + " where " + databasePass.col_username + " = '" + username + "' and " + databasePass.col_pass + " = '" + password + "'";
        }
        cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            id_user = cursor.getInt(0);
        }

        cursor.close();
        return id_user;

//        if (cursor.getCount() > 0) {
//            cursor.close();
//            return true;
//        } else {
//            cursor.close();
//            return false;
//        }
    }

    public int deleteUser(String username) {
        contentValues = new ContentValues();
        UserHandler.this.contentValues.put(databasePass.col_username, username);
        whereClause = databasePass.col_username + " = '" + username + "'";
        return sqLiteDatabase.delete(databasePass.table_user, whereClause, null);
    }

    public ArrayList<UserModel> displayUser() {
        sqLiteDatabase = databasePass.getReadableDatabase();
        cursor = sqLiteDatabase.rawQuery("select id_user, username, password from " + databasePass.table_user + "", null);
        if (cursor.moveToFirst()) {
            do {
                modelArrayList.add(new UserModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return modelArrayList;
    }
}
