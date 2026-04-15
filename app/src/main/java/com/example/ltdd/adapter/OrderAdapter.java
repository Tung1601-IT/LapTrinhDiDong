package com.example.ltdd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ltdd.R;
import com.example.ltdd.model.Order;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private final OnOrderActionListener listener;

    public interface OnOrderActionListener {
        void onEdit(Order order);
        void onDelete(Order order);
        void onItemClick(Order order);
    }

    public OrderAdapter(List<Order> orders, OnOrderActionListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvOrderIndex.setText(String.valueOf(position + 1));
        holder.tvOrderId.setText(order.getOrderId());
        holder.tvCustomerName.setText(order.getCustomerName());
        holder.tvOrderDate.setText(order.getDate());
        holder.tvTotalAmount.setText(order.getTotalAmount());

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(order));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(order));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(order));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void updateData(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderIndex, tvOrderId, tvCustomerName, tvOrderDate, tvTotalAmount;
        Button btnEdit, btnDelete;

        public OrderViewHolder(@NonNull View view) {
            super(view);
            tvOrderIndex = view.findViewById(R.id.tvOrderIndex);
            tvOrderId = view.findViewById(R.id.tvOrderId);
            tvCustomerName = view.findViewById(R.id.tvCustomerName);
            tvOrderDate = view.findViewById(R.id.tvOrderDate);
            tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
            btnEdit = view.findViewById(R.id.btnEdit);
            btnDelete = view.findViewById(R.id.btnDelete);
        }
    }
}
