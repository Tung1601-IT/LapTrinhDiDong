package com.example.ltdd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ltdd.adapter.BannerAdapter;
import com.example.ltdd.adapter.ProductUserAdapter;
import com.example.ltdd.database.DatabaseSanPham;
import com.example.ltdd.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private DatabaseSanPham databaseSanPham;
    private NestedScrollView nestedScrollView;
    
    private RecyclerView rvIphone, rvIpad, rvMac, rvWatch;
    private View layoutIphone, layoutIpad, layoutMac, layoutWatch;
    private ProductUserAdapter adapterIphone, adapterIpad, adapterMac, adapterWatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        databaseSanPham = new DatabaseSanPham(this);
        initViews();
        setupBanner();
        setupCategories();
        setupNavigationMenu();
    }

    private void initViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView);
        viewPager = findViewById(R.id.viewPager);
        
        rvIphone = findViewById(R.id.rvIphone);
        rvIpad = findViewById(R.id.rvIpad);
        rvMac = findViewById(R.id.rvMac);
        rvWatch = findViewById(R.id.rvWatch);

        layoutIphone = findViewById(R.id.layoutIphone);
        layoutIpad = findViewById(R.id.layoutIpad);
        layoutMac = findViewById(R.id.layoutMac);
        layoutWatch = findViewById(R.id.layoutWatch);
        
        ImageView imgMenuMain = findViewById(R.id.imgMenuMain);
        ImageView imgCartMain = findViewById(R.id.imgCartMain);
        TextView txtLogoMain = findViewById(R.id.txtLogoMain);

        imgMenuMain.setOnClickListener(v -> showMainMenu(v));
        
        // Nhấn vào Logo để hiện lại tất cả các danh mục
        if (txtLogoMain != null) {
            txtLogoMain.setOnClickListener(v -> showAllCategories());
        }

        imgCartMain.setOnClickListener(v -> 
            Toast.makeText(this, "Giỏ hàng đang được phát triển", Toast.LENGTH_SHORT).show()
        );
        
        findViewById(R.id.fabHotline).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0379819805"));
            startActivity(intent);
        });
    }

    private void setupNavigationMenu() {
        findViewById(R.id.tvMenuIphone).setOnClickListener(v -> filterCategory("iPhone"));
        findViewById(R.id.tvMenuIpad).setOnClickListener(v -> filterCategory("iPad"));
        findViewById(R.id.tvMenuMac).setOnClickListener(v -> filterCategory("MacBook"));
        findViewById(R.id.tvMenuWatch).setOnClickListener(v -> filterCategory("Watch"));
    }

    private void filterCategory(String category) {
        // Ẩn banner khi lọc danh mục
        viewPager.setVisibility(View.GONE);
        
        // Ẩn tất cả các layout danh mục
        layoutIphone.setVisibility(View.GONE);
        layoutIpad.setVisibility(View.GONE);
        layoutMac.setVisibility(View.GONE);
        layoutWatch.setVisibility(View.GONE);

        // Chỉ hiển thị layout tương ứng với danh mục được chọn
        if (category.equals("iPhone")) layoutIphone.setVisibility(View.VISIBLE);
        else if (category.equals("iPad")) layoutIpad.setVisibility(View.VISIBLE);
        else if (category.equals("MacBook")) layoutMac.setVisibility(View.VISIBLE);
        else if (category.equals("Watch")) layoutWatch.setVisibility(View.VISIBLE);
        
        // Đưa màn hình lên đầu trang (vị trí danh mục)
        nestedScrollView.scrollTo(0, 0);
    }

    private void showAllCategories() {
        // Hiển thị lại toàn bộ thành phần trang chủ
        viewPager.setVisibility(View.VISIBLE);
        layoutIphone.setVisibility(View.VISIBLE);
        layoutIpad.setVisibility(View.VISIBLE);
        layoutMac.setVisibility(View.VISIBLE);
        layoutWatch.setVisibility(View.VISIBLE);
        nestedScrollView.scrollTo(0, 0);
    }

    private void setupBanner() {
        int[] images = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3};
        BannerAdapter adapter = new BannerAdapter(images);
        viewPager.setAdapter(adapter);
    }

    private void setupCategories() {
        List<Product> allProducts = databaseSanPham.getAllProducts();
        
        adapterIphone = createAdapter(filterByLine(allProducts, "iPhone"));
        adapterIpad = createAdapter(filterByLine(allProducts, "iPad"));
        adapterMac = createAdapter(filterByLine(allProducts, "MacBook"));
        adapterWatch = createAdapter(filterByLine(allProducts, "Watch"));

        setupRecyclerView(rvIphone, adapterIphone);
        setupRecyclerView(rvIpad, adapterIpad);
        setupRecyclerView(rvMac, adapterMac);
        setupRecyclerView(rvWatch, adapterWatch);
    }

    private List<Product> filterByLine(List<Product> list, String line) {
        List<Product> result = new ArrayList<>();
        for (Product p : list) {
            if (line.equalsIgnoreCase(p.getProductLine())) {
                result.add(p);
            }
        }
        return result;
    }

    private ProductUserAdapter createAdapter(List<Product> list) {
        return new ProductUserAdapter(list, new ProductUserAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }

            @Override
            public void onAddToCartClick(Product product) {
                Toast.makeText(MainActivity.this, "Đã thêm " + product.getModel() + " vào giỏ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBuyNowClick(Product product) {
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });
    }

    private void setupRecyclerView(RecyclerView rv, ProductUserAdapter adapter) {
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(adapter);
        rv.setNestedScrollingEnabled(false);
    }

    private void showMainMenu(android.view.View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        Menu menu = popupMenu.getMenu();
        menu.add("Đăng xuất");
        popupMenu.setOnMenuItemClickListener(item -> {
            if ("Đăng xuất".contentEquals(item.getTitle())) {
                getSharedPreferences("LTDD_PREFS", MODE_PRIVATE).edit().clear().apply();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupCategories();
    }
}
