package com.example.appsneaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appsneaker.R;
import com.example.appsneaker.model.Item;

import java.util.List;

public class LichSuDonHangChiTietAdapter extends RecyclerView.Adapter<LichSuDonHangChiTietAdapter.MyViewHolder> {
    List<Item> itemList;
    Context context;

    public LichSuDonHangChiTietAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_chi_tiet_don_hang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichSuDonHangChiTietAdapter.MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.tv_ten_sp_chi_tiet.setText(item.getTensp() + " ");
        holder.tv_so_luong_sp_chi_tiet.setText("Số lượng: " + item.getSoluong() + " ");
        Glide.with(context).load(item.getHinhanh()).into(holder.imgChiTiet);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgChiTiet;
        TextView tv_ten_sp_chi_tiet, tv_so_luong_sp_chi_tiet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgChiTiet = itemView.findViewById(R.id.img_item_chi_tiet);
            tv_ten_sp_chi_tiet = itemView.findViewById(R.id.tv_item_ten_sp_chi_tiet);
            tv_so_luong_sp_chi_tiet = itemView.findViewById(R.id.tv_item_so_luong_sp_chi_tiet);
        }
    }
}
