package com.example.ltdd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.ltdd.MainKhachHang;
import com.example.ltdd.R;
import com.example.ltdd.model.Khachhang;
import java.util.List;

public class KhachhangAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Khachhang> list;

    public KhachhangAdapter(Context context, int layout, List<Khachhang> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
        }

        TextView txtten = view.findViewById(R.id.tenkh);
        TextView txtsdt = view.findViewById(R.id.sdtkh);
        ImageButton sua = view.findViewById(R.id.sua);
        
        Khachhang kh = list.get(i);
        txtten.setText(kh.getCustomer_name());
        txtsdt.setText(kh.getCustomer_contact_no());
        
        sua.setOnClickListener(v -> {
            if (context instanceof MainKhachHang) {
                ((MainKhachHang) context).hienDialogSua(kh);
            }
        });
        return view;
    }
}