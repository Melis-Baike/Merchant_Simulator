package com.example.merchantsimulator.enums;

public enum Products {
    CARROT("Carrot", 50),
    TOMATO("Tomato", 40),
    POTATO("Potato", 30),
    BEET("Beet",60),
    APPLE("Apple", 20);
    private final String name;
    private final int price;

    Products(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
