package com.example.ltdd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltdd.adapter.RepairOrderAdapter;
import com.example.ltdd.database.DatabaseRepair;
import com.example.ltdd.model.RepairOrder;

import java.util.List;

public class RepairManagementActivity extends AppCompatActivity {

    private DatabaseRepair databaseRepair;
    private RecyclerView rvRepairOrders;
    private Button btnTabList, btnTabHistory, btnCreateRepair, btnEditSelected, btnDeleteSelected;
    private ImageButton btnBack;
    private boolean isHistoryTab = false;
    private RepairOrder selectedOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_management);

        databaseRepair = new DatabaseRepair(this);

        rvRepairOrders = findViewById(R.id.rvRepairOrders);
        btnTabList = findViewById(R.id.btnTabList);
        btnTabHistory = findViewById(R.id.btnTabHistory);
        btnCreateRepair = findViewById(R.id.btnCreateRepair);
        btnEditSelected = findViewById(R.id.btnEditSelected);
        btnDeleteSelected = findViewById(R.id.btnDeleteSelected);
        btnBack = findViewById(R.id.btnBackManagement);

        rvRepairOrders.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());
        btnTabList.setOnClickListener(v -> {
            isHistoryTab = false;
            selectedOrder = null;
            updateTabState();
            loadData();
        });

        btnTabHistory.setOnClickListener(v -> {
            isHistoryTab = true;
            selectedOrder = null;
            updateTabState();
            loadData();
        });

        btnCreateRepair.setOnClickListener(v -> {
            Intent intent = new Intent(RepairManagementActivity.this, RepairFormActivity.class);
            startActivity(intent);
        });

        btnEditSelected.setOnClickListener(v -> editSelectedOrder());
        btnDeleteSelected.setOnClickListener(v -> deleteSelectedOrder());

        updateTabState();
        loadData();
    }

    private void loadData() {
        List<RepairOrder> list;

        if (isHistoryTab) {
            list = databaseRepair.getCompletedRepairOrders();
        } else {
            list = databaseRepair.getActiveRepairOrders();
        }

        RepairOrderAdapter adapter = new RepairOrderAdapter(
                list,
                isHistoryTab,
                order -> selectedOrder = order
        );

        rvRepairOrders.setAdapter(adapter);
        updateBottomActionsState();
    }

    private void editSelectedOrder() {
        if (selectedOrder == null) {
            Toast.makeText(this, "Hãy chọn một phiếu trước", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isHistoryTab) {
            Toast.makeText(this, "Tab lịch sử không sửa dữ liệu", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, RepairFormActivity.class);
        intent.putExtra("REPAIR_ID", selectedOrder.getId());
        startActivity(intent);
    }

    private void deleteSelectedOrder() {
        if (selectedOrder == null) {
            Toast.makeText(this, "Hãy chọn một phiếu trước", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isHistoryTab) {
            Toast.makeText(this, "Tab lịch sử không xóa dữ liệu", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc muốn xóa phiếu này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    databaseRepair.deleteRepairOrder(selectedOrder.getId());
                    selectedOrder = null;
                    loadData();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void updateTabState() {
        if (isHistoryTab) {
            btnTabList.setBackgroundResource(R.drawable.bg_repair_tab_idle);
            btnTabList.setTextColor(0xFF5C7390);
            btnTabHistory.setBackgroundResource(R.drawable.bg_repair_tab_selected);
            btnTabHistory.setTextColor(0xFFFFFFFF);
        } else {
            btnTabList.setBackgroundResource(R.drawable.bg_repair_tab_selected);
            btnTabList.setTextColor(0xFFFFFFFF);
            btnTabHistory.setBackgroundResource(R.drawable.bg_repair_tab_idle);
            btnTabHistory.setTextColor(0xFF5C7390);
        }
        updateBottomActionsState();
    }

    private void updateBottomActionsState() {
        int visibility = isHistoryTab ? Button.GONE : Button.VISIBLE;
        btnEditSelected.setVisibility(visibility);
        btnDeleteSelected.setVisibility(visibility);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedOrder = null;
        loadData();
    }
}
