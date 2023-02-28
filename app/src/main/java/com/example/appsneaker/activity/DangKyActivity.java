package com.example.appsneaker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appsneaker.R;
import com.example.appsneaker.Utils.Utils;
import com.example.appsneaker.retrofit.ApiSanPham;
import com.example.appsneaker.retrofit.RetrofitClient;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {
    EditText edtUserName, edtEmail, edtPass, edtResetPass, edtPhone;
    AppCompatButton button;
    ApiSanPham apiSanPham;
    CompositeDisposable compositeDisposable= new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControl();
    }

    private void initControl() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangKi();
            }
        });
    }

    private void dangKi() {
        String str_email = edtEmail.getText().toString().trim();
        String str_pass = edtPass.getText().toString().trim();
        String str_repass = edtResetPass.getText().toString().trim();
        String str_user = edtUserName.getText().toString().trim();
        String str_phone = edtPhone.getText().toString().trim();

        if (TextUtils.isEmpty(str_user)){
            Toast.makeText(getApplicationContext(), "User name error", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(), "Email error", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_pass)){
            Toast.makeText(getApplicationContext(), "Pass error", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_repass)){
            Toast.makeText(getApplicationContext(), "Repass error", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(str_phone)){
            Toast.makeText(getApplicationContext(), "Phone error", Toast.LENGTH_SHORT).show();
        } else {
            if (str_pass.equals(str_repass)){
                //post data
                compositeDisposable.add(apiSanPham.dangKi(str_email, str_pass, str_user, str_phone)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    if (userModel.isSuccess()){
                                        Utils.user_current.setEmail(str_email);
                                        Utils.user_current.setPass(str_pass);
                                        Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                        ));
            } else {
                Toast.makeText(getApplicationContext(), "Pass not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void initView() {
        edtEmail = findViewById(R.id.tv_edt_email);
        edtUserName = findViewById(R.id.tv_edt_username);
        edtPass = findViewById(R.id.tv_edt_pass);
        edtResetPass = findViewById(R.id.tv_edt_reset_pass);
        edtPhone = findViewById(R.id.tv_edt_sdt);
        button = findViewById(R.id.btn_register);

        apiSanPham = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiSanPham.class);
    }
}