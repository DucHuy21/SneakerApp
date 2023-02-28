package com.example.appsneaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appsneaker.R;
import com.example.appsneaker.model.LoaiSp;

import java.util.List;

public class LoaiSpAdapter extends BaseAdapter {
    List<LoaiSp> loaiSpList;
    Context context;

    public LoaiSpAdapter(List<LoaiSp> loaiSpList, Context context) {
        this.loaiSpList = loaiSpList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return loaiSpList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_sanpham, null);
            viewHolder.txtNameSp = view.findViewById(R.id.tv_item_sp);
            viewHolder.imgPictureSp = view.findViewById(R.id.img_hinh_sp);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.txtNameSp.setText(loaiSpList.get(i).getTensanpham());
        Glide.with(context).load(loaiSpList.get(i).getHinhanhsanpham()).into(viewHolder.imgPictureSp);

        return view;
    }

    public class ViewHolder {
        TextView txtNameSp;
        ImageView imgPictureSp;
    }
}
