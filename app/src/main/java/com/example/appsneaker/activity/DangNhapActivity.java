package com.example.appsneaker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appsneaker.R;
import com.example.appsneaker.Utils.Utils;
import com.example.appsneaker.retrofit.ApiSanPham;
import com.example.appsneaker.retrofit.RetrofitClient;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtRegister;
    EditText edtEmail, edtPass;
    AppCompatButton buttonLogin;
    ApiSanPham apiSanPham;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        initView();
        initControl();
    }

    private void initControl() {
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DangKyActivity.class);
                startActivity(intent);
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_email = edtEmail.getText().toString().trim();
                String str_pass = edtPass.getText().toString().trim();
                if (TextUtils.isEmpty(str_email)) {
                    Toast.makeText(getApplicationContext(), "Email error", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(str_pass)) {
                    Toast.makeText(getApplicationContext(), "Pass error", Toast.LENGTH_SHORT).show();
                } else {
                    //save
                    Paper.book().write("email", str_email);
                    Paper.book().write("pass", str_pass);
                    dangNhap(str_email, str_pass);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.user_current.getEmail() != null && Utils.user_current.getPass() != null) {
            edtEmail.setText(Utils.user_current.getEmail());
            edtPass.setText(Utils.user_current.getPass());
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void initView() {
        Paper.init(this);
        txtRegister = findViewById(R.id.tv_register_in_login);
        edtEmail = findViewById(R.id.tv_edt_email);
        edtPass = findViewById(R.id.tv_edt_pass);
        buttonLogin = findViewById(R.id.btn_login);
        apiSanPham = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiSanPham.class);

        //read data
        if (Paper.book().read("email") != null && Paper.book().read("pass") != null) {
            edtEmail.setText(Paper.book().read("email"));
            edtPass.setText(Paper.book().read("pass"));
            if (Paper.book().read("isLogin") != null) {
                boolean flag = Paper.book().read("isLogin");
                if (flag) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //dangNhap(Paper.book().read("email"),Paper.book().read("pass"));
                        }
                    }, 1000);
                }
            }
        }
    }

    private void dangNhap(String email, String pass) {
        compositeDisposable.add(apiSanPham.dangNhap(email, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()) {
                                isLogin = true;
                                Paper.book().write("isLogin", isLogin);
                                Utils.user_current = userModel.getResult().get(0);
                                // luu lai thong tin user
                                Paper.book().write("user", userModel);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }
}