package com.example.eren.myapplication.Models;

public class PaymentModel {
    String product;
    String quantity;
    String amount;
    String stock;
    String stockId;

    public String getStock() {
        return stock;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public PaymentModel(String product, String quantity, String amount,String stock,String stockId) {
        this.product = product;
        this.quantity = quantity;
        this.amount = amount;
        this.stock=stock;
        this.stockId=stockId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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
