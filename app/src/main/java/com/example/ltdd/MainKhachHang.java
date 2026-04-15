package com.example.ltdd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ltdd.adapter.KhachhangAdapter;
import com.example.ltdd.database.DatabaseKhachHang;
import com.example.ltdd.model.Khachhang;

import java.util.ArrayList;
import java.util.Calendar;

public class MainKhachHang extends AppCompatActivity {

    ListView lvkhachhang;
    ArrayList<Khachhang> listkh;
    KhachhangAdapter adapter;
    DatabaseKhachHang db;
    Button btnthem, btnloc, btnreset;
    TextView soluong;
    EditText edttimkiem, tungay, denngay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_khach_hang); // Đã đổi tên layout thành chữ thường

        lvkhachhang = findViewById(R.id.lvkhachhang);
        btnthem = findViewById(R.id.btnthem);
        soluong = findViewById(R.id.soluong);
        edttimkiem = findViewById(R.id.edttimkiem);
        tungay = findViewById(R.id.tungay);
        denngay = findViewById(R.id.denngay);
        btnloc = findViewById(R.id.btnloc);
        btnreset = findViewById(R.id.btnreset);

        if (findViewById(R.id.btnback) != null) {
            findViewById(R.id.btnback).setOnClickListener(v -> finish());
        }

        db = new DatabaseKhachHang(this);
        listkh = new ArrayList<>();
        adapter = new KhachhangAdapter(this, R.layout.itemkhachhang, listkh);
        lvkhachhang.setAdapter(adapter);

        laydulieu();

        btnthem.setOnClickListener(v -> hienDialogthem());

        lvkhachhang.setOnItemLongClickListener((parent, view, position, id) -> {
            Khachhang khSelected = listkh.get(position);
            xacNhanXoa(khSelected);
            return true;
        });

        edttimkiem.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                timKiem(s.toString().trim());
            }
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        tungay.setOnClickListener(v -> hienLich(tungay));
        denngay.setOnClickListener(v -> hienLich(denngay));
        btnloc.setOnClickListener(v -> locTheoNgay());
        btnreset.setOnClickListener(v -> {
            edttimkiem.setText("");
            tungay.setText("");
            denngay.setText("");
            laydulieu();
        });
    }

    private void hienDialogthem() {
        final Dialog dialog = new Dialog(MainKhachHang.this);
        dialog.setContentView(R.layout.layoutthem);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        EditText edtten = dialog.findViewById(R.id.edtten);
        EditText edtemail = dialog.findViewById(R.id.edtemail);
        EditText edtsdt = dialog.findViewById(R.id.edtsdt);
        EditText edtdiachi = dialog.findViewById(R.id.edtdiachi);
        RadioButton rbnam = dialog.findViewById(R.id.rbnam);
        Button btnluu = dialog.findViewById(R.id.btnluu);
        Button btnhuy = dialog.findViewById(R.id.btnhuy);

        btnhuy.setOnClickListener(v -> dialog.dismiss());

        btnluu.setOnClickListener(v -> {
            String name = edtten.getText().toString().trim();
            String email = edtemail.getText().toString().trim();
            String phone = edtsdt.getText().toString().trim();
            String address = edtdiachi.getText().toString().trim();
            String gender = rbnam.isChecked() ? "Nam" : "Nữ";

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
                Toast.makeText(MainKhachHang.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!email.matches("[a-zA-Z0-9._]+@gmail\\.com")) {
                Toast.makeText(MainKhachHang.this, "Email phải có định dạng @gmail.com!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!phone.matches("0[0-9]{9}")) {
                Toast.makeText(MainKhachHang.this, "SĐT phải có 10 số và bắt đầu bằng số 0!", Toast.LENGTH_SHORT).show();
                return;
            }

            Cursor cursor = db.Laydulieu("SELECT * FROM customer WHERE customer_contact_no = '" + phone + "'");
            if (cursor != null && cursor.getCount() > 0) {
                Toast.makeText(MainKhachHang.this, "Số điện thoại này đã tồn tại!", Toast.LENGTH_SHORT).show();
                cursor.close();
                return;
            }
            if (cursor != null) cursor.close();

            String sql = "INSERT INTO customer (customer_name, customer_gender, customer_email, customer_contact_no, customer_address) " +
                    "VALUES ('" + name + "', '" + gender + "', '" + email + "', '" + phone + "', '" + address + "')";
            try {
                db.Thucthi(sql);
                Toast.makeText(MainKhachHang.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                laydulieu();
                dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(MainKhachHang.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();
    }

    public void hienDialogSua(Khachhang kh) {
        final Dialog dialog = new Dialog(MainKhachHang.this);
        dialog.setContentView(R.layout.layoutthem);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        TextView tieude = dialog.findViewById(R.id.tieude);
        if (tieude != null) {
            tieude.setText("SỬA KHÁCH HÀNG");
        }
        EditText edtten = dialog.findViewById(R.id.edtten);
        EditText edtemail = dialog.findViewById(R.id.edtemail);
        EditText edtsdt = dialog.findViewById(R.id.edtsdt);
        EditText edtdiachi = dialog.findViewById(R.id.edtdiachi);
        RadioButton rbnam = dialog.findViewById(R.id.rbnam);
        RadioButton rbnu = dialog.findViewById(R.id.rbnu);
        Button btnluu = dialog.findViewById(R.id.btnluu);
        Button btnhuy = dialog.findViewById(R.id.btnhuy);

        edtten.setText(kh.getCustomer_name());
        edtemail.setText(kh.getCustomer_email());
        edtsdt.setText(kh.getCustomer_contact_no());
        edtdiachi.setText(kh.getCustomer_address());
        if (kh.getCustomer_gender().equalsIgnoreCase("Nam")) rbnam.setChecked(true);
        else rbnu.setChecked(true);

        btnhuy.setOnClickListener(v -> dialog.dismiss());

        btnluu.setOnClickListener(v -> {
            String name = edtten.getText().toString().trim();
            String email = edtemail.getText().toString().trim();
            String phone = edtsdt.getText().toString().trim();
            String address = edtdiachi.getText().toString().trim();
            String gender = rbnam.isChecked() ? "Nam" : "Nữ";

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!email.matches("[a-zA-Z0-9._]+@gmail\\.com") || !phone.matches("0[0-9]{9}")) {
                Toast.makeText(this, "Sai định dạng Email hoặc SĐT!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!phone.equals(kh.getCustomer_contact_no())) {
                Cursor cursor = db.Laydulieu("SELECT * FROM customer WHERE customer_contact_no = '" + phone + "'");
                if (cursor != null && cursor.getCount() > 0) {
                    Toast.makeText(this, "SĐT mới đã có người khác dùng!", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    return;
                }
                if (cursor != null) cursor.close();
            }

            String sqlUpdate = "UPDATE customer SET " +
                    "customer_name = '" + name + "', " +
                    "customer_gender = '" + gender + "', " +
                    "customer_email = '" + email + "', " +
                    "customer_contact_no = '" + phone + "', " +
                    "customer_address = '" + address + "' " +
                    "WHERE customer_id = " + kh.getCustomer_id();

            try {
                db.Thucthi(sqlUpdate);
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                laydulieu();
                dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();
    }

    public void xacNhanXoa(Khachhang kh) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa khách hàng: " + kh.getCustomer_name() + " không?");
        builder.setIcon(android.R.drawable.ic_delete);

        builder.setPositiveButton("Có", (dialog, which) -> {
            String sqlDelete = "DELETE FROM customer WHERE customer_id = " + kh.getCustomer_id();
            try {
                db.Thucthi(sqlDelete);
                Toast.makeText(this, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
                laydulieu();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khi xóa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void timKiem(String query) {
        listkh.clear();
        String sql = "SELECT * FROM customer WHERE customer_name LIKE '%" + query + "%' " +
                "OR customer_contact_no LIKE '%" + query + "%'";

        Cursor cursor = db.Laydulieu(sql);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                listkh.add(new Khachhang(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6)
                ));
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
        soluong.setText(listkh.size() + " kết quả");
    }

    private void hienLich(EditText edt) {
        Calendar c = Calendar.getInstance();
        int d = c.get(Calendar.DAY_OF_MONTH);
        int m = c.get(Calendar.MONTH);
        int y = c.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String ngay = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
            edt.setText(ngay);
        }, y, m, d);
        dialog.show();
    }

    private void locTheoNgay() {
        String tu = tungay.getText().toString();
        String den = denngay.getText().toString();

        if (tu.isEmpty() || den.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn cả Từ ngày và Đến ngày!", Toast.LENGTH_SHORT).show();
            return;
        }

        listkh.clear();
        String sql = "SELECT * FROM customer WHERE created_at BETWEEN '" + tu + " 00:00:00' AND '" + den + " 23:59:59'";

        Cursor cursor = db.Laydulieu(sql);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                listkh.add(new Khachhang(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6)
                ));
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
        soluong.setText(listkh.size() + " khách hàng");
    }

    private void laydulieu() {
        listkh.clear();
        Cursor cursor = db.Laydulieu("SELECT * FROM customer");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                listkh.add(new Khachhang(
                        cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6)
                ));
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
        soluong.setText(listkh.size() + " khách hàng");
    }
}
