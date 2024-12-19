package com.inventory.management.recursive;

import com.inventory.management.model.Product;

import java.util.List;

/**
 * Utility class for search operations.
 */
public class SearchUtility {

    /**
     * Finds the index of a product by its Product ID using recursive binary search.
     * Note: The list must be sorted by Product ID before performing binary search.
     *
     * @param products the sorted list of products
     * @param targetId the Product ID to search for
     * @return the index of the product if found; -1 otherwise
     */
    public static int findProductById(List<Product> products, String targetId) {
        // First, sort the list by Product ID to ensure binary search works correctly
        products.sort((p1, p2) -> p1.getProductId().compareToIgnoreCase(p2.getProductId()));
        return RecursiveOperations.binarySearch(products, targetId, 0, products.size() - 1);
    }
}
