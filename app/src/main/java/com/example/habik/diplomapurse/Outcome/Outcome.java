package com.example.habik.diplomapurse.Outcome;

public class Outcome {
    private  String date_outcome;
    private String category_outcome;
    private int sum_outcome;
    private String currency;
    private String name_outcome;
    private int sum_in_rub;

    public Outcome(String date_outcome, String category_outcome, int sum_outcome, String currency, String name_outcome, int sum_in_rub) {
        this.date_outcome = date_outcome;
        this.category_outcome = category_outcome;
        this.sum_outcome = sum_outcome;
        this.currency = currency;
        this.name_outcome = name_outcome;
        this.sum_in_rub = sum_in_rub;
    }

    public int getSum_in_rub() {
        return sum_in_rub;
    }

    public String getDate_outcome() {
        return date_outcome;
    }

    public void setDate_outcome(String date_outcome) {
        this.date_outcome = date_outcome;
    }

    public String getCategory_outcome() {
        return category_outcome;
    }

    public void setCategory_outcome(String category_outcome) {
        this.category_outcome = category_outcome;
    }

    public int getSum_outcome() {
        return sum_outcome;
    }

    public void setSum_outcome(int sum_outcome) {
        this.sum_outcome = sum_outcome;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName_outcome() {
        return name_outcome;
    }

    public void setName_outcome(String name_outcome) {
        this.name_outcome = name_outcome;
    }

    public void setSum_in_rub(int sum_in_rub) {
        this.sum_in_rub = sum_in_rub;
    }
}
