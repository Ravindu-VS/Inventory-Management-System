package com.inventory.management.ui;

import com.inventory.management.model.Product;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Simulates real-time inventory updates using JavaFX property bindings.
 */
public class RealTimeProduct {

    private final String productId;
    private final String name;
    private final String category;

    private final DoubleProperty price;
    private final IntegerProperty quantity;

    public RealTimeProduct(String productId, String name, double price, int quantity, String category) {
        this.productId = productId;
        this.name = name;
        this.category = category;

        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    // Getters and property methods

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
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
}
