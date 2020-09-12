package com.example.eren.myapplication.Models;

public class Shop {
    String _id;
    String name;
    String user;
    String logo;

    public Shop(String _id, String name, String user, String logo) {
        this._id = _id;
        this.name = name;
        this.user = user;
        this.logo = logo;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLogo() {

        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
