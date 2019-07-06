package com.example.habik.diplomapurse.AllOutcome;

import java.io.Serializable;

public class AllOutcome implements Serializable {

    private String category_name;
    private int sum_for_category;

    public AllOutcome(String category_name, int sum) {
        this.category_name = category_name;
        this.sum_for_category = sum;
    }

    public String getCategory_name() {
        return category_name;
    }

    public int getSum_for_category() {
        return sum_for_category;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setSum_for_category(int sum_for_category) {
        this.sum_for_category = sum_for_category;
    }
}
