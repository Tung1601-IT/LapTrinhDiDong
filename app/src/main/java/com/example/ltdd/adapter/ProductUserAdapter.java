package com.example.ltdd.adapter;

import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ltdd.R;
import com.example.ltdd.model.Product;
import java.text.DecimalFormat;
import java.util.List;

public class ProductUserAdapter extends RecyclerView.Adapter<ProductUserAdapter.ViewHolder> {

    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onAddToCartClick(Product product);
        void onBuyNowClick(Product product);
    }

    public ProductUserAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        
        // Chỉ hiển thị Model
        holder.txtName.setText(product.getModel());
        
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        
        // Giá hiện tại (màu xanh dương)
        holder.txtPrice.setText(formatter.format(product.getPrice()) + " đ");
        
        // Đã ẩn Giá cũ và Badge giảm giá trong XML
        holder.txtOldPrice.setVisibility(View.GONE);
        holder.txtDiscountBadge.setVisibility(View.GONE);

        // Hiển thị ảnh
        if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
            try {
                holder.imgProduct.setImageURI(Uri.parse(product.getImagePath()));
            } catch (Exception e) {
                holder.imgProduct.setImageResource(R.drawable.ic_apple_logo);
            }
        } else {
            holder.imgProduct.setImageResource(R.drawable.ic_apple_logo);
        }

        holder.itemView.setOnClickListener(v -> listener.onProductClick(product));
        holder.btnAddToCart.setOnClickListener(v -> listener.onAddToCartClick(product));
        holder.btnBuyNow.setOnClickListener(v -> listener.onBuyNowClick(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtPrice, txtOldPrice, txtDiscountBadge;
        View btnAddToCart, btnBuyNow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProductUser);
            txtName = itemView.findViewById(R.id.txtProductNameUser);
            txtPrice = itemView.findViewById(R.id.txtProductPriceUser);
            txtOldPrice = itemView.findViewById(R.id.txtOldPriceUser);
            txtDiscountBadge = itemView.findViewById(R.id.txtDiscountBadge);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnBuyNow = itemView.findViewById(R.id.btnBuyNow);
        }
    }
}