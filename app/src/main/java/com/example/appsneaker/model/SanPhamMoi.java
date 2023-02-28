package com.example.appsneaker.model;

import java.io.Serializable;

public class SanPhamMoi implements Serializable {
    int id;
    String tensp;
    String giasp;
    String hinhanhsp;
    String mota;
    int phanloai;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public String getGiasp() {
        return giasp;
    }

    public void setGiasp(String giasp) {
        this.giasp = giasp;
    }

    public String getHinhanhsp() {
        return hinhanhsp;
    }

    public void setHinhanhsp(String hinhanhsp) {
        this.hinhanhsp = hinhanhsp;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getPhanloai() {
        return phanloai;
    }

    public void setPhanloai(int phanloai) {
        this.phanloai = phanloai;
    }
}
