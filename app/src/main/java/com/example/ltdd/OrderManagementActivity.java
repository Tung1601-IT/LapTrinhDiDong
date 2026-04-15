package com.example.ltdd;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltdd.adapter.OrderAdapter;
import com.example.ltdd.database.DatabaseDonHang;
import com.example.ltdd.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderManagementActivity extends AppCompatActivity {
    private DatabaseDonHang dbHelper;
    private RecyclerView rvOrders;
    private OrderAdapter adapter;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);

        dbHelper = new DatabaseDonHang(this);
        initViews();
        setupAdapter();
    }

    private void initViews() {
        rvOrders = findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));

        etSearch = findViewById(R.id.etSearch);
        Button btnAddOrder = findViewById(R.id.btnAddOrder);
        Button btnSearch = findViewById(R.id.btnSearch);

        if (btnAddOrder != null) {
            btnAddOrder.setOnClickListener(v -> {
                Intent intent = new Intent(OrderManagementActivity.this, AddOrderActivity.class);
                startActivity(intent);
            });
        }

        if (btnSearch != null) {
            btnSearch.setOnClickListener(v -> loadOrders());
        }

        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    loadOrders();
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void setupAdapter() {
        adapter = new OrderAdapter(new ArrayList<>(), new OrderAdapter.OnOrderActionListener() {
            @Override
            public void onEdit(Order order) {
                Intent intent = new Intent(OrderManagementActivity.this, OrderDetailActivity.class);
                intent.putExtra("ORDER_ID", order.getOrderId());
                startActivity(intent);
            }

            @Override
            public void onDelete(Order order) {
                new AlertDialog.Builder(OrderManagementActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa đơn hàng #" + order.getOrderId() + " không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            dbHelper.deleteOrder(order.getOrderId());
                            loadOrders();
                            Toast.makeText(OrderManagementActivity.this, "Đã xóa đơn hàng", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }

            @Override
            public void onItemClick(Order order) {
                Intent intent = new Intent(OrderManagementActivity.this, OrderDetailActivity.class);
                intent.putExtra("ORDER_ID", order.getOrderId());
                startActivity(intent);
            }
        });
        rvOrders.setAdapter(adapter);
        loadOrders();
    }

    private void loadOrders() {
        String query = etSearch != null ? etSearch.getText().toString().trim() : "";
        List<Order> orders = dbHelper.searchOrders(query, "", "");
        adapter.updateData(orders);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }
}
