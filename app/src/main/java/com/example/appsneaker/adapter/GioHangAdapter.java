package com.example.appsneaker.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appsneaker.Interface.ImgClickListener;
import com.example.appsneaker.R;
import com.example.appsneaker.Utils.Utils;
import com.example.appsneaker.model.GioHang;
import com.example.appsneaker.model.eventbus.TinhTongEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

import okhttp3.internal.Util;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_giohang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangAdapter.MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.tv_tensp_giohang.setText(gioHang.getTensp());
        holder.soluong_sanpham_giohang.setText(gioHang.getSoluong() + " ");
        Glide.with(context).load(gioHang.getHinhsp()).into(holder.img_item_giohang);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tv_giohang_price.setText("Giá: " + decimalFormat.format((gioHang.getGiasp())) + "đ");
        long gia = gioHang.getSoluong() * gioHang.getGiasp();
        holder.setImgClickListener(new ImgClickListener() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if (giatri == 1){
                    if (gioHangList.get(pos).getSoluong() > 1){
                        int soluongmoi = gioHangList.get(pos).getSoluong() - 1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                        holder.soluong_sanpham_giohang.setText(gioHangList.get(pos).getSoluong() + " ");
                        long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    } else if(gioHangList.get(pos).getSoluong() == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xoá sản phẩm khỏi giỏ hàng không?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                        Utils.manggiohang.remove(pos);
                    }
                } else if (giatri == 2) {
                    if (gioHangList.get(pos).getSoluong() < 9){
                        int soluongmoi = gioHangList.get(pos).getSoluong() + 1;
                        gioHangList.get(pos).setSoluong(soluongmoi);
                    }
                    holder.soluong_sanpham_giohang.setText(gioHangList.get(pos).getSoluong() + " ");
                    long gia = gioHangList.get(pos).getSoluong() * gioHangList.get(pos).getGiasp();
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Utils.mangMuaHang.add(gioHang);
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                } else {
                    for (int i = 0; i < Utils.mangMuaHang.size(); i++){
                        if (Utils.mangMuaHang.get(i).getIdsp() == gioHang.getIdsp()){
                            Utils.mangMuaHang.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongEvent());
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_item_giohang, imgCong, imgTru;
        TextView tv_tensp_giohang, tv_giohang_price, soluong_sanpham_giohang;
        ImgClickListener imgClickListener;
        CheckBox  checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_item_giohang = itemView.findViewById(R.id.img_item_giohang);
            tv_tensp_giohang = itemView.findViewById(R.id.tv_tensp_giohang);
            tv_giohang_price = itemView.findViewById(R.id.tv_giohang_price);
            soluong_sanpham_giohang = itemView.findViewById(R.id.soluong_sanpham_giohang);
            imgTru = itemView.findViewById(R.id.img_remove_giohang);
            imgCong = itemView.findViewById(R.id.img_add_giohang);
            checkBox = itemView.findViewById(R.id.ckb_gio_hang);

            //event img click
            imgTru.setOnClickListener(this);
            imgCong.setOnClickListener(this);
        }

        public void setImgClickListener(ImgClickListener imgClickListener) {
            this.imgClickListener = imgClickListener;
        }

        @Override
        public void onClick(View view) {
            if (view == imgTru){
                imgClickListener.onImageClick(view, getAdapterPosition(), 1);
            } else if (view == imgCong) {
                imgClickListener.onImageClick(view, getAdapterPosition(), 2);
            }
        }
    }
}
