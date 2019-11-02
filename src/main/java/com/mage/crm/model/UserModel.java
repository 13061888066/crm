package com.mage.crm.model;

public class UserModel {

    private String id;
    private String  userName;
    private String trueName;

    public UserModel(String id, String userName, String trueName) {
        this.id = id;
        this.userName = userName;
        this.trueName = trueName;
    }

    public UserModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

}
