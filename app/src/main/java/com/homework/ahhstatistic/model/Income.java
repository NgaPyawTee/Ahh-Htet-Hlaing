package com.homework.ahhstatistic.model;

public class Income {
    private String totalIncome, totalAmount, totalDailyProfit, amount811, percent811, fullProfit811, paidProfit811, earning811,
            amount58, percent58, fullProfit58, paidProfit58, earning58,
            amount456, percent456, fullProfit456, paidProfit456, earning456,
            currentDate, millisTime, id;

    public Income(){

    }

    public Income(String totalIncome, String totalAmount, String totalDailyProfit,
                  String amount811, String percent811, String fullProfit811, String paidProfit811, String earning811,
                  String amount58, String percent58, String fullProfit58, String paidProfit58, String earning58,
                  String amount456, String percent456, String fullProfit456, String paidProfit456, String earning456,
                  String currentDate, String millisTime) {

        this.totalIncome = totalIncome;
        this.totalAmount = totalAmount;
        this.totalDailyProfit = totalDailyProfit;
        this.amount811 = amount811;
        this.percent811 = percent811;
        this.fullProfit811 = fullProfit811;
        this.paidProfit811 = paidProfit811;
        this.earning811 = earning811;
        this.amount58 = amount58;
        this.percent58 = percent58;
        this.fullProfit58 = fullProfit58;
        this.paidProfit58 = paidProfit58;
        this.earning58 = earning58;
        this.amount456 = amount456;
        this.percent456 = percent456;
        this.fullProfit456 = fullProfit456;
        this.paidProfit456 = paidProfit456;
        this.earning456 = earning456;
        this.currentDate = currentDate;
        this.millisTime = millisTime;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getTotalDailyProfit() {
        return totalDailyProfit;
    }

    public String getAmount811() {
        return amount811;
    }

    public String getPercent811() {
        return percent811;
    }

    public String getFullProfit811() {
        return fullProfit811;
    }

    public String getPaidProfit811() {
        return paidProfit811;
    }

    public String getEarning811() {
        return earning811;
    }

    public String getAmount58() {
        return amount58;
    }

    public String getPercent58() {
        return percent58;
    }

    public String getFullProfit58() {
        return fullProfit58;
    }

    public String getPaidProfit58() {
        return paidProfit58;
    }

    public String getEarning58() {
        return earning58;
    }

    public String getAmount456() {
        return amount456;
    }

    public String getPercent456() {
        return percent456;
    }

    public String getFullProfit456() {
        return fullProfit456;
    }

    public String getPaidProfit456() {
        return paidProfit456;
    }

    public String getEarning456() {
        return earning456;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getMillisTime() {
        return millisTime;
    }
}
