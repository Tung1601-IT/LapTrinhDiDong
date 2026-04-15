package com.example.ltdd.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ltdd.model.RepairOrder;

import java.util.ArrayList;
import java.util.List;

public class DatabaseRepair extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LTDD_Repair.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_REPAIR = "repair_orders";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CUSTOMER_NAME = "customer_name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_DEVICE = "device";
    public static final String COLUMN_ISSUE = "issue_description";
    public static final String COLUMN_RECEIVED_DATE = "received_date";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_COMPLETED_DATE = "completed_date";
    public static final String STATUS_NEW = "Mới tiếp nhận";
    public static final String STATUS_REPAIRING = "Đang sửa";
    public static final String STATUS_DONE = "Đã sửa";

    public DatabaseRepair(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_REPAIR + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CUSTOMER_NAME + " TEXT NOT NULL, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_DEVICE + " TEXT NOT NULL, " +
                COLUMN_ISSUE + " TEXT NOT NULL, " +
                COLUMN_RECEIVED_DATE + " TEXT NOT NULL, " +
                COLUMN_STATUS + " TEXT NOT NULL, " +
                COLUMN_COMPLETED_DATE + " TEXT" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPAIR);
        onCreate(db);
    }

    public long insertRepairOrder(RepairOrder order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CUSTOMER_NAME, order.getCustomerName());
        values.put(COLUMN_PHONE, order.getPhone());
        values.put(COLUMN_DEVICE, order.getDevice());
        values.put(COLUMN_ISSUE, order.getIssueDescription());
        values.put(COLUMN_RECEIVED_DATE, order.getReceivedDate());
        values.put(COLUMN_STATUS, order.getStatus());
        values.put(COLUMN_COMPLETED_DATE, order.getCompletedDate());
        return db.insert(TABLE_REPAIR, null, values);
    }

    public int updateRepairOrder(RepairOrder order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CUSTOMER_NAME, order.getCustomerName());
        values.put(COLUMN_PHONE, order.getPhone());
        values.put(COLUMN_DEVICE, order.getDevice());
        values.put(COLUMN_ISSUE, order.getIssueDescription());
        values.put(COLUMN_RECEIVED_DATE, order.getReceivedDate());
        values.put(COLUMN_STATUS, order.getStatus());
        values.put(COLUMN_COMPLETED_DATE, order.getCompletedDate());

        return db.update(TABLE_REPAIR, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(order.getId())});
    }

    public int deleteRepairOrder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_REPAIR, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public RepairOrder getRepairOrderById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_REPAIR,
                null,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        RepairOrder order = null;
        if (cursor != null && cursor.moveToFirst()) {
            order = mapCursorToRepairOrder(cursor);
            cursor.close();
        }

        return order;
    }

    public List<RepairOrder> getActiveRepairOrders() {
        List<RepairOrder> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_REPAIR,
                null,
                COLUMN_STATUS + " != ?",
                new String[]{STATUS_DONE},
                null,
                null,
                COLUMN_ID + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                list.add(mapCursorToRepairOrder(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public List<RepairOrder> getCompletedRepairOrders() {
        List<RepairOrder> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_REPAIR,
                null,
                COLUMN_STATUS + " = ?",
                new String[]{STATUS_DONE},
                null,
                null,
                COLUMN_ID + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                list.add(mapCursorToRepairOrder(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    private RepairOrder mapCursorToRepairOrder(Cursor cursor) {
        return new RepairOrder(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEVICE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ISSUE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECEIVED_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED_DATE))
        );
    }
}
