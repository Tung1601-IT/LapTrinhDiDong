package com.example.ltdd;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ltdd.database.DatabaseRepair;
import com.example.ltdd.model.RepairOrder;

public class RepairFormActivity extends AppCompatActivity {

    private static final String[] STATUS_OPTIONS = {"Mới tiếp nhận", "Đang sửa", "Đã sửa"};

    private TextView tvFormTitle;
    private ImageButton btnBack;
    private EditText edtCustomerName;
    private EditText edtPhone;
    private EditText edtDevice;
    private EditText edtIssue;
    private EditText edtReceivedDate;
    private EditText edtCompletedDate;
    private Spinner spStatus;
    private Button btnSave;
    private Button btnClose;

    private DatabaseRepair databaseRepair;
    private int repairId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_form);

        databaseRepair = new DatabaseRepair(this);

        tvFormTitle = findViewById(R.id.tvFormTitle);
        btnBack = findViewById(R.id.btnBackForm);
        edtCustomerName = findViewById(R.id.edtCustomerName);
        edtPhone = findViewById(R.id.edtPhone);
        edtDevice = findViewById(R.id.edtDevice);
        edtIssue = findViewById(R.id.edtIssue);
        edtReceivedDate = findViewById(R.id.edtReceivedDate);
        edtCompletedDate = findViewById(R.id.edtCompletedDate);
        spStatus = findViewById(R.id.spStatus);
        btnSave = findViewById(R.id.btnSave);
        btnClose = findViewById(R.id.btnClose);

        setupStatusSpinner();

        repairId = getIntent().getIntExtra("REPAIR_ID", -1);

        if (repairId != -1) {
            tvFormTitle.setText("Sửa phiếu sửa chữa");
            btnSave.setText("Lưu sửa");
            loadRepairData(repairId);
        } else {
            tvFormTitle.setText("Tạo phiếu sửa chữa");
            btnSave.setText("Lưu");
            edtReceivedDate.setText("");
            edtCompletedDate.setText("");
            spStatus.setSelection(0);
        }

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveRepairOrder());
        btnClose.setOnClickListener(v -> finish());
    }

    private void setupStatusSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                STATUS_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(adapter);
    }

    private void loadRepairData(int id) {
        RepairOrder order = databaseRepair.getRepairOrderById(id);
        if (order == null) {
            return;
        }

        edtCustomerName.setText(order.getCustomerName());
        edtPhone.setText(order.getPhone());
        edtDevice.setText(order.getDevice());
        edtIssue.setText(order.getIssueDescription());
        edtReceivedDate.setText(order.getReceivedDate());
        edtCompletedDate.setText(order.getCompletedDate());
        setSpinnerValue(order.getStatus());
    }

    private void setSpinnerValue(String value) {
        for (int i = 0; i < STATUS_OPTIONS.length; i++) {
            if (STATUS_OPTIONS[i].equals(value)) {
                spStatus.setSelection(i);
                return;
            }
        }
        spStatus.setSelection(0);
    }

    private void saveRepairOrder() {
        String customerName = edtCustomerName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String device = edtDevice.getText().toString().trim();
        String issue = edtIssue.getText().toString().trim();
        String receivedDate = edtReceivedDate.getText().toString().trim();
        String status = spStatus.getSelectedItem().toString();
        String completedDate = edtCompletedDate.getText().toString().trim();

        if (TextUtils.isEmpty(customerName)
                || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(device)
                || TextUtils.isEmpty(issue)
                || TextUtils.isEmpty(receivedDate)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        RepairOrder order = new RepairOrder();
        order.setCustomerName(customerName);
        order.setPhone(phone);
        order.setDevice(device);
        order.setIssueDescription(issue);
        order.setReceivedDate(receivedDate);
        order.setStatus(status);
        order.setCompletedDate(completedDate);

        if (repairId == -1) {
            databaseRepair.insertRepairOrder(order);
            Toast.makeText(this, "Thêm phiếu thành công", Toast.LENGTH_SHORT).show();
        } else {
            order.setId(repairId);
            databaseRepair.updateRepairOrder(order);
            Toast.makeText(this, "Cập nhật phiếu thành công", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
