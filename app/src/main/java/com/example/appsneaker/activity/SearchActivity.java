package com.example.appsneaker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.appsneaker.R;
import com.example.appsneaker.Utils.Utils;
import com.example.appsneaker.adapter.AdidasAdapter;
import com.example.appsneaker.model.SanPhamMoi;
import com.example.appsneaker.retrofit.ApiSanPham;
import com.example.appsneaker.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.internal.Util;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText editText;
    RecyclerView recyclerView;
    List<SanPhamMoi> sanPhamMoiList;
    ApiSanPham apiSanPham;
    AdidasAdapter searchAdapter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        
        initView();
        ActionToolBar();
        
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

    private void getDataSearch(String s) {
        sanPhamMoiList.clear();
        String str_search = editText.getText().toString().trim();
        compositeDisposable.add(apiSanPham.search(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()){
                                sanPhamMoiList = sanPhamMoiModel.getResult();
                                searchAdapter = new AdidasAdapter(getApplicationContext(), sanPhamMoiList);
                                recyclerView.setAdapter(searchAdapter);
                            }
                        },
                        throwable -> {

                        }
                ));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void initView() {
        editText = findViewById(R.id.edt_search);
        sanPhamMoiList = new ArrayList<>();
        toolbar = findViewById(R.id.toolBar_search);
        recyclerView = findViewById(R.id.rcv_search);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        apiSanPham = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiSanPham.class);
        
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0){
                    sanPhamMoiList.clear();
                    searchAdapter = new AdidasAdapter(getApplicationContext(), sanPhamMoiList);
                    recyclerView.setAdapter(searchAdapter);
                } else {
                    getDataSearch(charSequence.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}