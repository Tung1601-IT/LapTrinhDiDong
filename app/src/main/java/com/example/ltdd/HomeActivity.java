package com.example.ltdd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltdd.adapter.HomeAdapter;
import com.example.ltdd.model.HomeItem;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbarHome);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("TRANG CHỦ - HOME");
            }
        }

        RecyclerView rv = findViewById(R.id.rvHomeFunctions);
        List<HomeItem> list = new ArrayList<>();

        list.add(new HomeItem("Đơn hàng", android.R.drawable.ic_menu_agenda, Color.parseColor("#4285F4")));
        list.add(new HomeItem("Quản lý bán hàng", android.R.drawable.ic_menu_save, Color.parseColor("#34A853")));
        list.add(new HomeItem("Khách hàng", android.R.drawable.ic_menu_myplaces, Color.parseColor("#FBBC05")));
        list.add(new HomeItem("Sản phẩm", android.R.drawable.ic_menu_gallery, Color.parseColor("#EA4335")));
        list.add(new HomeItem("Báo cáo thống kê", android.R.drawable.ic_menu_sort_by_size, Color.parseColor("#8E24AA")));
        list.add(new HomeItem("Đánh giá", android.R.drawable.ic_btn_speak_now, Color.parseColor("#00ACC1")));
        list.add(new HomeItem("Khuyến mãi", android.R.drawable.ic_menu_today, Color.parseColor("#FF7043")));
        list.add(new HomeItem("Sửa chữa", android.R.drawable.ic_menu_manage, Color.parseColor("#795548")));
        list.add(new HomeItem("Đăng xuất", android.R.drawable.ic_lock_power_off, Color.parseColor("#607D8B")));

        HomeAdapter adapter = new HomeAdapter(list, item -> {
            switch (item.getTitle()) {
                case "Đơn hàng":
                    startActivity(new Intent(this, OrderManagementActivity.class));
                    break;
                case "Quản lý bán hàng":
                    startActivity(new Intent(this, SalesManagementActivity.class));
                    break;
                case "Khách hàng":
                    startActivity(new Intent(this, MainKhachHang.class));
                    break;
                case "Sản phẩm":
                    startActivity(new Intent(this, MainActivitySanPham.class));
                    break;
                case "Báo cáo thống kê":
                    startActivity(new Intent(this, ReportActivity.class));
                    break;
                case "Sửa chữa":
                    startActivity(new Intent(this, RepairManagementActivity.class));
                    break;
                case "Đăng xuất":
                    performLogout();
                    break;
                default:
                    Toast.makeText(this, "Bạn đã chọn chức năng: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        rv.setLayoutManager(new GridLayoutManager(this, 3));
        rv.setAdapter(adapter);
    }

    private void performLogout() {
        // Xóa trạng thái đăng nhập trong SharedPreferences
        SharedPreferences preferences = getSharedPreferences("LTDD_PREFS", MODE_PRIVATE);
        preferences.edit().clear().apply();

        // Quay lại màn hình Login và xóa hết các Activity khác trong stack
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
