package com.inventory.management.recursive;

import com.inventory.management.model.Product;

import java.util.List;

/**
 * Implements recursive operations for the inventory management system.
 */
public class RecursiveOperations {

    /**
     * Recursively performs a binary search to find the index of a product by its Product ID.
     *
     * @param products    the sorted list of products
     * @param targetId    the Product ID to search for
     * @param left        the starting index
     * @param right       the ending index
     * @return the index of the product if found; -1 otherwise
     */
    public static int binarySearch(List<Product> products, String targetId, int left, int right) {
        if (left > right) {
            return -1; // Base case: not found
        }

        int mid = left + (right - left) / 2;
        Product midProduct = products.get(mid);
        int comparison = midProduct.getProductId().compareToIgnoreCase(targetId);

        if (comparison == 0) {
            return mid; // Found
        } else if (comparison < 0) {
            return binarySearch(products, targetId, mid + 1, right); // Search in the right half
        } else {
            return binarySearch(products, targetId, left, mid - 1); // Search in the left half
        }
    }

    /**
     * Recursively counts the number of products in a specific category.
     *
     * @param products the list of products
     * @param category the category to count
     * @param index    the current index
     * @return the total number of products in the category
     */
    public static int countProductsInCategory(List<Product> products, String category, int index) {
        if (index >= products.size()) {
            return 0; // Base case: end of list
        }

        int count = products.get(index).getCategory().equalsIgnoreCase(category) ? 1 : 0;
        return count + countProductsInCategory(products, category, index + 1);
    }

    /**
     * Recursively calculates the total value of all products in the inventory.
     *
     * @param products the list of products
     * @param index    the current index
     * @return the total value of the inventory
     */
    public static double calculateTotalValue(List<Product> products, int index) {
        if (index >= products.size()) {
            return 0.0; // Base case: end of list
        }

        double value = products.get(index).getPrice() * products.get(index).getQuantity();
        return value + calculateTotalValue(products, index + 1);
    }
}
