package com.example.eren.myapplication.Models;

/**
 * Created by oguzh on 4 May 2019.
 */

public class OrderDetail {
    String id;
    String products;
    String created_at;
    String quantity;
    String amount;
    String productImage;

    public OrderDetail(String id, String products, String created_at, String quantity, String amount, String productImage) {
        this.id = id;
        this.products = products;
        this.created_at = created_at;
        this.quantity = quantity;
        this.amount = amount;
        this.productImage=productImage;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
