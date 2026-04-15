package com.example.ltdd.model;

public class InventoryReport {
    private String productName;
    private int openingStock;
    private int importQty;
    private int exportQty;
    private int closingStock;

    public InventoryReport(String productName, int openingStock, int importQty, int exportQty, int closingStock) {
        this.productName = productName;
        this.openingStock = openingStock;
        this.importQty = importQty;
        this.exportQty = exportQty;
        this.closingStock = closingStock;
    }

    public String getProductName() { return productName; }
    public int getOpeningStock() { return openingStock; }
    public int getImportQty() { return importQty; }
    public int getExportQty() { return exportQty; }
    public int getClosingStock() { return closingStock; }
}
