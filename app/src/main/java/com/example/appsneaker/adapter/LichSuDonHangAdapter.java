package com.example.appsneaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appsneaker.R;
import com.example.appsneaker.model.DonHang;

import java.util.List;

public class LichSuDonHangAdapter extends RecyclerView.Adapter<LichSuDonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    List<DonHang> donHangList;
    Context context;

    public LichSuDonHangAdapter(List<DonHang> donHangList, Context context) {
        this.donHangList = donHangList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_lich_su, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichSuDonHangAdapter.MyViewHolder holder, int position) {
        DonHang donHang = donHangList.get(position);
        holder.tv_donHang.setText("Đơn hàng: " + donHang.getId());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
          holder.recyclerView.getContext(),
          LinearLayoutManager.VERTICAL,
                false
        );
        linearLayoutManager.setInitialPrefetchItemCount(donHang.getItem().size());

        //adapter chi tiet
        LichSuDonHangChiTietAdapter lichSuDonHangChiTietAdapter = new LichSuDonHangChiTietAdapter(donHang.getItem(), context);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        holder.recyclerView.setAdapter(lichSuDonHangChiTietAdapter);
        holder.recyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return donHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_donHang;
        RecyclerView recyclerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_donHang = itemView.findViewById(R.id.tv_lich_su_don_hang);
            recyclerView = itemView.findViewById(R.id.rcv_chi_tiet_don_hang);
        }
    }
}
