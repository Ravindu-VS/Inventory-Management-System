package com.inventory.management.ui;

import com.inventory.management.model.Product;
import javafx.collections.ObservableList;

public class InventoryUpdateController {

    public static void updateProduct(ObservableList<Product> inventory, Product updatedProduct) {
        inventory.removeIf(p -> p.getProductId().equals(updatedProduct.getProductId())); // Fixed method name
        inventory.add(updatedProduct);
    }
}
