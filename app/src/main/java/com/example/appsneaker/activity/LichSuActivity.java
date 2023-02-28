package com.example.appsneaker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.appsneaker.R;
import com.example.appsneaker.Utils.Utils;
import com.example.appsneaker.adapter.LichSuDonHangAdapter;
import com.example.appsneaker.model.DonHang;
import com.example.appsneaker.retrofit.ApiSanPham;
import com.example.appsneaker.retrofit.RetrofitClient;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LichSuActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiSanPham apiSanPham;
    RecyclerView reDonHang;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su);

        initView();
        initToolBar();
        getOder();
    }

    private void getOder() {
        compositeDisposable.add(apiSanPham.historyShopping(Utils.user_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            LichSuDonHangAdapter adapter = new LichSuDonHangAdapter((List<DonHang>) getApplicationContext(), (Context) donHangModel.getResult());
                            reDonHang.setAdapter(adapter);
                        },
                        throwable -> {

                        }
                ));
    }

    private void initToolBar() {
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
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void initView() {
        apiSanPham = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiSanPham.class);
        reDonHang = findViewById(R.id.rcv_lich_su);
        toolbar = findViewById(R.id.toolbar_lich_su);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        reDonHang.setLayoutManager(layoutManager);
    }
}