package com.oldrich.ecommerce.models;

public class ImageSliderModel {
    int Image;

    public ImageSliderModel() {
    }

    public ImageSliderModel(int image) {
        Image = image;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}