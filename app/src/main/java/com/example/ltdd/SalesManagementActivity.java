package com.example.ltdd;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltdd.adapter.InvoiceAdapter;
import com.example.ltdd.database.DatabaseDonHang;
import com.example.ltdd.model.Invoice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SalesManagementActivity extends AppCompatActivity {

    private DatabaseDonHang dbDonHang;
    private TextView tvTotalInvoices, tvTotalRevenue, tvFromDate, tvToDate;
    private RecyclerView rvInvoices;
    private InvoiceAdapter adapter;
    private String selectedFromDate = "", selectedToDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_management);

        dbDonHang = new DatabaseDonHang(this);
        initViews();
        setupFilters();
        loadData();
    }

    private void initViews() {
        if (findViewById(R.id.btnBack) != null) {
            findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        }

        tvTotalInvoices = findViewById(R.id.tvTotalInvoices);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvFromDate = findViewById(R.id.tvFromDate);
        tvToDate = findViewById(R.id.tvToDate);
        rvInvoices = findViewById(R.id.rvInvoices);
        ImageButton btnFilter = findViewById(R.id.btnFilter);
        ImageButton btnClearFilter = findViewById(R.id.btnClearFilter);

        rvInvoices.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InvoiceAdapter(new ArrayList<>());
        rvInvoices.setAdapter(adapter);

        if (btnFilter != null) btnFilter.setOnClickListener(v -> loadData());
        if (btnClearFilter != null) {
            btnClearFilter.setOnClickListener(v -> {
                selectedFromDate = "";
                selectedToDate = "";
                tvFromDate.setText("");
                tvToDate.setText("");
                loadData();
            });
        }
    }

    private void setupFilters() {
        if (tvFromDate != null) tvFromDate.setOnClickListener(v -> showDatePicker(true));
        if (tvToDate != null) tvToDate.setOnClickListener(v -> showDatePicker(false));
    }

    private void showDatePicker(boolean isFromDate) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            if (isFromDate) {
                selectedFromDate = date;
                tvFromDate.setText(date);
            } else {
                selectedToDate = date;
                tvToDate.setText(date);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void loadData() {
        loadSummary();
        loadInvoices();
    }

    private void loadSummary() {
        SQLiteDatabase db = dbDonHang.getReadableDatabase();
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(*) FROM " + DatabaseDonHang.TABLE_NAME + " WHERE 1=1");
        StringBuilder sqlSum = new StringBuilder("SELECT SUM(" + DatabaseDonHang.COLUMN_TOTAL_PAID + ") FROM " + DatabaseDonHang.TABLE_NAME + " WHERE 1=1");
        List<String> args = new ArrayList<>();

        if (!selectedFromDate.isEmpty()) {
            sqlCount.append(" AND date(" + DatabaseDonHang.COLUMN_DATE + ") >= date(?)");
            sqlSum.append(" AND date(" + DatabaseDonHang.COLUMN_DATE + ") >= date(?)");
            args.add(selectedFromDate);
        }
        if (!selectedToDate.isEmpty()) {
            sqlCount.append(" AND date(" + DatabaseDonHang.COLUMN_DATE + ") <= date(?)");
            sqlSum.append(" AND date(" + DatabaseDonHang.COLUMN_DATE + ") <= date(?)");
            args.add(selectedToDate);
        }

        Cursor cursorTotal = db.rawQuery(sqlCount.toString(), args.toArray(new String[0]));
        if (cursorTotal.moveToFirst()) {
            tvTotalInvoices.setText(String.valueOf(cursorTotal.getInt(0)));
        }
        cursorTotal.close();

        Cursor cursorRevenue = db.rawQuery(sqlSum.toString(), args.toArray(new String[0]));
        if (cursorRevenue.moveToFirst()) {
            double total = cursorRevenue.getDouble(0);
            tvTotalRevenue.setText(String.format("%,.0f VND", total).replace(',', '.'));
        }
        cursorRevenue.close();
    }

    private void loadInvoices() {
        List<Invoice> invoiceList = new ArrayList<>();
        SQLiteDatabase db = dbDonHang.getReadableDatabase();
        StringBuilder sql = new StringBuilder("SELECT * FROM " + DatabaseDonHang.TABLE_NAME + " WHERE 1=1");
        List<String> args = new ArrayList<>();

        if (!selectedFromDate.isEmpty()) {
            sql.append(" AND date(" + DatabaseDonHang.COLUMN_DATE + ") >= date(?)");
            args.add(selectedFromDate);
        }
        if (!selectedToDate.isEmpty()) {
            sql.append(" AND date(" + DatabaseDonHang.COLUMN_DATE + ") <= date(?)");
            args.add(selectedToDate);
        }
        sql.append(" ORDER BY " + DatabaseDonHang.COLUMN_ID + " DESC");

        Cursor cursor = db.rawQuery(sql.toString(), args.toArray(new String[0]));
        if (cursor.moveToFirst()) {
            do {
                String id = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_ID)));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_CUSTOMER_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_CUSTOMER_CONTACT));
                String product = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_MODEL));
                String imei = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_IMEI));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_TOTAL_PAID));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseDonHang.COLUMN_DATE));

                invoiceList.add(new Invoice(
                        id,
                        name,
                        phone,
                        product,
                        imei,
                        String.format("%,.0f VND", amount).replace(',', '.'),
                        date
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.updateData(invoiceList);
    }
}
