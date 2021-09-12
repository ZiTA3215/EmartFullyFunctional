package com.example.ecommerce.models;

import java.io.Serializable;

public class MyCartModel {


    String productName;
    String totalQuantity;
    int totalPrice;
    String img_url;

    String documentId;

    public MyCartModel() {
    }



    public MyCartModel(String productName, String totalQuantity, int totalPrice, String img_url) {

        this.productName = productName;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.img_url = img_url;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
