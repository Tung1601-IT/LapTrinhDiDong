package com.example.ltdd;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ltdd.adapter.InventoryReportAdapter;
import com.example.ltdd.database.DatabaseDonHang;
import com.example.ltdd.database.DatabaseSanPham;
import com.example.ltdd.model.InventoryReport;
import com.example.ltdd.model.Product;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    private TextView tvSelectedMonth;
    private EditText etSearchProduct;
    private RecyclerView rvReport;
    private InventoryReportAdapter adapter;
    private DatabaseDonHang dbDonHang;
    private DatabaseSanPham dbSanPham;
    private Calendar calendar;
    private List<InventoryReport> fullReportList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        dbDonHang = new DatabaseDonHang(this);
        dbSanPham = new DatabaseSanPham(this);
        calendar = Calendar.getInstance();

        initViews();
        loadReportData();
    }

    private void initViews() {
        tvSelectedMonth = findViewById(R.id.tvSelectedMonth);
        etSearchProduct = findViewById(R.id.etSearchProduct);
        rvReport = findViewById(R.id.rvReport);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnChooseMonth).setOnClickListener(v -> showMonthPicker());

        rvReport.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InventoryReportAdapter(new ArrayList<>());
        rvReport.setAdapter(adapter);

        etSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterReport(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        updateMonthDisplay();
    }

    private void updateMonthDisplay() {
        String monthStr = String.format(Locale.getDefault(), "Tháng %02d/%04d", 
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        tvSelectedMonth.setText(monthStr);
    }

    private void showMonthPicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    updateMonthDisplay();
                    loadReportData();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void loadReportData() {
        fullReportList.clear();
        List<Product> products = dbSanPham.getAllProducts();
        
        String monthStr = String.format(Locale.getDefault(), "%04d-%02d", 
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
        String monthYearStr = String.format(Locale.getDefault(), "%02d-%04d", 
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        for (Product p : products) {
            int imp = dbSanPham.getMonthlyImportQtyByProduct(p.getModel(), monthYearStr);
            int exp = dbDonHang.getMonthlyExportQtyByProduct(p.getModel(), monthStr);
            
            // Tồn cuối = Tồn hiện tại (giả định đây là báo cáo tháng hiện tại)
            // Tồn đầu = Tồn cuối - Nhập + Xuất
            int closing = p.getQuantity();
            int opening = closing - imp + exp;

            fullReportList.add(new InventoryReport(p.getModel(), opening, imp, exp, closing));
        }
        adapter.updateData(fullReportList);
    }

    private void filterReport(String query) {
        List<InventoryReport> filtered = new ArrayList<>();
        for (InventoryReport item : fullReportList) {
            if (item.getProductName().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(item);
            }
        }
        adapter.updateData(filtered);
    }
}
