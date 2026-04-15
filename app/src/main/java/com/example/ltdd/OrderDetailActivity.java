package com.example.ltdd;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ltdd.database.DatabaseDonHang;

public class OrderDetailActivity extends AppCompatActivity {
    private DatabaseDonHang dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        dbHelper = new DatabaseDonHang(this);

        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        String orderId = getIntent().getStringExtra("ORDER_ID");
        if (orderId != null && !orderId.isEmpty()) {
            loadOrderDetail(orderId);
        }
    }

    private void loadOrderDetail(String orderId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT *, datetime(" + DatabaseDonHang.COLUMN_CREATED_AT + ") as full_date FROM " + DatabaseDonHang.TABLE_NAME + " WHERE " + DatabaseDonHang.COLUMN_ID + " = ?", new String[]{orderId});

        if (cursor.moveToFirst()) {
            ((TextView) findViewById(R.id.tvDetailOrderId)).setText("#DH" + orderId);
            ((TextView) findViewById(R.id.tvDetailCustomerName)).setText("Tên: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_CUSTOMER_NAME)));
            ((TextView) findViewById(R.id.tvDetailPhone)).setText("SĐT: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_CUSTOMER_CONTACT)));
            ((TextView) findViewById(R.id.tvDetailProductModel)).setText("Mẫu: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_MODEL)));
            ((TextView) findViewById(R.id.tvDetailCompany)).setText("Hãng: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_COMPANY)));
            ((TextView) findViewById(R.id.tvDetailIMEI)).setText("IMEI: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_IMEI)));

            String displayDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_DATE));
            if (displayDate != null && displayDate.length() <= 10) {
                displayDate = cursor.getString(cursor.getColumnIndexOrThrow("full_date"));
            }
            ((TextView) findViewById(R.id.tvDetailDate)).setText(displayDate);

            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_PRICE));
            double paid = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_TOTAL_PAID));
            int qty = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_QTY));

            ((TextView) findViewById(R.id.tvDetailPrice)).setText(String.format("%,.0fđ", price));
            ((TextView) findViewById(R.id.tvDetailPaid)).setText(String.format("%,.0fđ", paid));
            
            TextView tvQuantity = findViewById(R.id.tvDetailQuantity);
            if (tvQuantity != null) {
                tvQuantity.setText(String.valueOf(qty));
            }
        }
        cursor.close();
    }
}
