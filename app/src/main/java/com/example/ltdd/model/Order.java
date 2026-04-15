package com.example.ltdd.model;

import java.io.Serializable;

public class Order implements Serializable {
    private String orderId;
    private String customerName;
    private String status;
    private String totalAmount;
    private String date;
    private int quantity;

    public Order(String orderId, String customerName, String status, String totalAmount, String date, int quantity) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.status = status;
        this.totalAmount = totalAmount;
        this.date = date;
        this.quantity = quantity;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getStatus() { return status; }
    public String getTotalAmount() { return totalAmount; }
    public String getDate() { return date; }
    public int getQuantity() { return quantity; }

    public void setStatus(String status) { this.status = status; }
}