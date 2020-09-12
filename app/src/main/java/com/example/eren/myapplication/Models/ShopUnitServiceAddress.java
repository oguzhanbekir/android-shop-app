package com.example.eren.myapplication.Models;

public class ShopUnitServiceAddress {
    String _id;
    String shopUnit;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ShopUnitServiceAddress(String _id, String shopUnit) {
        this._id = _id;
        this.shopUnit = shopUnit;
    }

    public String getShopUnit() {
        return shopUnit;
    }

    public void setShopUnit(String shopUnit) {
        this.shopUnit = shopUnit;
    }
}
