package com.example.appsneaker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appsneaker.R;
import com.example.appsneaker.Utils.Utils;
import com.example.appsneaker.adapter.GioHangAdapter;
import com.example.appsneaker.model.GioHang;
import com.example.appsneaker.model.eventbus.TinhTongEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong, tv_gia_thanh_toan;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button button;
    GioHangAdapter gioHangAdapter;
    long totalmoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        initView();
        initControl();
        totalMoney();
    }

    private void totalMoney() {
        totalmoney = 0;
        for (int i =0; i < Utils.mangMuaHang.size(); i++) {
            totalmoney = totalmoney + (Utils.mangMuaHang.get(i).getGiasp() * Utils.mangMuaHang.get(i).getSoluong());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tv_gia_thanh_toan.setText(decimalFormat.format(totalmoney));
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Utils.manggiohang.size() == 0){
            giohangtrong.setVisibility(View.VISIBLE);
        } else {
            gioHangAdapter = new GioHangAdapter(getApplicationContext(), Utils.manggiohang);
            recyclerView.setAdapter(gioHangAdapter);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThanhToansActivity.class);
                intent.putExtra("tongtien", totalmoney);
                Utils.manggiohang.clear();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent tinhTongEvent){
        if (tinhTongEvent != null){
            totalMoney();
        }
    }

    private void initView() {
        giohangtrong = findViewById(R.id.tv_giohang);
        toolbar = findViewById(R.id.toolbar_giohang);
        recyclerView = findViewById(R.id.rcv_giohang);
        button = findViewById(R.id.btn_giohang);
        tv_gia_thanh_toan = findViewById(R.id.tv_gia_thanh_toan);
    }
}