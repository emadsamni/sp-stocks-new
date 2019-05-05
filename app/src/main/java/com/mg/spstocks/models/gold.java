package com.mg.spstocks.models;

import android.widget.LinearLayout;

import java.util.List;

public class gold {
           private   int id;
           private  String image;
           private  String gold_name;
           private Type gold_type;
           private List<GoldLog> log;

    public gold(int id, String image, String gold_name, Type gold_type, List<GoldLog> log) {
        this.id = id;
        this.image = image;
        this.gold_name = gold_name;
        this.gold_type = gold_type;
        this.log = log;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getGold_name() {
        return gold_name;
    }

    public Type getGold_type() {
        return gold_type;
    }

    public List<GoldLog> getLog() {
        return log;
    }
}
