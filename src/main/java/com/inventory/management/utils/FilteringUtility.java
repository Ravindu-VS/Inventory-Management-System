
package com.inventory.management.utils;

import com.inventory.management.model.Product;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides filtering utilities for inventory management.
 */
public class FilteringUtility {

    /**
     * Filters products within a specified price range.
     *
     * @param products the list of products to filter
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     * @return a filtered list of products within the price range
     */
    public static List<Product> filterByPriceRange(List<Product> products, double minPrice, double maxPrice) {
        return products.stream()
                .filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Filters products by a specific category.
     *
     * @param products the list of products to filter
     * @param category the category to filter by
     * @return a filtered list of products in the given category
     */
    public static List<Product> filterByCategory(List<Product> products, String category) {
        return products.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Filters products with a quantity above a certain threshold.
     *
     * @param products the list of products to filter
     * @param minQuantity the minimum quantity
     * @return a filtered list of products meeting the quantity threshold
     */
    public static List<Product> filterByQuantity(List<Product> products, int minQuantity) {
        return products.stream()
                .filter(product -> product.getQuantity() >= minQuantity)
                .collect(Collectors.toList());
    }
}
