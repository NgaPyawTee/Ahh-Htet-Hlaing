package com.homework.ahhstatistic.model;

public class Income {
    private String totalAmount, percent, fullProfit, paidProfit, income, date, milleTime , id;

    public Income() {
    }

    public Income(String totalAmount, String percent, String fullProfit, String paidProfit, String income, String date,String milleTime) {
        this.totalAmount = totalAmount;
        this.percent = percent;
        this.fullProfit = fullProfit;
        this.paidProfit = paidProfit;
        this.income = income;
        this.date = date;
        this.milleTime = milleTime;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getPercent() {
        return percent;
    }

    public String getFullProfit() {
        return fullProfit;
    }

    public String getPaidProfit() {
        return paidProfit;
    }

    public String getIncome() {
        return income;
    }

    public String getDate() {
        return date;
    }

    public String getMilleTime() {
        return milleTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
