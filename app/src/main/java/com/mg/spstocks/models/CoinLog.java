package com.mg.spstocks.models;

public class CoinLog {
    private  double  buy;
    private  double sell;

    public CoinLog(double buy, double sell) {
        this.buy = buy;
        this.sell = sell;
    }

    public double getBuy() {
        return buy;
    }

    public double getSell() {
        return sell;
    }
}
