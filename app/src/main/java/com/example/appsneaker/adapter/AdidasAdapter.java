package com.example.appsneaker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class AdidasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPhamMoi> list;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public AdidasAdapter(Context context, List<SanPhamMoi> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null? VIEW_TYPE_LOADING:VIEW_TYPE_DATA;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_adidas, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return  new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder){
            MyViewHolder viewHolder = (MyViewHolder) holder;
            SanPhamMoi sanPhamMoi = list.get(position);
            viewHolder.txtNameAdidas.setText(sanPhamMoi.getTensp());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            viewHolder.txtPriceAdidas.setText("Giá: " + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp())) + "đ");
            viewHolder.txtNoteAdidas.setText(sanPhamMoi.getMota());
            Glide.with(context).load(sanPhamMoi.getHinhanhsp()).into(viewHolder.imgAdidas);
            viewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if (!isLongClick){
                        //click
                        Intent intent = new Intent(context, ChiTietActivity.class);
                        intent.putExtra("chitiet", sanPhamMoi);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        } else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.ProgressBar);
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtNameAdidas, txtPriceAdidas, txtNoteAdidas;
        ImageView imgAdidas;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameAdidas = itemView.findViewById(R.id.tv_name_adidas);
            txtPriceAdidas = itemView.findViewById(R.id.tv_price_adidas);
            txtNoteAdidas = itemView.findViewById(R.id.tv_note_adidas);
            imgAdidas = itemView.findViewById(R.id.item_img_adidas);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

    }


}
