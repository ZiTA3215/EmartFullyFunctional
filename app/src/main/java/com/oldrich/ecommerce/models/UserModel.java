package com.oldrich.ecommerce.models;

public class UserModel {
    String useremail;
    String username;
    String userpassword;
    String ProfileImg;

    public UserModel() {
    }

    public UserModel(String username, String useremail, String userpasswrord) {
        this.useremail = useremail;
        this.username = username;
        this.userpassword = userpasswrord;


    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getProfileImg() {
        return ProfileImg;
    }

    public void setProfileImg(String profileImg) {
        ProfileImg = profileImg;
    }



    }



