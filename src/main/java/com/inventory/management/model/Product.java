package com.inventory.management.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Product {
    private final StringProperty productId;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty quantity;
    private final StringProperty category;
    private final StringProperty imagePath;
    private final ObjectProperty<LocalDate> dateAdded;

    public Product(String productId, String name, double price, int quantity, String category, String imagePath, LocalDate dateAdded) {
        this.productId = new SimpleStringProperty(productId);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.category = new SimpleStringProperty(category);
        this.imagePath = new SimpleStringProperty(imagePath);
        this.dateAdded = new SimpleObjectProperty<>(dateAdded);
    }

    // Getters and Property Methods

    public String getProductId() {
        return productId.get();
    }

    public StringProperty productIdProperty() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId.set(productId);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getImagePath() {
        return imagePath.get();
    }

    public StringProperty imagePathProperty() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath.set(imagePath);
    }

    public LocalDate getDateAdded() {
        return dateAdded.get();
    }

    public ObjectProperty<LocalDate> dateAddedProperty() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded.set(dateAdded);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + getProductId() + '\'' +
                ", name='" + getName() + '\'' +
                ", price=" + getPrice() +
                ", quantity=" + getQuantity() +
                ", category='" + getCategory() + '\'' +
                ", imagePath='" + getImagePath() + '\'' +
                ", dateAdded=" + getDateAdded() +
                '}';
    }
}
