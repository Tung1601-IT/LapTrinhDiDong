package com.example.ltdd.model;

public class Khachhang {
    private int customer_id;
    private String customer_name;
    private String customer_gender;
    private String customer_email;
    private String customer_contact_no;
    private String customer_address;
    private String created_at;

    public Khachhang() {
    }

    public Khachhang(int customer_id, String customer_name, String customer_gender, String customer_email, String customer_contact_no, String customer_address, String created_at) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_gender = customer_gender;
        this.customer_email = customer_email;
        this.customer_contact_no = customer_contact_no;
        this.customer_address = customer_address;
        this.created_at = created_at;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_gender() {
        return customer_gender;
    }

    public void setCustomer_gender(String customer_gender) {
        this.customer_gender = customer_gender;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_contact_no() {
        return customer_contact_no;
    }

    public void setCustomer_contact_no(String customer_contact_no) {
        this.customer_contact_no = customer_contact_no;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Khachhang{" +
                "customer_id=" + customer_id +
                ", customer_name='" + customer_name + '\'' +
                ", customer_gender='" + customer_gender + '\'' +
                ", customer_email='" + customer_email + '\'' +
                ", customer_contact_no='" + customer_contact_no + '\'' +
                ", customer_address='" + customer_address + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
