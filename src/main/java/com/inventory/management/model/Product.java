package com.inventory.management.model;

import java.time.LocalDate;
import java.util.Objects;

public class Product {
    private String productId;
    private String name;
    private double price;
    private int quantity;
    private String category;
    private String imagePath;
    private LocalDate dateAdded;

    // Constructor with all required parameters
    public Product(String productId, String name, double price, int quantity, String category, String imagePath, LocalDate dateAdded) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.imagePath = imagePath;
        this.dateAdded = dateAdded;
    }

    // Getters and Setters

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    // Similarly, implement getters and setters for other fields

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    // toString method for debugging and display purposes
    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", dateAdded=" + dateAdded +
                '}';
    }

    // Override equals() and hashCode() if necessary for proper comparisons and collections handling
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return Objects.equals(productId.toLowerCase(), product.productId.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId.toLowerCase());
    }
}
