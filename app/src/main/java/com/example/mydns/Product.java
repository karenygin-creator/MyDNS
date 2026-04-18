package com.example.mydns;

public class Product {
    private String name;
    private String price;
    private String description;
    private int image;

    public Product(String name, String price,String description, int image) {
        this.name = name;
        this.price = price;
        this.description=description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}
