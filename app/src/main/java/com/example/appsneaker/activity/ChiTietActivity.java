package com.example.appsneaker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appsneaker.R;
import com.example.appsneaker.Utils.Utils;
import com.example.appsneaker.model.GioHang;
import com.example.appsneaker.model.SanPhamMoi;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView txtNameSp, txtPriceSp, txtMota;
    Button btnAdd;
    ImageView imgPicture;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themVaoGioHang();
            }
        });
    }

    private void themVaoGioHang() {
        if (Utils.manggiohang.size() > 0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i < Utils.manggiohang.size(); i++){
                if (Utils.manggiohang.get(i).getIdsp() == sanPhamMoi.getId()){
                    Utils.manggiohang.get(i).setSoluong(soluong + Utils.manggiohang.get(i).getSoluong());
                    long gia = Long.parseLong(sanPhamMoi.getGiasp()) * Utils.manggiohang.get(i).getSoluong();
                    Utils.manggiohang.get(i).setGiasp(gia);
                    flag = true;
                }
            }
            if (flag == false) {
                long gia = Long.parseLong(sanPhamMoi.getGiasp()) * soluong;
                GioHang gioHang = new GioHang();
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdsp(sanPhamMoi.getId());
                gioHang.setTensp(sanPhamMoi.getTensp());
                gioHang.setHinhsp(sanPhamMoi.getHinhanhsp());
                Utils.manggiohang.add(gioHang);
            }
        } else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiasp()) *soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setTensp(sanPhamMoi.getTensp());
            gioHang.setHinhsp(sanPhamMoi.getHinhanhsp());
            Utils.manggiohang.add(gioHang);
        }
        int totalItem = 0;
        for (int i =0; i < Utils.manggiohang.size(); i++){
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }
        notificationBadge.setText(String.valueOf(totalItem));
    }

    private void initData() {
        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        txtNameSp.setText(sanPhamMoi.getTensp());
        txtPriceSp.setText(sanPhamMoi.getGiasp());
        txtMota.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanhsp()).into(imgPicture);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtPriceSp.setText("Giá: " + decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp())) + "đ");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9};
        ArrayAdapter<Integer> integerArrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, so);
        spinner.setAdapter(integerArrayAdapter);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.manggiohang != null){
            int totalItem = 0;
            for (int i = 0; i < Utils.manggiohang.size(); i++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
        }
        notificationBadge.setText(String.valueOf(Utils.manggiohang.size()));
    }

    private void initView() {
        txtNameSp = findViewById(R.id.tv_name_chitiet);
        txtPriceSp = findViewById(R.id.tv_price_chitiet);
        txtMota = findViewById(R.id.tv_mota_chitiet);
        btnAdd = findViewById(R.id.btn_chitiet);
        imgPicture = findViewById(R.id.img_chi_tiet);
        spinner = findViewById(R.id.spn_chitiet);
        toolbar = findViewById(R.id.toolbar_chi_tiet);
        FrameLayout frameLayoutGioHang = findViewById(R.id.gio_hang_click);
        frameLayoutGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGH = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intentGH);
            }
        });
        notificationBadge = findViewById(R.id.menu_badge);
        if (Utils.manggiohang != null){
            int totalItem = 0;
            for (int i = 0; i < Utils.manggiohang.size(); i++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
        }
        notificationBadge.setText(String.valueOf(Utils.manggiohang.size()));
    }
}