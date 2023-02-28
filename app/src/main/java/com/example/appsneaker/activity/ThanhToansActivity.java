package com.example.appsneaker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appsneaker.R;
import com.example.appsneaker.Utils.Utils;
import com.example.appsneaker.retrofit.ApiSanPham;
import com.example.appsneaker.retrofit.RetrofitClient;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToansActivity extends AppCompatActivity {
    
    Toolbar toolbar;
    TextView txtTongTien, txtSdt, txtEmail;
    EditText edtDiaChi;
    AppCompatButton btnDatHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiSanPham apiSanPham;
    long tongtien;
    int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toans);
        
        initView();
        initControl();
        countItem();
    }

    private void countItem() {
        totalItem = 0;
        for (int i = 0; i < Utils.mangMuaHang.size(); i++){
            totalItem = totalItem + Utils.mangMuaHang.get(i).getSoluong();
        }
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
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = getIntent().getLongExtra("tongtien", 0);
        txtTongTien.setText(decimalFormat.format(tongtien));
        txtEmail.setText(Utils.user_current.getEmail());
        txtSdt.setText(Utils.user_current.getSdt());

        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = edtDiaChi.getText().toString().trim();
                if (TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(), "Ban chua nhap dia chi", Toast.LENGTH_SHORT).show();
                } else {
                    //post data
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getSdt();
                    int id = Utils.user_current.getId();
                    Log.d("test", new Gson().toJson(Utils.mangMuaHang));
                    compositeDisposable.add(apiSanPham.createOder(str_email, str_sdt,String.valueOf(tongtien), id, str_diachi, totalItem, new Gson().toJson(Utils.manggiohang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                        Utils.mangMuaHang.clear();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar_giohang);
        txtTongTien = findViewById(R.id.tv_tong_tien);
        txtSdt = findViewById(R.id.tv_sdt);
        txtEmail = findViewById(R.id.tv_email);
        edtDiaChi = findViewById(R.id.edt_diachi);
        btnDatHang = findViewById(R.id.btn_dat_hang);

        apiSanPham = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiSanPham.class);
    }
}