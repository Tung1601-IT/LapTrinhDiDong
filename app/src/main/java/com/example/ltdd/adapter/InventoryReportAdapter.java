package com.example.ltdd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ltdd.R;
import com.example.ltdd.model.InventoryReport;
import java.util.List;

public class InventoryReportAdapter extends RecyclerView.Adapter<InventoryReportAdapter.ReportViewHolder> {

    private List<InventoryReport> reportList;

    public InventoryReportAdapter(List<InventoryReport> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Đã sửa: Sử dụng đúng layout R.layout.item_report_inventory
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_inventory, parent, false);
        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        InventoryReport item = reportList.get(position);
        holder.tvProductName.setText(item.getProductName());
        holder.tvOpeningStock.setText(String.valueOf(item.getOpeningStock()));
        holder.tvImportQty.setText(String.valueOf(item.getImportQty()));
        holder.tvExportQty.setText(String.valueOf(item.getExportQty()));
        holder.tvClosingStock.setText(String.valueOf(item.getClosingStock()));
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public void updateData(List<InventoryReport> newList) {
        this.reportList = newList;
        notifyDataSetChanged();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvOpeningStock, tvImportQty, tvExportQty, tvClosingStock;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvReportProductName);
            tvOpeningStock = itemView.findViewById(R.id.tvOpeningStock);
            tvImportQty = itemView.findViewById(R.id.tvImportQty);
            tvExportQty = itemView.findViewById(R.id.tvExportQty);
            tvClosingStock = itemView.findViewById(R.id.tvClosingStock);
        }
    }
}
