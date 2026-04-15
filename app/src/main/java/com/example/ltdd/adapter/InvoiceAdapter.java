package com.example.ltdd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ltdd.R;
import com.example.ltdd.model.Invoice;
import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> {

    private List<Invoice> invoices;

    public InvoiceAdapter(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice, parent, false);
        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        Invoice invoice = invoices.get(position);
        holder.tvInvoiceId.setText("#" + invoice.getId());
        holder.tvCustomerName.setText(invoice.getCustomerName());
        holder.tvCustomerPhone.setText(invoice.getCustomerPhone());
        holder.tvProductName.setText(invoice.getProductName());
        holder.tvProductIMEI.setText("IMEI: " + invoice.getProductIMEI());
        holder.tvAmount.setText(invoice.getAmount());
        holder.tvDate.setText(invoice.getDate());
    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }

    public void updateData(List<Invoice> newInvoices) {
        this.invoices = newInvoices;
        notifyDataSetChanged();
    }

    public static class InvoiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvInvoiceId, tvCustomerName, tvCustomerPhone, tvProductName, tvProductIMEI, tvAmount, tvDate;

        public InvoiceViewHolder(@NonNull View view) {
            super(view);
            tvInvoiceId = view.findViewById(R.id.tvInvoiceId);
            tvCustomerName = view.findViewById(R.id.tvCustomerName);
            tvCustomerPhone = view.findViewById(R.id.tvCustomerPhone);
            tvProductName = view.findViewById(R.id.tvProductName);
            tvProductIMEI = view.findViewById(R.id.tvProductIMEI);
            tvAmount = view.findViewById(R.id.tvAmount);
            tvDate = view.findViewById(R.id.tvDate);
        }
    }
}
