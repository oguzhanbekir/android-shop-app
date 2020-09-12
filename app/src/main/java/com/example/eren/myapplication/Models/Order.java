package com.example.eren.myapplication.Models;

/**
 * Created by oguzh on 3 May 2019.
 */

public class Order {
    String id;
    String created_at;
    String shopUnit;
    int total_amount;
    String payment_type;
    String order_status;


    public Order(String id, String created_at, String shopUnit, int total_amount, String payment_type, String order_status) {
        this.id = id;
        this.created_at = created_at;
        this.shopUnit = shopUnit;
        this.total_amount = total_amount;
        this.payment_type = payment_type;
        this.order_status = order_status;
    }

    public String getId() {
        return id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getShopUnit() {
        return shopUnit;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setShopUnit(String shopUnit) {
        this.shopUnit = shopUnit;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
