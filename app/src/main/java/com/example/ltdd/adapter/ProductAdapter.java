package com.example.ltdd.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltdd.R;
import com.example.ltdd.model.Product;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnProductListener onProductListener;

    public interface OnProductListener {
        void onEditClick(Product product);
        void onDeleteClick(Product product);
    }

    public ProductAdapter(List<Product> productList, OnProductListener onProductListener) {
        this.productList = productList;
        this.onProductListener = onProductListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvStt.setText(String.valueOf(position + 1));
        
        if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
            try {
                holder.ivProduct.setImageURI(Uri.parse(product.getImagePath()));
            } catch (SecurityException e) {
                holder.ivProduct.setImageResource(android.R.drawable.ic_menu_report_image);
            }
        } else {
            holder.ivProduct.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.tvProductLine.setText(product.getProductLine());
        holder.tvModel.setText(product.getModel());
        holder.tvImei.setText(product.getImei());
        holder.tvRam.setText(product.getRam());
        holder.tvRom.setText(product.getRom());
        holder.tvScreenSize.setText(product.getScreenSize() + "\"");
        holder.tvScreenQuality.setText(product.getScreenQuality());
        holder.tvProcessor.setText(product.getProcessor());
        holder.tvPin.setText(product.getPin());
        holder.tvColor.setText(product.getColor());
        holder.tvQuantity.setText(String.valueOf(product.getQuantity()));
        holder.tvPrice.setText(String.format("%,.0f đ", product.getPrice()));

        holder.btnEdit.setOnClickListener(v -> onProductListener.onEditClick(product));
        holder.btnDelete.setOnClickListener(v -> onProductListener.onDeleteClick(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvStt, tvProductLine, tvModel, tvImei, tvRam, tvRom, tvScreenSize, tvScreenQuality, tvProcessor, tvPin, tvColor, tvQuantity, tvPrice;
        ImageView ivProduct;
        Button btnEdit, btnDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStt = itemView.findViewById(R.id.tvStt);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvProductLine = itemView.findViewById(R.id.tvProductLine);
            tvModel = itemView.findViewById(R.id.tvModel);
            tvImei = itemView.findViewById(R.id.tvImei);
            tvRam = itemView.findViewById(R.id.tvRam);
            tvRom = itemView.findViewById(R.id.tvRom);
            tvScreenSize = itemView.findViewById(R.id.tvScreenSize);
            tvScreenQuality = itemView.findViewById(R.id.tvScreenQuality);
            tvProcessor = itemView.findViewById(R.id.tvProcessor);
            tvPin = itemView.findViewById(R.id.tvPin);
            tvColor = itemView.findViewById(R.id.tvColor);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}