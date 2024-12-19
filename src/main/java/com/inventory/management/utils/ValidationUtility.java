
package com.inventory.management.utils;

import com.inventory.management.model.Product;

/**
 * Provides validation utilities for input and data consistency.
 */
public class ValidationUtility {

    /**
     * Validates a product's details.
     *
     * @param product the product to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateProduct(Product product) {
        if (product.getProductId() == null || product.getProductId().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be empty.");
        }
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("Product Name cannot be empty.");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Product Price cannot be negative.");
        }
        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException("Product Quantity cannot be negative.");
        }
        if (product.getCategory() == null || product.getCategory().isEmpty()) {
            throw new IllegalArgumentException("Product Category cannot be empty.");
        }
    }

    /**
     * Validates a search input.
     *
     * @param input the input string to validate
     * @throws IllegalArgumentException if the input is null or empty
     */
    public static void validateSearchInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Search input cannot be empty.");
        }
    }
}
