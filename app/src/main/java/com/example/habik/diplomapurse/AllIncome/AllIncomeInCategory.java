package com.example.habik.diplomapurse.AllIncome;

public class AllIncomeInCategory {
    private int id;
    private String date;
    private double sum;
    private String currency;
    private String comment;

    public AllIncomeInCategory(int id,String date, String currency, String comment, double sum)
    {
        this.id = id;
        this.date = date;
        this.comment = comment;
        this.currency = currency;
        this.sum = sum;
    }

    public int getId() {return id;};
    public String getDate() {return date;};
    public String getCurrency() {return currency;};
    public String getComment() {return comment;};
    public double getSum() {return sum;};

}
