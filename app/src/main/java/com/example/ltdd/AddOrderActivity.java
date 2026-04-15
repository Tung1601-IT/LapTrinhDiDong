package com.example.ltdd;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;

import com.example.ltdd.database.DatabaseDonHang;
import com.example.ltdd.database.DatabaseSanPham;
import com.example.ltdd.model.Product;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddOrderActivity extends AppCompatActivity {

    private EditText etOrderDate, etCustomerName, etCustomerPhone, etCustomerAddress;
    private EditText etSeries, etModel, etPrice, etQuantity, etTotalAmount;
    private Spinner spinnerProduct;
    private Button btnSubmit;

    private DatabaseSanPham dbSanPham;
    private DatabaseDonHang dbDonHang;
    private List<Product> productList;
    private Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        dbSanPham = new DatabaseSanPham(this);
        dbDonHang = new DatabaseDonHang(this);

        initViews();
        setupDatePicker();
        loadProducts();
        setupListeners();
    }

    private void initViews() {
        etOrderDate = findViewById(R.id.etOrderDate);
        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerPhone = findViewById(R.id.etCustomerPhone);
        etCustomerAddress = findViewById(R.id.etCustomerAddress);
        etSeries = findViewById(R.id.etSeries);
        etModel = findViewById(R.id.etModel);
        etPrice = findViewById(R.id.etPrice);
        etQuantity = findViewById(R.id.etQuantity);
        etTotalAmount = findViewById(R.id.etTotalAmount);
        spinnerProduct = findViewById(R.id.spinnerProduct);
        btnSubmit = findViewById(R.id.btnSubmit);

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        etOrderDate.setText(currentDate);
    }

    private void setupDatePicker() {
        etOrderDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                etOrderDate.setText(date);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void loadProducts() {
        productList = dbSanPham.getAllProducts();
        String[] productNames = new String[productList.size() + 1];
        productNames[0] = "-- Chọn điện thoại --";
        for (int i = 0; i < productList.size(); i++) {
            productNames[i + 1] = productList.get(i).getModel();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProduct.setAdapter(adapter);
    }

    private void setupListeners() {
        spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedProduct = productList.get(position - 1);
                    etSeries.setText(selectedProduct.getProductLine());
                    etModel.setText(selectedProduct.getModel());
                    etPrice.setText(String.format("%,.0f", selectedProduct.getPrice()).replace(',', '.'));
                    calculateTotal();
                } else {
                    selectedProduct = null;
                    clearProductFields();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { calculateTotal(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnSubmit.setOnClickListener(v -> saveOrder());
    }

    private void clearProductFields() {
        etSeries.setText("");
        etModel.setText("");
        etPrice.setText("");
        etTotalAmount.setText("");
    }

    private void calculateTotal() {
        if (selectedProduct != null) {
            try {
                String qtyStr = etQuantity.getText().toString();
                int qty = qtyStr.isEmpty() ? 0 : Integer.parseInt(qtyStr);
                double total = selectedProduct.getPrice() * qty;
                etTotalAmount.setText(String.format("%,.0f", total).replace(',', '.'));
            } catch (Exception e) {
                etTotalAmount.setText("");
            }
        }
    }

    private void saveOrder() {
        String name = etCustomerName.getText().toString().trim();
        String phone = etCustomerPhone.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();

        if (name.isEmpty() || selectedProduct == null || qtyStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int qty = Integer.parseInt(qtyStr);
        if (qty > selectedProduct.getQuantity()) {
            Toast.makeText(this, "Số lượng trong kho không đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseDonHang.COLUMN_DATE, etOrderDate.getText().toString());
        values.put(DatabaseDonHang.COLUMN_CUSTOMER_NAME, name);
        values.put(DatabaseDonHang.COLUMN_CUSTOMER_CONTACT, phone);
        values.put(DatabaseDonHang.COLUMN_PRODUCT_ID, selectedProduct.getId());
        values.put(DatabaseDonHang.COLUMN_COMPANY, selectedProduct.getProductLine());
        values.put(DatabaseDonHang.COLUMN_SERIES, etSeries.getText().toString());
        values.put(DatabaseDonHang.COLUMN_MODEL, etModel.getText().toString());
        values.put(DatabaseDonHang.COLUMN_IMEI, selectedProduct.getImei());
        values.put(DatabaseDonHang.COLUMN_PRICE, selectedProduct.getPrice());
        values.put(DatabaseDonHang.COLUMN_QTY, qty);
        values.put(DatabaseDonHang.COLUMN_TOTAL_PAID, selectedProduct.getPrice() * qty);
        values.put(DatabaseDonHang.COLUMN_STATUS, "Đã duyệt"); // Thiết lập Đã duyệt ngay từ đầu

        long id = dbDonHang.addOrder(values);
        if (id > 0) {
            dbSanPham.updateProductStock(selectedProduct.getId(), selectedProduct.getQuantity() - qty);
            Toast.makeText(this, "Thêm đơn hàng thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi thêm đơn hàng", Toast.LENGTH_SHORT).show();
        }
    }
}
