package com.example.ltdd.model;

public class Invoice {
    private String id;
    private String customerName;
    private String customerPhone;
    private String productName;
    private String productIMEI;
    private String amount;
    private String date;

    public Invoice(String id, String customerName, String customerPhone, String productName, String productIMEI, String amount, String date) {
        this.id = id;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.productName = productName;
        this.productIMEI = productIMEI;
        this.amount = amount;
        this.date = date;
    }

    public String getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public String getProductName() { return productName; }
    public String getProductIMEI() { return productIMEI; }
    public String getAmount() { return amount; }
    public String getDate() { return date; }
}
