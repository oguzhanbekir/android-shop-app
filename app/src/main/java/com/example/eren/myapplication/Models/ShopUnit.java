package com.example.eren.myapplication.Models;

public class ShopUnit {
    String _id;
    String name;
    String shop;
    Integer min_order;
    String phone;
    String email;
    Boolean status;

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

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public ShopUnit() {
    }

    public Integer getMin_order() {
        return min_order;
    }

    public void setMin_order(Integer min_order) {
        this.min_order = min_order;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ShopUnit(String _id, String name, String shop, Integer min_order, String phone, String email, Boolean status) {
        this._id = _id;
        this.name = name;
        this.shop = shop;
        this.min_order = min_order;
        this.phone = phone;
        this.email = email;
        this.status = status;
    }
}
