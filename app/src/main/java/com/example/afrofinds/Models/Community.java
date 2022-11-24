package com.example.afrofinds.Models;

public class Community {
    String country, img, embassy, id;
    public Community() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEmbassy() {
        return embassy;
    }

    public void setEmbassy(String embassy) {
        this.embassy = embassy;
    }
}
