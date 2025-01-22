package com.inventory.management.ui;

import com.inventory.management.model.Product;
import javafx.collections.ObservableList;

public class InventoryUpdateController {

    public static boolean updateProduct(ObservableList<Product> inventory, Product updatedProduct) {
        for (int i = 0; i < inventory.size(); i++) {
            Product existingProduct = inventory.get(i);
            if (existingProduct.getProductId().equalsIgnoreCase(updatedProduct.getProductId())) {
                inventory.set(i, updatedProduct);
                return true;
            }
        }
        return false;
    }
}
