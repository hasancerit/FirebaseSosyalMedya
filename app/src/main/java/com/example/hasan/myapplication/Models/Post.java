package com.example.hasan.myapplication.Models;

import com.example.hasan.myapplication.R;

import java.util.ArrayList;

public class Post {
    private String content,desc,type,kişi,likeSayi,yorumSayi,id,paylasanId;
    private ArrayList<String> pointler;

    public void setContent(String content) {
        this.content = content;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }

    public String getKişi() {
        return kişi;
    }

    public void setKişi(String kişi) {
        this.kişi = kişi;
    }

    public String getLikeSayi() {
        return likeSayi;
    }

    public void setLikeSayi(String likeSayi) {
        this.likeSayi = likeSayi;
    }

    public String getYorumSayi() {
        return yorumSayi;
    }

    public void setYorumSayi(String yorumSayi) {
        this.yorumSayi = yorumSayi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaylasanId() {
        return paylasanId;
    }

    public void setPaylasanId(String paylasanId) {
        this.paylasanId = paylasanId;
    }

    public ArrayList<String> getPointler() {
        return pointler;
    }

    public void setPointler(ArrayList<String> points) {
        this.pointler = points;
    }
}
