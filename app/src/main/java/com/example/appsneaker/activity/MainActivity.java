package com.example.appsneaker.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.appsneaker.R;
import com.example.appsneaker.Utils.Utils;
import com.example.appsneaker.adapter.LoaiSpAdapter;
import com.example.appsneaker.adapter.SanPhamMoiAdapter;
import com.example.appsneaker.model.LoaiSp;
import com.example.appsneaker.model.SanPhamMoi;
import com.example.appsneaker.model.SanPhamMoiModel;
import com.example.appsneaker.model.User;
import com.example.appsneaker.retrofit.ApiSanPham;
import com.example.appsneaker.retrofit.RetrofitClient;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewHome;
    NavigationView navigationViewHomee;
    ListView listViewHome;
    DrawerLayout drawerLayoutHome;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiSanPham apiSanPham;
    List<SanPhamMoi> sanPhamMoiModelList;
    SanPhamMoiAdapter sanPhamMoiAdapter;
    NotificationBadge notificationBadge;
    FrameLayout frameLayoutHome;
    ImageView imageViewSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiSanPham = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiSanPham.class);
        Paper.init(this);
        if (Paper.book().read("user") == null){
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }
        AnhXa();
        ActionBar();

        if (isConnected(this)) {
            ActionViewFlipper();
            getLoaiSanPham();
            getSanPhamMoi();
            getEventItemClick();
        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEventItemClick() {
        listViewHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent adidas = new Intent(getApplicationContext(), SneakerAdidasActivity.class);
                        adidas.putExtra("phanloai",2);
                        startActivity(adidas);
                        break;
                    case 2:
                        Intent airForce1 = new Intent(getApplicationContext(), SneakerAdidasActivity.class);
                        airForce1.putExtra("phanloai", 1);
                        startActivity(airForce1);
                        break;
                    case 5:
                        Intent lichSu = new Intent(getApplicationContext(), LichSuActivity.class);
                        startActivity(lichSu);
                        break;
                    case 6://xoa key user
                        Paper.book().delete("user");
                        Intent login = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(login);
                        finish();
                        break;
                }
            }
        });
    }

    private void getSanPhamMoi() {
        compositeDisposable.add(apiSanPham.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {
                                sanPhamMoiModelList = sanPhamMoiModel.getResult();
                                sanPhamMoiAdapter = new SanPhamMoiAdapter(getApplicationContext(), sanPhamMoiModelList);
                                recyclerViewHome.setAdapter(sanPhamMoiAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Error" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiSanPham.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if (loaiSpModel.isSuccess()) {
                                mangloaisp = loaiSpModel.getResult();
                                mangloaisp.add(new LoaiSp("Đăng xuất", "data:image/png;base64,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"));
                                //khoi tao adapter
                                loaiSpAdapter = new LoaiSpAdapter(mangloaisp, getApplicationContext());
                                listViewHome.setAdapter(loaiSpAdapter);
                            }
                        }
                ));
    }

    private void ActionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAoHCBQSFBgSExQZGBgYGRgaGRgYGBsYGBgZGBgaGRsaGxgbIC0kGx0pIBgYJjglKS4wNDQ0GiM5PzkyPi0yNDABCwsLEA8QGxISHTUpIyMwMjIyMjI1MjI1MDAyMjAyMjIyNjIyNTAyMjIyNTAyMjIyMjIyMjI1MjAyMjIyMjIyMv/AABEIAKgBLAMBIgACEQEDEQH/xAAcAAACAgMBAQAAAAAAAAAAAAADBAIFAAEGBwj/xABDEAACAQIEAwUFBQYFAwQDAAABAhEAAwQSITEFQVEGEyJhcTKBkaHwQlKxwdEHFCNyguEzYpLS8RVTolSjstMWJEP/xAAZAQADAQEBAAAAAAAAAAAAAAAAAQIDBAX/xAAnEQACAgICAgAFBQAAAAAAAAAAAQIRAyESMUFRBCJhcfATMlKh0f/aAAwDAQACEQMRAD8A5dUplLdYiU0iVmaEUt00lutpbplLdIoglujJboiJR1SpGBW3U1t0dUoipQMALVSFumAlby0h2Ld3WilMlK0UpUFi2Wsy0cpUctAAwtSAqWWtgUwMAqarWKKmq0CMVaIqVJEo6JVUTYNUogt0RUoqpToVi+St93TQt1IW6KCxPu60bVPd3WjbooViJt1E26ea3Q2SnQWJG3UGt06bdRZKKCyva3Q3t1YNboTpRQFa9ul7lurN0pd7dAFTct0pct1b3LdKXLdAiouW6AbdWVxKX7unQEkSmraVC2tN21pDRJEphErSLTKLQUbRKKqViLRVWkBFUoqpUlWiBakYMLUstTy1hFFBYMrUGFEcganQDc8hVK/H7YfKbdxRlzFyoCBddSZnl012ooVlkRWooeHxSXFD22DKdiPn76JNPiHIyKwCtipAUUFmgKKi1irRkWhITZJFo6JUUWmEWnQmzESjKlbRaIq1aRDZEJUglFC1LLRQrA5a0Uo+Wsy0UFipt1FkpsrUGSmOxJkqDJTjJQ2SgdibJQ2Sm2ShslACLpQHSn3SgOlIZW3EpS4lWlxKUuW6QFXdSlTbqzu26VKVQgdtaatrQLYpu2KhlBkWmEWhoKYQUATRaMi1pFoyLQBirUwtSValloKIRWiKJlpXFOQyKoMyCx2EDUQeZkDbYTPKkwWzje0Pauyo7tg5J17tCFOXdc7n2JEHKoJg6xtXO3O1aXCFOFZ11ItteZ1LRuUCBY30jrVPheMXbV247KjtcJ7xbqB1LEkmQ2oIJPPnVvwb99xLE4dbdlG0Z0tIlsDYicpLHyBMeVXRCd9F9wPiNxwXcuWgeBEXulDKGCRpkYAjdifXl0mExIuCQGEGCGEEH06eY0rluJdk07tAbjuVWCSQPUheXzPmaf7K8O/dQ65mKvlIDRCkTMADnPyFK0NxZ0i1NRQkcGY5GPkD+dHQUqFdBFFMotCQUdKaQnIKgoyihLRVNWkQ5BUFGUUJaMpqqJsmBUgK0KmKKCzUVkVMVhFKgsGRUSKJFaIooqwRWhslHIqJWlQ7FXShMtNstBdaQxVkoDLTjrQXWgBF0pS4lWLrS1xaAK26lKlKsbiUqUoARtimrYpa1TdsVJYwgplBQba09Zt0UBK2lMJbqVu3TSJRQrArbrfd00qVIpRQWJlKpuLYtrVxPBKkHxSNYBEDp7QOu8V0LpVDxvCM4Y5yQDKpCwIUTrE9TvzqZ2lo0x03s5m/gME9w3Xw4LEyTBKknclQcpPnFPjH2wABKgaABTAHkANKRW2TtRrVgHnr7qxTkzo4xQ7bbvAWUEgb6Efjv7qH+7POZQYjkQPkactWbippcgAExlT8SKBeN3IzZyIVvsoRt1E1qiJJDGBcMsgzJn5CnUFU/DrpABPoRt8Byq3RpEitUjkk9hlNFU1znaHjdzDAd1b70goXUEyqMSAYGomDrsNOtLcQ4hiSWuJZVraLICYkrc9jMwIt5lDe1v0q0jN2dXfxlu2JuOqjzME+g3PuqiftnbW4FyRbzFWdm8a6GGFlVLFSRHI84iuN/wCq2nRr2HZ1usRKYhHuoWBGveAETHUAdRzoC9qbui4nBW7ikQMgZG06RmT/AMelO0HFndr23QuqpYcKTq9xltCCSFKqSS0kDQwda6nAY1Lq509CD7SnoQP+DyryrE4TC8TKrZ76zc9mb1hmVwATlLoSJEDxNFdX2S4JicJdnEXA4KMkqxZTDKUkEDxQDqZO+upppoHHR2V3FJbAa4wUExJouG4thm8CXlYxrycxvB5+gFeGdqu1mKxJNq53aKjzCZsxgECSxPJvLlVLgrl/I91LrgW8pILvrJMRB3kDpvSd3oFFVs9+4L2iw91EuXLioWXMFJiAw0zdDHLzqyxWMk22Xxq75SV1AkaHf2dDrPTrXzdb4viA0o5H+UAFQPIEGBVrge1+MwrC4txCZ/w2WUbzbKRqPWnt260HFJd7PoE1qvMOy/bzG4u7D/u1u2ql2Yq6s4UgZLc3ILmdJ0Gvoe+wPHLF58lu4pcRmTMmZZE6rMxodYilQWWBFRIqZFRIqSgbChMKOwobCkAs4oLrTLigsKRQo60vcWnXFLOKBCFxaVK09dWliKAKi1TtkUlZqysLQWN2Ep+0tLWRTlugQxbWmEFBt0wlAgiipxUVrYoAi61592y4hikxmHw+E1Z1ZnSAVYBoliQSihVYkiIGtehtWsPjLKWy4iWALGJJGrKpn+YwPM6VMqRcb8HnOM4BjMS6HC31NkvkulVCPbgZmJBLZhG0RqwBHOun4Rwy2yOLuH7t0KwzT41aQCQSfunXemcJx4XFc6IwcgiEUHwKR4S8nQjWKqsZibzOYkJm0BJmQs6hgp26jnvWSNG3ZZOqDQRGoiBHmK5PG4ic6GQpdwcqAymc+GQsgEaaQa3heIhjcuSQFusDlGYkKxUgiCSsjl0rleJcRvC4XtschcHLESCZIIglZkDXrVpBKR1eBcRlWYG0zOnrrVpYu5dzp+HnXP8ACcQzqtx0yEk+HNm0kjcdRVji7hCsQA3KGIVYJglixACgEk6itVo55bZyPG2tYu8b2HvFHbwmQyq4CjLkJiDAAI98c2EOHFbL38K16LJVMSjMyFs2oYd3Ep4spE6aEEgmj3eF2TJQ2bjyzKq4u20kzp3ZRVO+ysCNIPOgYO4lwLYuXirplIW47LZvAqGKOR7LqTAfciPUSa9a8Ev/AMgAspbRVZgrlp0C90kqSumjEnp86Vw+Nu3HdCwQJa71oGUgLazlRzJLFR5AV1WLW0+FbDPgr1psrd29lP3lcxJMi6pkydwTsd6oeIYbDoTaFx72Ja5cK27CjMq3bYQ2nYggQFgwCRroN6VIds6nsCmXD964uKzKpZ7rEq4GZgygscqANvpNdLbxyMVKkEFtNYzeEnwjciCDPQgiQZrnOzXZq5bRWxbs8QUsMxa3ayiEBGzsoA/yg6jXWr3F2ENy3cZRmRiFb7oceL45V+FZN/Oi6fE8R4i2e6xJ1zMup5J4V9NBHuqXDvE+QnwNJZeRygsJ94FAx6zdcgEjOxHmC5iPdTOAsvlZ1Qll5QToQVJ9PEPhXfiV5F+aOSekxjEoFAC/Z90gIZreFwIuL4nUBZYBjGcloEKAWbc6qNlbpVz2axeCtMrY6xed5Jk289lfu+EOM0DeVJkmug4zgcJjEz8POHs3RAcHNhwywZ/hus5tvEu+s8o6J5o21xr7mSi67KheFpcwYyXLee2zA5GKhw5aFdWAZGiQIkDKOWgpuC5kYrk8dpgchA1ZXMhhpIg5SBuG10NEu8AvhiWuWgCYY53AynTKCVCxHLXavR+yz3Vw9rFW7Yd2TJce06Le/hMUAZGIS6oykzmnYAVycqZpVouuxF9mwi23tvba0SmR0ZDkGqEBiZXKRBBI03NdAao7PatLlxLL5Q7MFhw1m7uP/wCbgE68lnlV4am7HVEGqDVNqg1IYJhQXo7UF6Q0LOKXemXpZ6QCtwUqRTVyljQIp8OKsrFV1irGyaZVlhapu3Sdo00hpCHENGU1WvjQsmCYnYSdOg+HrNDwnErly4QLOVNId3ysevgCmIMjUjap5xLUGy6VqkDSVnFo7FBMqJMggbxoTvy+IpkNWiafRm7XZrFSbbhdyrAepBitFFZFQk5GAUEZ0zKFEBHiHkLzkHlFJWuLhcamHYoFyFyD7bQGjKJ2kdPsn3OcU7RyoTD2s5JZTndLUFVkKoYySeUCNDJEVk6k6LVxVlVYHds6hnUM5IQI7tlyJbGqghVlN41g+lVnGuLWcIii4ZceIWlXKzZyTL6LA8LDYDTQHSiXu02IRe7bB4hNyWtr3hiZjOkjykawN964rilq2L3eLZd1clnDrczZiTOYtOaSSffVwxfyM8mb0V2Fx6pmGsks7HwlfE06ag75t/jRlw73y3cEuzFYVQOatLMWPgAyrvp4hrQOJrfZ4tgsjKICJCydzosjmOtV1ngGMBBt23BPQ5IHTxEEircY+AjKVbO1wyd2O7JBKQpgyM0AnX300cMl5ClxQ6yNGEiROvrVDwvB3sOmS6BLMWUAzAgDUxqZ9a6LAhcogzpJ/mkabbanXyrLJ0bY+9lNa7PYdHHgVpaIZZiMvP8Aq+VWl/g2GME2Lc6a5V5AVu5/iA+Y/L9KZvvsPrlXPJs6Ul6C8P4XZRPAgXT7JK+ewNGwvDbNol7VtEcgjOiKrayTqB5A+6h3Meli2huSFfwqdzO23TeT+OsZjeJi3bNw2yVQDPJhldmKhIg6xnM9I+9o1CbRLlET47j3tlT3jjRtnImI5e+qzhPEb9y8FzlyVcqjscuYIxE01x9O9t27oWcyuUKyQRnXl96ANNYnc70j2etsmKtZhE5/X/DflWsYtJJmUpJ20c3eEEil7mJW2AxklSpUDrm1n+gt74pvEr9rr+lUmKxKumUSNQRPlI5eRPwrbHGV3HwZyqqZ0D8Zshc2eeij2vhy99c7xHiD3zJ0UeyvIeZ6nzqNjh1x1Z1C5VgMS6LE7aMwOtXy/s+4n/6X/wB2z/8AZXRlyzkqkqRhHHGO0cuAK3E8pp/jHBMRg3FvEWwjsuYLnR/CSRJyMY1B36UlaOUzrtyrHizSxng/+Pa8RSLiQw3Vp8LR5NFfTYnnvzI0E89K+YVfxqwmVZTqFGxG593Ovp0tOo2NNxa7FZhqLVsmotUjBtQnojGguaTAC9L3KO5pe4aQCtw0saYuGlzQBS2DVjZNVdlqInEra/a28idvSlJpdlRTfRdW8VbVsr3EQ/5p/IGl8bxixbBPeh45IGE+QkQTMGZ5GuSui4WJW8kE7tbJb3nMJo1q2CIa9cPXIltfhmuNWMp2WsUvDQzje3GW2Fs2wr65gRmmdFykcvTWa9BZCWzgqAQMv8RFJERtmEz515s9jCW5u93cd1GZWuMp1XUGA5AOn3arsR2mRp/hKCebOT/astP2W1NUlTPTsFcUXGt6ZkXVswM5iNJBIMZNfd51yfbftvewl23Zw2QEKzOWUOGliqLrtopOn3hVX2Z47/EZnVXULoqkLBneQDynfrS/bngV97qX7VpnVkVSEUuyss7qBMEEa+u3PaEvBLg+Nvstcf8AtRtNZKphmLsBmDZVUHfR1JYgHUQF91cZw6xfxbM62lInxu+dlUzMAuxk+Xxq14N2JuOQ+JGReSA+Nv5iPZHz9K63EDu1VFVVVRCqqgAD0q214KjFvbObvpcw+RFxF0HLmIDkoNSAApkfZ+dMnjeLYAd8ZH2sluf/AIULiDzckwfCB8OVARzmjLI8iPwNZNuzSkXGCxl5we8uTMBfBbBDEwPsxG/KfOkcRib3j7y88BWgJ4MsLzKQTrTRuIi5m9kHaNdmIjzkVRYniiOl6AFBBVQT4jmG+WOvLpVpszaimMYLFLcJAZmIiSzMx1n7TEztV1g7xGmn0TXLcD0LH+T8D+tdXgm0FE9xFD9xq5c8YPp+VHu4jUEgmNwu58h5mgO3j94/AUSwha4rZoCHMdAS0CcoBIB1yiJHtAAgkVko20aylUQHEMdaxWLW5eJt4eyVKQAV7tHHgCzJLBdgJ1G8GgYztBZxVvGWDKvev57TDQEKgVASRodJjQHaRpM+1h/gqVtrlBLMMhQzJGzW0ZguaZP3YM71y/Z64y3cyiWHiEZiwPsyMrodnMjMJGldblTo40rVnW4TFumFTDZCVW47h28RVQgOhURqzD0DLzOi/Zw5sUjMZMOffkI/Or3iItuv8S46gsGViqBQkyylgzFgIiQQssNzqEOB8MuW7+a4ugRoYEFSSQNPcTvRNbQ4tUzlsVYKM9pt0LLP8pIn5VRf9NFtnS84VgAU3hiQ2sldvDzjeu07XYXJe7yNHUT/ADL4T8sp99UmNx+HIH7zh80yAyeEgLHmD9raavE2m0OW1ZQYR/A42nLsY679RpXpeH/aMi4ZQ1t2xCqFjQW2YCM5aZg7xG+nnXFX+F2TbFzDO7Zj7DhQeY38iarL6NbOV4UnlIOnXwzXpYnhnFKT6fs55Jp6DY/FPfuPdutmdzLMfhoOQAAAHIAUiyUxZAY+2o82DQPPRT9GmreCQkTest4gMs3AWGhgEJpPs+R8oJ6pxg4kJuxp+yuKXDLisqsjLnCqwNwIdA5Qa5dtp67a17nwbEd5h7Nz79q23+pFP51Q8H4thsZjUuLbe2wtoqLnCImUsTopEk5gIgjQe7pr99Vu9yEC+EkQVjwkSMo9nQg6xOsV4+TK5JRa6NkghNQY1zWH7ZWmxLYV7boT/hPlZ1urJGYBVkCQddRpqRtTeP413JzXBbW3r7V9VuuAAc1u2ygPE6rmnUQDtWDaLLVjQXNK2uIi6neWoKQrB3lUZYzMQYkQuuo5H1oNriti47Jbuo7KFJCnWGBIMegpWgGXalnapu9Lu9MAVxqATUnaglqCTird27/3G+C/7aUW8zQZiQDpH6VZi00HYac6TTh7aDMB8a45ZL7Z2xhXSBhm+83xoiT95v8AUabThw5sT6afiDTCcOTmX+K/7azc4migyte2SjsAWCqWaDOg3OppPAYp7dxXEKF6ABtV23nn/emuPolsBIcgqTlBgEgxmY7QCRp/mrmc7iAWgGAJAk9IXcDTc10/DtNP6nn/ABkW2kn0djhOOW7OJGJvJ3ttcubMAWA1mFnVpy6nlO9dXxbtNYxGENzC3O7JUE5spKtztlm0TpmIP5144yNkZiScpUHmBmDDcGKlwo+IwMzGAqnYknn5CAfcOlaTSS0Vg5JKLdnQ4bit93FwXPMnNznad48oirqzxm9fR7JUFpzG4TIRdFChNCZykjXma5K9gbks5EGZyrmE6TygKukTT/Z5wjsAGBYA6gwRpBAIJgGZZj9pYGtO042ZYotZKvXovMRgSQvjOkySNTty5c6We1lOqz5yRT5eRFL32MiFmayZ6KA4m5mtshXQa7zsCPzoDcBuXEGXKpcAqpBBKyJYdd589OtGxgASPvEDXfUiT8Jpa3dJcl1zIqaqS2QJaEZFI5kACNQI58hIiXYPCYcJcI71lTUksgQsVHsrmDEE7SQAIPpTwx122FKZGU6gEkOE6sOQnSRIojcNW4iYm3b8JzEqSxyqpAjJJA/mkz0ERVPhsS1iSkPbLAD7JTMTMSJB1IzdPSqpEU7OgtY5bniGh+7MxGh1gSJB5D0FXHDhAFxQrEGHV82UgqXEAaGQ6gzoYHQVzePtrbIuW7gYvDInssQRvlGyjz099P8ADeLBcwYSCq+EEM4YeA+FeUBTPmelEElIMilKNpC3FBcuHEXLgFtBbdltKVKiFKg+ELBLMhkiTAB2qk7PCbxGoIRXBBIIh7ZJ01iJ0qy7Q4zvLfdW1AdyO9Kw0KjFkQkbmTPl7zVFh81q8twq2Q+FwoKnKyZWg9QDI8wKqU48iIxfE9JVbl62lu8EhWVnZFJLlNV8T7AE6SrQJGsmaLjfF71hwTddARaGXLbhS1tSdWQ6gzOvX0qOF4/3ad2VZyE0urkyNp7WrCPQ69Yqu4ky4khbt7IihfGwLM7KoAbL5qDy3aKqc00kmTijTtoJxG9cvKBdxDQPHJ7uQIOoUWwTOnPrvVBewLXGKqxOUE+MQ0GPsgeXrTQwKHvL1wSiwEjSYJXJ5nqZkZIIhhVTZuMjKwcqSCYEEBdwNSN9dPSlHlHaezqc8UmvkpXunssrfD8VaBS2VYanTl5jOBHpVLdRsxzzmnxTvNdBgbmJxHizJ3YMsCoywolgQIMar4SRmnSQDBcdwVLpFy0coI8SgSARsQNIBHIRtsK2xZFF3Ol9jLLCD1it/Vjn7OeFpiMQpuWxcVT4laMqgAQTOjEk6DyrvP2kdkbL2bmLs2VR7aZi1s5SQknMUEKdAZbeAOkVzP7PUXCtcd7iwxCoG8MvHi2k7FNeU+deqvjrNy3kN228qwcFgJQghhM+Hnqeh92k8zlLlF6OVx46Z51+zbi5vl7ZtqpTKxdebsWmCpG8DSa9LNvNbZbZy5lOUiTlYaqYZQAPMjy1mvGuxKDB43E2Lj6JAn2gyK8K/h5Qyn+rlXsmBUvbMXAZhkYKGHWdd58o8jzrKWw6Z4pb4nesOQjuMjtnssxWHzEMVB0DzMjYmdjJPQWO0+HxTJhblt2NyQ+cRkYCVJUyGEg6GRIEiNKY7e9mEN794QlWeO8ySFzRCtlaRqFIP8o61ydvhuJsOHtOrEbT4THMcx+FccppSpnUoco2jtb/AA3E4Z+9wdwOhyh7dxnMkZRmzMxWSSzMRk0AADGnU4Pcu2ycXbVHIeLmFZlYZ+bLH+JH2iG2O1crY7Z37QC3sOwjmoP4rIpm1+0uzzVh75/OhSbJeOi0t8IxpuFRjAyakEoodddEJCkMABuYJnalMbwvG297lxgPtIEce+En4ilMX2/S6pNq0zMBIOQkz086q1/aNdQw9qPIiPxFVykwSSGmv3v+8/8Apt/7KD3t3/uv8E/20SzxFsYzXGVVmNmUk+oUmOW9E7o1m8jTqzVQi1dEMk1JLQofeTWlcnnXMzoQ0o+h+v6UZRPl6UkHI5/OjJcPUj31DRSYjxu4i3La3ICFWksYGpUgf+JPuql41gYCuqlbdxVZFjMQCFJUj2tDBjbWrLj627ql3JItsocDU76DynOB/V5UnZC4lu8xN1baHwhR4my7Qq6wBO5G9dOO4U/Hk5fiOM1xVXen1XuzmrbasGUkEQSAp1G2oBj3a+dWF/Bfu7W3tyc0nM2xIB8Om2n1pXouDfBW7YRHs5QI8Tq5jc7mfdXNcexmHOVMMgbU5lVcq6eKVUjf2jtHnTj8Q8kuPFpb/GVLBHElLkm9a/w0cYmIwme6YKOVRSASRlQQfMEM08qpsNwi7euHuElwQSJEa88zAySdZIUAjSeWIsFHuK4sFmGkTmGrLMwGOnPb0NdjwvtPhLaBEtMg6LlM+ZYsCT60Sc8cagr9ekZJ455eU2oqqftv2V72rtoAXkKN5kEGOYZZBpZkuXG8KZve22/IVacY7YW2AS3bZmJGWSJmdAFWZJ2iedWrPAy/E1n+pkUU5KmdKWOTqDtI5e3hMStxLndgqjq2UuQSFMkCdjVle4EmIJuYZygdgbtpxBBgkgT7BO33SDodpsM/1+FAxlgXIIZkcbOjFXHvG48jIpLO72OWJVo1ieEXLirbdbwRdRbRJUkeySToYHzk+kF7NiMzNlUHXvmCoixuyoROseVVvE7fEDATErA6IqMfMlQZO3SudxXBsZdP8Vy8c3ctE9Adq6Izi+5IxbmrSTOlb9yVmC38O5jUlriqNIAU+KVHkal+/YK2io1xbhMeDDoFLmdM7sYjbQ5Z38qp8J2dSALsE/5ZBPnIp6z2dsr4ghmdJYmKHlgChOiwxNpYXIABGy7fKlCCTHup/EgrlAGgGkDof70qx15/DY/UVzI3YBMFncQi5jP2QW0EmOZMA1o8CLsWYKCTuwZWPTdegHwqOPW41s92xRtwQSpB6gjUVW2+LcVTRbrkdYVvmwmuvFxr5mYvJODuK/otcTw97dp7ZKsjAmBJZCNJBI8ue2Ucq59+Fi5qH1iAN1EbATJA/WrK12nvL4Mer3BMqysEdDEeHLCn31bWuM4C7E3yv+W5bmPVxAmt2rriyIZYq/1I3frVCGGwn/6pW3vPjWIaFzROp5ktvEkkRtTvCMI1vDh7pAe8wZEOjC0itJjlLOp/qWncPxHhttvC/eNGipbKhoOgOWZ1PUeelFLd9N24IcxCjZEHsoPIfMydKyzNRW+2ODcvlj0nZV3sTcsgkWxcRjLJALAjmFP5QdKnh+2uFUZXwzCOXeOI1k6T1FNMw2IigvZVtwD6isYZnFUOeFSdiuM4kmKupisHbyOgKXFYk96rAaEtuYzDXyq44R2lu2my2LgRedm7oynouZSCJ56HzO5TtQugAFEvYVXG0+R1HunaqWd2S8Cou8Zxi5eR2xDIGZURFRpMh1cnQREK3x+FctzqJqusYZLZkKAeoAB+VMM46n4ms8j5OzTHHiqHiisNIpS/gbb+0iv/ADKG/GhhyNQfnRFxX3h7xWdV0XafYunBrAOYW1UjaF0pu5gh0/St552MisFwjY0nb7DQNMOE5D3aVuR0+VTF4HfSsn0pgVoeph+tLB4/U/lWw3X+9ILGVudPr686kG+hv8aXVo8vrrR1GktoPmfdSaHYFnFp+9yZlZSlxcpYMhG7KNWA28sxPKqfEYfBsSbWJNv/ACsFdR5K+ZSR6g+tXrXp0Gg+fvNVuJ4ZZfVkEnmPCfiK3x5aVM58uFSdlWbeFT28W7j7qIFn+rM8f6aJhOMIrqmFsEFjDOxLXCuzAHZQRM5QvpTC8CsDXJP9TfhNP4bDpb0RVXrA/PnVyzqqSJhgp2TuC5bDNbVXDj+JbaMrH0IKn0I8/WqbiKg+Lhiz5ZgPggC1eB+Q+v70ZIUef1oKzhmcVRpPDGTsocPicS5ixhUw86F8oVgOcMQGPzro7RKqFmYAE9TQnePf+Fazx9fX1FRlyOfZpixqHQfPH18/rrW1IH1r60BWmpK0mPqKyo1saVuf15fjS901tnpd2/CkkFk0aNNv+aKr/L6/Sky8nXrPwoyN9etVQrJ4l9vfSwI+h9edTxJmPfSyEj461aWiGxtVWNRvtWglsfZ+tf7UuXk9B/fettcp0Fm8Th7bxKAjnPSlP+g4eZ7sfFo+E09baoM5GnwpptdMTSfaN4fCW7XsIo9B+NEF6Dp9f2oQufXTrQ3IGo2ooLHXcOPOl88aGhJc+v0qbsDofcaKHZtjzo9m950mDGhrYaihWWDsG8jSzyN/nt8eVBFyiC7OhqqFZov/AMVvP7/TegusbfCoK/uNMVhhc5j5UVcV1+NKF5qJepoLLDvJ8/StZvWq8PGo0qX7yegpcQ5EQff51NBP6msrKSGFzAbb/Ww5epqBc86yspMZrPNaDdKyspAEz1EtWVlABbZy71I3OZrKymMjnk/jUjc5VlZSAkXito+56/X6CsrKGUbZ9Pd9fjQC+vxrKykhM1n1iiI/19e+srKoAd99B60HvIMisrKaIZHP/wA9fOs7ydeoH6fl863WUxGB6It3asrKYyNxtz9etaV6ysqiSEx+VaFw/XI1qspATzzWw9ZWUAazVrPW6ymBIXKi46VusoECZ43+P61ne9aysoEQa59Co5j5VlZQB//Z");
        mangquangcao.add("https://bizweb.dktcdn.net/thumb/grande/100/347/092/articles/giay-sneaker-la-gi-1.jpg?v=1599104206663");
        mangquangcao.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTLZAdzB4WnkZxes5dSAFtbJTE9grKraBrsag&usqp=CAU");
        mangquangcao.add("https://cdn.shopify.com/s/files/1/1404/4249/articles/giay-sneaker-nu-dong-hai-zucia-gjb03-trang_1024x1024.png?v=1585367810");
        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayoutHome.openDrawer(GravityCompat.START);
            }
        });
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for (int i =0; i < Utils.manggiohang.size(); i++){
            totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
        }
        notificationBadge.setText(String.valueOf(totalItem));
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar);
        viewFlipper = findViewById(R.id.view_fliper);
        navigationViewHomee = findViewById(R.id.navigation);
        listViewHome = findViewById(R.id.lst_view_man_hinh_chinh);
        drawerLayoutHome = findViewById(R.id.drw_layout);
        notificationBadge = findViewById(R.id.menu_badge_home);
        frameLayoutHome = findViewById(R.id.gio_hang_home_click);
        imageViewSearch = findViewById(R.id.img_search);

        //recyclerview
        recyclerViewHome = findViewById(R.id.rcv_item);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewHome.setLayoutManager(layoutManager);
        recyclerViewHome.setHasFixedSize(true);

        //khoi tao list
        mangloaisp = new ArrayList<>();
        sanPhamMoiModelList = new ArrayList<>();

        //gio hang
        if (Utils.manggiohang == null){
            Utils.manggiohang = new ArrayList<>();
        } else {
            int totalItem = 0;
            for (int i =0; i < Utils.manggiohang.size(); i++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSoluong();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }

        frameLayoutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGHHome = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intentGHHome);
            }
        });

        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }
}