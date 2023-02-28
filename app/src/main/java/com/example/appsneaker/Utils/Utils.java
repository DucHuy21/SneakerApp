package com.example.appsneaker.Utils;

import com.example.appsneaker.model.GioHang;
import com.example.appsneaker.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL="http://192.168.1.12/sneaker/";
    public static List<GioHang> manggiohang;

    public static List<GioHang> mangMuaHang = new ArrayList<>();

    public static User user_current = new User();
}
