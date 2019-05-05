package com.mg.spstocks.models;

import java.util.List;

public class Coin {
    private int id;
    private String image;
    private String coin_name;
    List<CoinLog> log;
     public Coin(String coin_name ,String image)
     {
         this.coin_name = coin_name;
         this.image = image;
     }
    public Coin(int id, String image, String coin_name, List<CoinLog> log) {
        this.id = id;
        this.image = image;
        this.coin_name = coin_name;
        this.log = log;
    }

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getCoin_name() {
        return coin_name;
    }

    public List<CoinLog> getLog() {
        return log;
    }
}
