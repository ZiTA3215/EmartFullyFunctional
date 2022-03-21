package com.oldrich.ecommerce.models;

public class ShippingModel {


String payment_id;
int price;
String currentDate;
String currentTime;
String address;
    String documentId;
    String shippingname;
    String Tracking;
    String TrackingTitle;

    String shippingurl;
    String shippinglogourl;

    public String getTrackingTitle() {
        return TrackingTitle;
    }

    public void setTrackingTitle(String trackingTitle) {
        TrackingTitle = trackingTitle;
    }

    public String getShippingname() {
        return shippingname;
    }

    public void setShippingname(String shippingname) {
        this.shippingname = shippingname;
    }

    public String getTracking() {
        return Tracking;
    }

    public void setTracking(String tracking) {
        Tracking = tracking;
    }

    public String getShippingurl() {
        return shippingurl;
    }

    public void setShippingurl(String shippingurl) {
        this.shippingurl = shippingurl;
    }

    public String getShippinglogourl() {
        return shippinglogourl;
    }

    public void setShippinglogourl(String shippinglogourl) {
        this.shippinglogourl = shippinglogourl;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public ShippingModel() {
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

