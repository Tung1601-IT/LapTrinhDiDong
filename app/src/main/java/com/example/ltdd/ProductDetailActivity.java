package com.example.ltdd;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltdd.adapter.ProductUserAdapter;
import com.example.ltdd.database.DatabaseSanPham;
import com.example.ltdd.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private Product product;
    private DatabaseSanPham db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = new DatabaseSanPham(this);
        product = (Product) getIntent().getSerializableExtra("product");

        if (product == null) {
            finish();
            return;
        }

        initViews();
        setupRelatedProducts();
    }

    private void initViews() {
        ImageView btnBack = findViewById(R.id.btnBackDetail);
        ImageView imgDetail = findViewById(R.id.imgDetailMain);
        TextView txtTitle = findViewById(R.id.txtDetailTitle);
        TextView txtPrice = findViewById(R.id.txtDetailPrice);
        TextView txtOldPrice = findViewById(R.id.txtDetailOldPrice);
        LinearLayout specsContainer = findViewById(R.id.specsContainer);

        btnBack.setOnClickListener(v -> finish());

        // Điền thông tin cơ bản
        txtTitle.setText(product.getModel());
        
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String priceStr = formatter.format(product.getPrice()) + "đ";

        txtPrice.setText(priceStr);
        
        // Ẩn giá cũ
        if (txtOldPrice != null) {
            txtOldPrice.setVisibility(View.GONE);
        }

        if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
            imgDetail.setImageURI(Uri.parse(product.getImagePath()));
        }

        // Tự động điền Bảng thông số kỹ thuật
        addSpecRow(specsContainer, "Kích thước màn hình", product.getScreenSize());
        addSpecRow(specsContainer, "Công nghệ màn hình", product.getScreenQuality());
        addSpecRow(specsContainer, "Chip", product.getProcessor());
        addSpecRow(specsContainer, "Dung lượng RAM", product.getRam());
        addSpecRow(specsContainer, "Bộ nhớ", product.getRom());
        addSpecRow(specsContainer, "Pin", product.getPin() + " mAh");

        // Gửi đánh giá
        findViewById(R.id.btnSubmitReview).setOnClickListener(v -> {
            Toast.makeText(this, "Cảm ơn bạn đã gửi đánh giá!", Toast.LENGTH_SHORT).show();
            findViewById(R.id.edtComment).setEnabled(false);
            v.setEnabled(false);
        });

        // Các nút bấm dưới cùng
        findViewById(R.id.btnAddToCartDetail).setOnClickListener(v -> 
            Toast.makeText(this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show()
        );

        findViewById(R.id.btnBuyNowDetail).setOnClickListener(v -> 
            Toast.makeText(this, "Mua ngay: " + product.getModel(), Toast.LENGTH_SHORT).show()
        );
    }

    private void addSpecRow(LinearLayout container, String title, String value) {
        if (container == null) return;
        
        View row = LayoutInflater.from(this).inflate(R.layout.row_spec, container, false);
        TextView tvTitle = row.findViewById(R.id.tvSpecTitle);
        TextView tvValue = row.findViewById(R.id.tvSpecValue);
        
        tvTitle.setText(title);
        tvValue.setText(value != null ? value : "Đang cập nhật");
        container.addView(row);
    }

    private void setupRelatedProducts() {
        RecyclerView rvRelated = findViewById(R.id.rvRelated);
        List<Product> all = db.getAllProducts();
        List<Product> related = new ArrayList<>();
        
        // Lấy các sản phẩm cùng dòng (iPhone, iPad...) nhưng khác ID hiện tại
        for (Product p : all) {
            if (p.getId() != product.getId() && p.getProductLine().equalsIgnoreCase(product.getProductLine())) {
                related.add(p);
            }
            if (related.size() >= 4) break; // Lấy tối đa 4 cái
        }

        ProductUserAdapter adapter = new ProductUserAdapter(related, new ProductUserAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product p) {
                Intent intent = new Intent(ProductDetailActivity.this, ProductDetailActivity.class);
                intent.putExtra("product", p);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAddToCartClick(Product p) {
                Toast.makeText(ProductDetailActivity.this, "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBuyNowClick(Product p) {
                Intent intent = new Intent(ProductDetailActivity.this, ProductDetailActivity.class);
                intent.putExtra("product", p);
                startActivity(intent);
                finish();
            }
        });

        rvRelated.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRelated.setAdapter(adapter);
    }
}
