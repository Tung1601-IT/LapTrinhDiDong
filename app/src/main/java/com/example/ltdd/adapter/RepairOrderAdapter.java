package com.example.ltdd.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltdd.R;
import com.example.ltdd.model.RepairOrder;

import java.util.List;

public class RepairOrderAdapter extends RecyclerView.Adapter<RepairOrderAdapter.RepairViewHolder> {

    public interface OnRepairSelectListener {
        void onSelect(RepairOrder order);
    }

    private final List<RepairOrder> list;
    private final boolean isHistoryTab;
    private final OnRepairSelectListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public RepairOrderAdapter(List<RepairOrder> list, boolean isHistoryTab, OnRepairSelectListener listener) {
        this.list = list;
        this.isHistoryTab = isHistoryTab;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RepairViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_repair_order, parent, false);
        return new RepairViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepairViewHolder holder, int position) {
        RepairOrder order = list.get(position);
        Drawable normalBackground = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_repair_item);
        Drawable selectedBackground = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_repair_item_selected);

        holder.tvCustomerName.setText(order.getCustomerName());
        holder.tvDevice.setText(order.getDevice());
        holder.tvIssue.setText("Lỗi: " + order.getIssueDescription());

        if (isHistoryTab) {
            holder.tvStatus.setText("Ngày nhận: " + order.getReceivedDate()
                    + "\nNgày hoàn tất: " + order.getCompletedDate());
            holder.tvHistoryBadge.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setText("Trạng thái: " + order.getStatus());
            holder.tvHistoryBadge.setVisibility(View.GONE);
        }

        holder.layoutItemRoot.setBackground(position == selectedPosition ? selectedBackground : normalBackground);

        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            int adapterPosition = holder.getBindingAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return;
            }

            selectedPosition = adapterPosition;
            if (oldPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(oldPosition);
            }
            notifyItemChanged(selectedPosition);
            listener.onSelect(order);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class RepairViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutItemRoot;
        TextView tvCustomerName, tvDevice, tvIssue, tvStatus, tvHistoryBadge;

        RepairViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutItemRoot = itemView.findViewById(R.id.layoutItemRoot);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvDevice = itemView.findViewById(R.id.tvDevice);
            tvIssue = itemView.findViewById(R.id.tvIssue);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvHistoryBadge = itemView.findViewById(R.id.tvHistoryBadge);
        }
    }
}
