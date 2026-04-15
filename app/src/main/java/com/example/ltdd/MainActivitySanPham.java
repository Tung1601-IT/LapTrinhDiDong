package com.example.ltdd;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltdd.adapter.ProductAdapter;
import com.example.ltdd.database.DatabaseSanPham;
import com.example.ltdd.model.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivitySanPham extends AppCompatActivity implements ProductAdapter.OnProductListener {

    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private DatabaseSanPham dbHelper;
    private EditText etSearch;
    private Button btnAdd, btnImport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_san_pham); // Đảm bảo layout này tồn tại

        dbHelper = new DatabaseSanPham(this);
        rvProducts = findViewById(R.id.rvProducts);
        etSearch = findViewById(R.id.etSearch);
        btnAdd = findViewById(R.id.btnAdd);
        btnImport = findViewById(R.id.btnImport);

        if (rvProducts != null) {
            rvProducts.setLayoutManager(new LinearLayoutManager(this));
            loadProducts();
        }

        if (btnAdd != null) {
            btnAdd.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivitySanPham.this, AddProductActivity.class);
                startActivity(intent);
            });
        }

        if (btnImport != null) {
            btnImport.setOnClickListener(v -> showImportDialog());
        }

        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchProducts(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private String generateBillCode() {
        String datePart = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        int randomPart = new Random().nextInt(9000) + 1000;
        return "NH-" + datePart + "-" + randomPart;
    }

    private void showImportDialog() {
        List<Product> allProducts = dbHelper.getAllProducts();
        if (allProducts.isEmpty()) {
            Toast.makeText(this, "Chưa có sản phẩm nào để nhập hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_import, null);
        Spinner spinner = dialogView.findViewById(R.id.dialog_spProduct);
        EditText etSupplier = dialogView.findViewById(R.id.dialog_etSupplier);
        EditText etPhone = dialogView.findViewById(R.id.dialog_etPhone);
        EditText etQty = dialogView.findViewById(R.id.dialog_etQty);
        EditText etPrice = dialogView.findViewById(R.id.dialog_etImportPrice);

        List<String> productNames = new ArrayList<>();
        for (Product p : allProducts) {
            productNames.add(p.getModel() + " (" + p.getImei() + ")");
        }

        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, productNames));

        new AlertDialog.Builder(this)
                .setTitle("Nhập hàng")
                .setView(dialogView)
                .setPositiveButton("Xác nhận", (dialog, which) -> {
                    try {
                        int selectedPos = spinner.getSelectedItemPosition();
                        Product p = allProducts.get(selectedPos);
                        
                        String supplier = etSupplier.getText().toString().trim();
                        String phone = etPhone.getText().toString().trim();
                        String qtyStr = etQty.getText().toString().trim();
                        String priceStr = etPrice.getText().toString().trim();

                        if (supplier.isEmpty() || qtyStr.isEmpty() || priceStr.isEmpty()) {
                            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int addQty = Integer.parseInt(qtyStr);
                        double importPrice = Double.parseDouble(priceStr);
                        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                        String billCode = generateBillCode();

                        p.setQuantity(p.getQuantity() + addQty);
                        dbHelper.updateProduct(p);

                        dbHelper.addImportRecord(billCode, p.getModel(), supplier, phone, addQty, importPrice, date);

                        loadProducts();
                        Toast.makeText(this, "Nhập hàng thành công! Mã đơn: " + billCode, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    private void loadProducts() {
        List<Product> productList = dbHelper.getAllProducts();
        if (adapter == null) {
            adapter = new ProductAdapter(productList, this);
            rvProducts.setAdapter(adapter);
        } else {
            adapter.setProductList(productList);
        }
    }

    private void searchProducts(String query) {
        List<Product> filteredList = dbHelper.searchProducts(query);
        if (adapter != null) {
            adapter.setProductList(filteredList);
        }
    }

    @Override
    public void onEditClick(Product product) {
        Intent intent = new Intent(MainActivitySanPham.this, AddProductActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteProduct(product.getId());
                    loadProducts();
                    Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
