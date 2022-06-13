package com.homework.ahhstatistic.model;

public class ExpiredDate {
    String amountCash, amountBanking, percent, date, currentTime, imageUrl, id, color;

    public ExpiredDate() {

    }

    public ExpiredDate(String amountCash, String amountBanking, String percent, String date, String currentTime, String imageUrl, String color) {
        this.amountCash = amountCash;
        this.amountBanking = amountBanking;
        this.percent = percent;
        this.date = date;
        this.currentTime = currentTime;
        this.id = id;
        this.color = color;

        if (imageUrl.trim().equals("")){
            this.imageUrl = "";
        }else{
            this.imageUrl = imageUrl;
        }
    }

    public String getAmountCash() {
        return amountCash;
    }

    public String getAmountBanking() {
        return amountBanking;
    }

    public String getPercent() {
        return percent;
    }

    public String getDate() {
        return date;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getColor() {
        return color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
