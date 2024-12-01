package com.example.mybudget;

public class Expense {
    private String source;
    private String amount;

    public Expense() {
    }

    public Expense(String source, String amount) {
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
