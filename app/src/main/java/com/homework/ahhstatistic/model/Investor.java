package com.homework.ahhstatistic.model;

public class Investor {
    private String name;
    private String companyID;
    private String phone;
    private String nrc;
    private String address;
    private String amount811;
    private String percent811;
    private String date811;
    private String amount58;
    private String percent58;
    private String date58;
    private String amount456;
    private String percent456;
    private String date456;
    private String preProfit;
    private String cashBonus;
    private String imgUrlOne, imgUrlTwo, imgUrlThree;

    private String id;

    public Investor() {

    }

    public Investor(String name, String companyID, String phone, String nrc, String address,
                    String amount811, String percent811, String date811,
                    String amount58, String percent58, String date58,
                    String amount456, String percent456, String date456,
                    String cashBonus,
                    String imgUrlOne, String imgUrlTwo, String imgUrlThree,String preProfit) {

        if (amount811.trim().equals("") | percent811.trim().equals("") | date811.trim().equals("")) {
            this.amount811 = "0";
            this.percent811 = "";
            this.date811 = "";
        }else{
            this.amount811 = amount811;
            this.percent811 = percent811;
            this.date811 = date811;
        }

        if (amount58.trim().equals("") | percent58.trim().equals("") | date58.trim().equals("")) {
            this.amount58 = "0";
            this.percent58 = "";
            this.date58 = "";
        }else{
            this.amount58 = amount58;
            this.percent58 = percent58;
            this.date58 = date58;
        }

        if (amount456.trim().equals("") | percent456.trim().equals("") | date456.trim().equals("")) {
            this.amount456 = "0";
            this.percent456 = "";
            this.date456 = "";
        }else{
            this.amount456 = amount456;
            this.percent456 = percent456;
            this.date456 = date456;
        }

        if (imgUrlOne.trim().equals("")){
            this.imgUrlOne = "";
        }else{
            this.imgUrlOne = imgUrlOne;
        }

        if (imgUrlTwo.trim().equals("")){
            this.imgUrlTwo = "";
        }else{
            this.imgUrlTwo = imgUrlTwo;
        }

        if (imgUrlThree.trim().equals("")){
            this.imgUrlThree = "";
        }else{
            this.imgUrlThree = imgUrlThree;
        }

        this.name = name;
        this.companyID = companyID;
        this.phone = phone;
        this.nrc = nrc;
        this.address = address;
        this.preProfit = preProfit;
        this.cashBonus = cashBonus;

    }

    public String getName() {
        return name;
    }

    public String getCompanyID() {
        return companyID;
    }

    public String getPhone() {
        return phone;
    }

    public String getNrc() {
        return nrc;
    }

    public String getAddress() {
        return address;
    }

    public String getAmount811() {
        return amount811;
    }

    public String getPercent811() {
        return percent811;
    }

    public String getDate811() {
        return date811;
    }

    public String getAmount58() {
        return amount58;
    }

    public String getPercent58() {
        return percent58;
    }

    public String getDate58() {
        return date58;
    }

    public String getAmount456() {
        return amount456;
    }

    public String getPercent456() {
        return percent456;
    }

    public String getDate456() {
        return date456;
    }

    public String getCashAmount() {
        return cashBonus;
    }

    public String getImgUrlOne() {
        return imgUrlOne;
    }

    public String getImgUrlTwo() {
        return imgUrlTwo;
    }

    public String getImgUrlThree() {
        return imgUrlThree;
    }

    public String getId() {
        return id;
    }

    public String getPreProfit() {
        return preProfit;
    }

    public void setId(String id) {
        this.id = id;
    }
}
