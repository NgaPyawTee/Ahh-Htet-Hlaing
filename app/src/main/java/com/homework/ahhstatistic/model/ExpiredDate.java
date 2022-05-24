package com.homework.ahhstatistic.model;

public class ExpiredDate {
    String amount, percent, date, currentTime, imageUrl, id, color;

    public ExpiredDate() {

    }

    public ExpiredDate(String amount, String percent, String date, String currentTime, String imageUrl,String color) {

        this.amount = amount;
        this.percent = percent;
        this.date = date;
        this.currentTime = currentTime;
        this.color = color;

        if (imageUrl.trim().equals("")){
            this.imageUrl = "";
        }else{
            this.imageUrl = imageUrl;
        }
    }

    public String getAmount() {
        return amount;
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
