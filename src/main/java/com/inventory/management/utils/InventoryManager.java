package com.inventory.management.utils;

import com.inventory.management.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventoryManager {

    private static ObservableList<Product> inventory = FXCollections.observableArrayList();

    public static ObservableList<Product> getInventory() {
        return inventory;
    }

    public static void addProduct(Product product) {
        inventory.add(product);
    }

    public static void removeProduct(Product product) {
        inventory.remove(product);
    }

    public static void updateProduct(Product oldProduct, Product newProduct) {
        int index = inventory.indexOf(oldProduct);
        if (index != -1) {
            inventory.set(index, newProduct);
        }
    }
}
