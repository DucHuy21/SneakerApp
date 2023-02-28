package com.example.appsneaker.model;

public class LoaiSp {
    int id;
    String tensanpham;
    String hinhanhsanpham;

    public LoaiSp(String tensanpham, String hinhanhsanpham) {
        this.tensanpham = tensanpham;
        this.hinhanhsanpham = hinhanhsanpham;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTensanpham() {
        return tensanpham;
    }

    public void setTensanpham(String tensanpham) {
        this.tensanpham = tensanpham;
    }

    public String getHinhanhsanpham() {
        return hinhanhsanpham;
    }

    public void setHinhanhsanpham(String hinhanhsanpham) {
        this.hinhanhsanpham = hinhanhsanpham;
    }
}
