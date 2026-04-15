package com.example.ltdd.model;

import java.io.Serializable;

public class RepairOrder implements Serializable {
    private int id;
    private String customerName;
    private String phone;
    private String device;
    private String issueDescription;
    private String receivedDate;
    private String status;
    private String completedDate;

    public RepairOrder() {
    }

    public RepairOrder(int id, String customerName, String phone, String device,
                       String issueDescription, String receivedDate,
                       String status, String completedDate) {
        this.id = id;
        this.customerName = customerName;
        this.phone = phone;
        this.device = device;
        this.issueDescription = issueDescription;
        this.receivedDate = receivedDate;
        this.status = status;
        this.completedDate = completedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }
}
