package com.example.eren.myapplication.Models;

public class Products {
    String id;
    String name;
    String productImage;
    String description;

    public Products(String id,String name, String productImage,String description) {
        this.name = name;
        this.productImage = productImage;
        this.id = id;
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProductImage() {
        return productImage;
    }



    public Products( ) {  }


}