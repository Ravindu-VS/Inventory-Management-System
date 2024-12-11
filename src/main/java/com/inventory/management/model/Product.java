package com.inventory.management.model;

public class Product {
    private String productId;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private String imagePath; // Added field for image path

    public Product(String productId, String name, double price, int quantity, String category, String imagePath) {
        if (productId == null || productId.isEmpty()) throw new IllegalArgumentException("Invalid Product ID");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Invalid Product Name");
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        if (category == null || category.isEmpty()) throw new IllegalArgumentException("Invalid Category");

        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.imagePath = imagePath;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Invalid price");
        this.price = price;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Invalid quantity");
        this.quantity = quantity;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
