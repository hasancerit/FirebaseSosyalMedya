package com.example.hasan.myapplication.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String desc,email,name,post,takip,takipci,imageUri,filtreVarMi;
    private ArrayList<String> mList = new ArrayList<>();

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setTakip(String takip) {
        this.takip = takip;
    }

    public void setTakipci(String takipci) {
        this.takipci = takipci;
    }

    public String getDesc() {
        return desc;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPost() {
        return post;
    }

    public String getTakip() {
        return takip;
    }

    public String getTakipci() {
        return takipci;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public ArrayList<String> getmList() {
        return mList;
    }

    public void setmList(ArrayList<String> mList) {
        this.mList = mList;
    }

    public String getFiltreVarMi() {
        return filtreVarMi;
    }

    public void setFiltreVarMi(String filtreVarMi) {
        this.filtreVarMi = filtreVarMi;
    }
}
