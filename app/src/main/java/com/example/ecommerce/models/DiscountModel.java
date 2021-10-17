package com.example.ecommerce.models;

public class DiscountModel {

    String code;

    public DiscountModel() {
    }

    public DiscountModel(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

