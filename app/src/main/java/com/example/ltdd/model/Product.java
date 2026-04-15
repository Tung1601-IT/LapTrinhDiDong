package com.example.ltdd.model;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String productLine;
    private String model;
    private String imei;
    private String ram;
    private String rom;
    private String screenSize;
    private String screenQuality;
    private String processor;
    private String pin;
    private String color;
    private int quantity;
    private double price;
    private double oldPrice; // Thêm giá cũ
    private String imagePath;

    public Product() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getProductLine() { return productLine; }
    public void setProductLine(String productLine) { this.productLine = productLine; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getImei() { return imei; }
    public void setImei(String imei) { this.imei = imei; }
    public String getRam() { return ram; }
    public void setRam(String ram) { this.ram = ram; }
    public String getRom() { return rom; }
    public void setRom(String rom) { this.rom = rom; }
    public String getScreenSize() { return screenSize; }
    public void setScreenSize(String screenSize) { this.screenSize = screenSize; }
    public String getScreenQuality() { return screenQuality; }
    public void setScreenQuality(String screenQuality) { this.screenQuality = screenQuality; }
    public String getProcessor() { return processor; }
    public void setProcessor(String processor) { this.processor = processor; }
    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getOldPrice() { 
        if (oldPrice <= price) return price * 1.1; // Giả định giá cũ cao hơn 10% nếu chưa có
        return oldPrice; 
    }
    public void setOldPrice(double oldPrice) { this.oldPrice = oldPrice; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}