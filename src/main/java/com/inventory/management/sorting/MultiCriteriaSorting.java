
package com.inventory.management.sorting;

import com.inventory.management.model.Product;

import java.util.Comparator;
import java.util.List;

/**
 * Extends sorting utility to support multi-criteria sorting.
 */
public class MultiCriteriaSorting {

    /**
     * Sorts the product list by Category first, and then by Price within each Category.
     *
     * @param products the list of products to sort
     */
    public static void sortByCategoryAndPrice(List<Product> products) {
        products.sort(Comparator
                .comparing(Product::getCategory, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Comparator.comparingDouble(Product::getPrice).reversed()));
    }
}
