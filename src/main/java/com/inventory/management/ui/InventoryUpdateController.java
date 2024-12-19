package com.inventory.management.ui;

import com.inventory.management.model.Product;
import javafx.collections.ObservableList;

/**
 * Controller for updating inventory products.
 */
public class InventoryUpdateController {

    /**
     * Updates a product in the inventory after validating the product details.
     *
     * @param inventory      the observable list of products
     * @param updatedProduct the product with updated details
     * @throws IllegalArgumentException if any product field is invalid
     */
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
    /**
     * Validates the product details.
     *
     * @param product the product to validate
     * @throws IllegalArgumentException if any product field is invalid
     */
    private static void validateProduct(Product product) {
        if (product.getProductId() == null || product.getProductId().trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be empty.");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Product category cannot be empty.");
        }
        // Additional validations can be added as needed
    }

}
