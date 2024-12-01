package com.example.mybudget;

public class Income {
    private String source;
    private String amount;

    public Income() {
    }

    public Income(String source, String amount) {
        this.source = source;
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public String getAmount() {
        return amount;
    }
}
