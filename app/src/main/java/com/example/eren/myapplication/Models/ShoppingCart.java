package com.example.eren.myapplication.Models;

public class ShoppingCart {
    String productId;
    String name;
    String imageURL;
    Integer quantity;
    String productDescription;

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ShoppingCart(String productId, String name, String imageURL, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.imageURL = imageURL;
        this.quantity = quantity;
    }
}
