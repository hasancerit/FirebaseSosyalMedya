package com.example.hasan.myapplication.Models;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;

public class Filter {
    private ArrayList<String> istenenAlanlar;
    private ArrayList<String> istenenTür;

    public ArrayList<String> getIstenenAlanlar() {
        return istenenAlanlar;
    }

    public void setIstenenAlanlar(ArrayList<String> istenenAlanlar) {
        this.istenenAlanlar = istenenAlanlar;
    }

    public ArrayList<String> getIstenenTür() {
        return istenenTür;
    }

    public void setIstenenTür(ArrayList<String> istenenTür) {
        this.istenenTür = istenenTür;
    }
}
