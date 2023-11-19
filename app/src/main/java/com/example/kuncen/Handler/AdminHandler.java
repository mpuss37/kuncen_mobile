package com.example.kuncen.Handler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kuncen.View.DatabasePass;
import com.example.kuncen.View.MainActivity;

public class AdminHandler extends MainActivity {
    private static DatabasePass databasePass;
    private SQLiteDatabase sqLiteDatabase;

    public AdminHandler(Context context) {
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

    public int readAdmin(String username, String password) {
        int id_admin = -1;
        String query = "select * from " + databasePass.table_admin + " where " + databasePass.col_admin_username + " = '" + username + "' and " + databasePass.col_admin_pass + " = '" + password + "'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
//        if (cursor.getCount() > 0) {
//            cursor.close();
//            return true;
//        } else {
//            cursor.close();
//            return false;
//        }
        if (cursor.moveToFirst()) {
            id_admin = cursor.getInt(0);
        }

        cursor.close();
        return id_admin;
    }
}
