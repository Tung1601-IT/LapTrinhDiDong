package com.example.ltdd.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ltdd.model.Order;
import java.util.ArrayList;
import java.util.List;

public class DatabaseDonHang extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LTDD_Orders";
    private static final int DATABASE_VERSION = 6;

    public static final String TABLE_NAME = "paymentbill";
    public static final String COLUMN_ID = "pb_id";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_DATE = "pb_date";
    public static final String COLUMN_CUSTOMER_NAME = "customer_name";
    public static final String COLUMN_CUSTOMER_CONTACT = "customer_contact_no";
    public static final String COLUMN_IMEI = "purchase_mobile_imei_no";
    public static final String COLUMN_COMPANY = "purchase_mobile_company";
    public static final String COLUMN_SERIES = "purchase_mobile_series";
    public static final String COLUMN_MODEL = "purchase_mobile_model";
    public static final String COLUMN_PRICE = "purchase_mobile_price";
    public static final String COLUMN_IMPORT_PRICE = "cost_price";
    public static final String COLUMN_QTY = "sale_quantity";
    public static final String COLUMN_TOTAL_PAID = "total_paid_amount";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_CREATED_AT = "created_at";

    public DatabaseDonHang(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBillTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PRODUCT_ID + " INTEGER, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                COLUMN_CUSTOMER_NAME + " TEXT NOT NULL, " +
                COLUMN_CUSTOMER_CONTACT + " TEXT, " +
                COLUMN_IMEI + " TEXT NOT NULL, " +
                COLUMN_COMPANY + " TEXT NOT NULL, " +
                COLUMN_SERIES + " TEXT NOT NULL, " +
                COLUMN_MODEL + " TEXT NOT NULL, " +
                COLUMN_PRICE + " REAL NOT NULL DEFAULT 0.00, " +
                COLUMN_IMPORT_PRICE + " REAL DEFAULT 0.00, " +
                COLUMN_QTY + " INTEGER DEFAULT 1, " +
                COLUMN_TOTAL_PAID + " REAL NOT NULL DEFAULT 0.00, " +
                COLUMN_STATUS + " TEXT DEFAULT 'Đã duyệt', " +
                COLUMN_CREATED_AT + " DATETIME DEFAULT (datetime('now','localtime')))";
        db.execSQL(createBillTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public double getMonthlyRevenue(String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT SUM(" + COLUMN_TOTAL_PAID + ") FROM " + TABLE_NAME + 
                     " WHERE strftime('%Y-%m', " + COLUMN_DATE + ") = ? AND " + COLUMN_STATUS + " = 'Đã duyệt'";
        Cursor cursor = db.rawQuery(sql, new String[]{month});
        double total = 0;
        if (cursor.moveToFirst()) total = cursor.getDouble(0);
        cursor.close();
        return total;
    }

    public double getMonthlyProfit(String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT SUM((" + COLUMN_PRICE + " - " + COLUMN_IMPORT_PRICE + ") * " + COLUMN_QTY + ") FROM " + TABLE_NAME + 
                     " WHERE strftime('%Y-%m', " + COLUMN_DATE + ") = ? AND " + COLUMN_STATUS + " = 'Đã duyệt'";
        Cursor cursor = db.rawQuery(sql, new String[]{month});
        double profit = 0;
        if (cursor.moveToFirst()) profit = cursor.getDouble(0);
        cursor.close();
        return profit;
    }

    public int getMonthlyExportQuantity(String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT SUM(" + COLUMN_QTY + ") FROM " + TABLE_NAME + 
                     " WHERE strftime('%Y-%m', " + COLUMN_DATE + ") = ? AND " + COLUMN_STATUS + " = 'Đã duyệt'";
        Cursor cursor = db.rawQuery(sql, new String[]{month});
        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getMonthlyExportQtyByProduct(String model, String month) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT SUM(" + COLUMN_QTY + ") FROM " + TABLE_NAME + 
                     " WHERE " + COLUMN_MODEL + " = ? AND strftime('%Y-%m', " + COLUMN_DATE + ") = ? AND " + COLUMN_STATUS + " = 'Đã duyệt'";
        Cursor cursor = db.rawQuery(sql, new String[]{model, month});
        int total = 0;
        if (cursor.moveToFirst()) total = cursor.getInt(0);
        cursor.close();
        return total;
    }

    public List<Order> searchOrders(String query, String fromDate, String toDate) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder sql = new StringBuilder("SELECT * FROM " + TABLE_NAME + " WHERE 1=1");
        List<String> args = new ArrayList<>();

        if (query != null && !query.isEmpty()) {
            sql.append(" AND (" + COLUMN_CUSTOMER_NAME + " LIKE ? OR " + COLUMN_ID + " LIKE ?)");
            args.add("%" + query + "%"); args.add("%" + query + "%");
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append(" AND date(" + COLUMN_DATE + ") >= date(?)");
            args.add(fromDate);
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql.append(" AND date(" + COLUMN_DATE + ") <= date(?)");
            args.add(toDate);
        }

        sql.append(" ORDER BY " + COLUMN_ID + " DESC");
        Cursor cursor = db.rawQuery(sql.toString(), args.toArray(new String[0]));

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                    String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_CUSTOMER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_STATUS)),
                    String.format("%,.0f đ", cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_PAID))),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QTY))
                );
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderList;
    }

    public void updateOrderStatus(String orderId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, newStatus);
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{orderId});
    }

    public void deleteOrder(String orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{orderId});
    }

    public long addOrder(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!values.containsKey(COLUMN_STATUS)) {
            values.put(COLUMN_STATUS, "Đã duyệt");
        }
        return db.insert(TABLE_NAME, null, values);
    }
}
