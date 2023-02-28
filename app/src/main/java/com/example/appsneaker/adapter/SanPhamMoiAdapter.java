package com.example.appsneaker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appsneaker.Interface.ItemClickListener;
import com.example.appsneaker.R;
import com.example.appsneaker.activity.ChiTietActivity;
import com.example.appsneaker.model.SanPhamMoi;

import java.text.DecimalFormat;
import java.util.List;

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.MyViewHolder> {
    Context context;
    List<SanPhamMoi> list;

    public SanPhamMoiAdapter(Context context, List<SanPhamMoi> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SanPhamMoiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham_moi, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamMoiAdapter.MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoiModel = list.get(position);
        holder.txtName.setText(sanPhamMoiModel.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtPrice.setText("Giá: " + decimalFormat.format(Double.parseDouble(sanPhamMoiModel.getGiasp())) + "đ");
        Glide.with(context).load(sanPhamMoiModel.getHinhanhsp()).into(holder.imgPicture);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if (!isLongClick){
                    //click
                    Intent intent = new Intent(context, ChiTietActivity.class);
                    intent.putExtra("chitiet", sanPhamMoiModel);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtPrice, txtName;
        ImageView imgPicture;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.tv_name_sp);
            txtPrice = itemView.findViewById(R.id.tv_price_sp);
            imgPicture = itemView.findViewById(R.id.img_sp_moi);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }
}
