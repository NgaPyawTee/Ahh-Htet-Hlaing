package com.homework.ahhstatistic.model;

public class DeletedInvestor {
    private String name;
    private String companyID;
    private String phone;
    private String nrc;
    private String address;
    private String amount811Cash;
    private String amount811Banking;
    private String percent811;
    private String date811;
    private String amount58Cash;
    private String amount58Banking;
    private String percent58;
    private String date58;
    private String amount456Cash;
    private String amount456Banking;
    private String percent456;
    private String date456;
    private String preProfit;
    private String imgUrlOne, imgUrlTwo, imgUrlThree, nrcImgUrl;
    private String id;

    public DeletedInvestor(String name, String companyID, String phone, String nrc, String address,
                           String amount811Cash, String amount811Banking, String percent811, String date811,
                           String amount58Cash, String amount58Banking, String percent58, String date58,
                           String amount456Cash, String amount456Banking, String percent456, String date456,
                           String preProfit, String imgUrlOne, String imgUrlTwo, String imgUrlThree, String nrcImgUrl) {

        if (amount811Cash.trim().equals("") | amount811Banking.trim().equals("") | percent811.trim().equals("") | date811.trim().equals("")) {
            this.amount811Cash = "0";
            this.amount811Banking = "0";
            this.percent811 = "";
            this.date811 = "";
        }else{
            this.amount811Cash = amount811Cash;
            this.amount811Banking = amount811Banking;
            this.percent811 = percent811;
            this.date811 = date811;
        }

        if (amount58Cash.trim().equals("") | amount58Banking.trim().equals("") | percent58.trim().equals("") | date58.trim().equals("")) {
            this.amount58Cash = "0";
            this.amount58Banking = "0";
            this.percent58 = "";
            this.date58 = "";
        }else{
            this.amount58Cash = amount58Cash;
            this.amount58Banking = amount58Banking;
            this.percent58 = percent58;
            this.date58 = date58;
        }

        if (amount456Cash.trim().equals("") | amount456Banking.trim().equals("") | percent456.trim().equals("") | date456.trim().equals("")) {
            this.amount456Cash = "0";
            this.amount456Banking = "0";
            this.percent456 = "";
            this.date456 = "";
        }else{
            this.amount456Cash = amount456Cash;
            this.amount456Banking = amount456Banking;
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

        if (nrcImgUrl.trim().equals("")){
            this.nrcImgUrl = "";
        }else{
            this.nrcImgUrl = nrcImgUrl;
        }

        this.name = name;
        this.companyID = companyID;
        this.phone = phone;
        this.nrc = nrc;
        this.address = address;
        this.preProfit = preProfit;
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

    public String getAmount811Cash() {
        return amount811Cash;
    }

    public String getAmount811Banking() {
        return amount811Banking;
    }

    public String getPercent811() {
        return percent811;
    }

    public String getDate811() {
        return date811;
    }

    public String getAmount58Cash() {
        return amount58Cash;
    }

    public String getAmount58Banking() {
        return amount58Banking;
    }

    public String getPercent58() {
        return percent58;
    }

    public String getDate58() {
        return date58;
    }

    public String getAmount456Cash() {
        return amount456Cash;
    }

    public String getAmount456Banking() {
        return amount456Banking;
    }

    public String getPercent456() {
        return percent456;
    }

    public String getDate456() {
        return date456;
    }

    public String getPreProfit() {
        return preProfit;
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

    public String getNrcImgUrl() {
        return nrcImgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
