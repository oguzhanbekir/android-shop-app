package com.example.eren.myapplication.Models;

public class ShopUnitStock {
    String _id;
    String shopUnit;
    String product;
    Integer stock;
    Long price;
    Boolean discount;
    Integer discount_rate;
    Boolean campaign;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getShopUnit() {
        return shopUnit;
    }

    public void setShopUnit(String shopUnit) {
        this.shopUnit = shopUnit;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Boolean getDiscount() {
        return discount;
    }

    public void setDiscount(Boolean discount) {
        this.discount = discount;
    }

    public Integer getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(Integer discount_rate) {
        this.discount_rate = discount_rate;
    }

    public Boolean getCampaign() {
        return campaign;
    }

    public void setCampaign(Boolean campaign) {
        this.campaign = campaign;
    }

    public ShopUnitStock(String _id, String shopUnit, String product, Integer stock, Long price, Boolean discount, Integer discount_rate, Boolean campaign) {
        this._id = _id;
        this.shopUnit = shopUnit;
        this.product = product;
        this.stock = stock;
        this.price = price;
        this.discount = discount;
        this.discount_rate = discount_rate;
        this.campaign = campaign;
    }
}
