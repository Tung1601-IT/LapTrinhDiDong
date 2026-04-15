package com.example.ltdd.model;

public class HomeItem {
    private String title;
    private int iconRes;
    private int colorRes;

    public HomeItem(String title, int iconRes, int colorRes) {
        this.title = title;
        this.iconRes = iconRes;
        this.colorRes = colorRes;
    }

    public String getTitle() { return title; }
    public int getIconRes() { return iconRes; }
    public int getColorRes() { return colorRes; }
}