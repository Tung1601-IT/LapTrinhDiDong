package com.example.ltdd.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseKhachHang extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LTDD_Customer";
    private static final int DATABASE_VERSION = 1;

    public DatabaseKhachHang(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void Thucthi(String sql){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public Cursor Laydulieu(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists customer (" +
                "customer_id integer primary key autoincrement, " +
                "customer_name varchar(120) not null, " +
                "customer_gender varchar(20), " +
                "customer_email varchar(191), " +
                "customer_contact_no varchar(30), " +
                "customer_address text, " +
                "created_at timestamp default current_timestamp)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS customer");
        onCreate(db);
    }
}