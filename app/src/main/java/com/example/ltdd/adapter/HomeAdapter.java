package com.example.ltdd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ltdd.R;
import com.example.ltdd.model.HomeItem;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private List<HomeItem> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(HomeItem item);
    }

    public HomeAdapter(List<HomeItem> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        HomeItem item = list.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.imgIcon.setImageResource(item.getIconRes());
        holder.cardIcon.setCardBackgroundColor(item.getColorRes());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvTitle;
        CardView cardIcon;

        HomeViewHolder(View view) {
            super(view);
            imgIcon = view.findViewById(R.id.imgIcon);
            tvTitle = view.findViewById(R.id.tvTitle);
            cardIcon = view.findViewById(R.id.cardIcon);
        }
    }
}