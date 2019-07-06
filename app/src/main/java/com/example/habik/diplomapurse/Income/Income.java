package com.example.habik.diplomapurse.Income;

public class Income {
    private  String date_income;
    private String category_income;
    private int sum_income;
    private String currency;
    private String name_income;
    private double sum_in_rub;

    public Income(String date_income, String category_income, int sum_income, String currency, String name_income, double sum_in_rub) {
        this.date_income = date_income;
        this.category_income = category_income;
        this.sum_income = sum_income;
        this.currency = currency;
        this.name_income = name_income;
        this.sum_in_rub = sum_in_rub;
    }

    public String getDate_income() {
        return date_income;
    }

    public String getCategory_income() {
        return category_income;
    }

    public int getSum_income() {
        return sum_income;
    }

    public String getCurrency() {
        return currency;
    }

    public String getName_income() {
        return name_income;
    }

    public double getSum_in_rub() {
        return sum_in_rub;
    }

    public void setDate_income(String date_income) {
        this.date_income = date_income;
    }

    public void setCategory_income(String category_income) {
        this.category_income = category_income;
    }

    public void setSum_income(int sum_income) {
        this.sum_income = sum_income;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setName_income(String name_income) {
        this.name_income = name_income;
    }

    public void setSum_in_rub(double sum_in_rub) {
        this.sum_in_rub = sum_in_rub;
    }
}
