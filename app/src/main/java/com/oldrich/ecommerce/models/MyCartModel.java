package com.oldrich.ecommerce.models;

import java.io.Serializable;

public class MyCartModel implements Serializable {


    String productName;
    String totalQuantity;
    int totalPrice;
    String img_url;
    int cartbadge;

    String documentId;

    public MyCartModel() {
    }



    public MyCartModel(String productName, String totalQuantity, int totalPrice,int cartbadge, String img_url) {

        this.productName = productName;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.cartbadge = cartbadge;
        this.img_url = img_url;

    }

    public int getCartbadge() {
        return cartbadge;
    }

    public void setCartbadge(int cartbadge) {
        this.cartbadge = cartbadge;
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
