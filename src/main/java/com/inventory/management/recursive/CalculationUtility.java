package com.inventory.management.recursive;

import com.inventory.management.model.Product;

import java.util.List;

/**
 * Utility class for performing calculations on the inventory.
 */
public class CalculationUtility {

    /**
     * Counts the number of products in a specific category using recursion.
     *
     * @param products the list of products
     * @param category the category to count
     * @return the total number of products in the category
     */
    public static int countProductsInCategory(List<Product> products, String category) {
        return RecursiveOperations.countProductsInCategory(products, category, 0);
    }

    /**
     * Calculates the total value of all products in the inventory using recursion.
     *
     * @param products the list of products
     * @return the total value of the inventory
     */
    public static double calculateTotalInventoryValue(List<Product> products) {
        return RecursiveOperations.calculateTotalValue(products, 0);
    }
}
