package com.example.habik.diplomapurse.Balance;

public class Balance {
    String month;
    double sum;

    public Balance(String month, double sum) {
        this.month = month;
        this.sum = sum;
    }

    public String getMonth() {
        return month;
    }

    public double getSum() {
        return sum;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
