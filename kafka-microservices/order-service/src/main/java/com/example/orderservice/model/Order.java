package com.example.orderservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {
    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("productId")
    private String productId;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("customerEmail")
    private String customerEmail;

    public Order() {}

    public Order(String orderId, String productId, int quantity, String customerEmail) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.customerEmail = customerEmail;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", customerEmail='" + customerEmail + '\'' +
                '}';
    }
}