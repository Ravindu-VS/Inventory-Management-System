package com.inventory.management.sorting;

import com.inventory.management.model.Product;

import java.util.List;

/**
 * Utility class for sorting products using different algorithms.
 */
public class SortingUtility {

    /**
     * Sorts products by price in descending order using Quick Sort.
     *
     * @param products the list of products to sort
     */
    public static void sortByPriceDesc(List<Product> products) {
        SortingAlgorithms.quickSortByPriceDesc(products, 0, products.size() - 1);
    }

    /**
     * Sorts products by name in alphabetical order using Merge Sort.
     *
     * @param products the list of products to sort
     */
    public static void sortByNameAsc(List<Product> products) {
        SortingAlgorithms.mergeSortByName(products);
    }

    /**
     * Sorts products by quantity in ascending order using Shell Sort.
     *
     * @param products the list of products to sort
     */
    public static void sortByQuantityAsc(List<Product> products) {
        SortingAlgorithms.shellSortByQuantityAsc(products);
    }

    /**
     * Sorts products by quantity in descending order using modified Shell Sort.
     *
     * @param products the list of products to sort
     */
    public static void sortByQuantityDesc(List<Product> products) {
        // Implement a modified Shell Sort for descending order
        int n = products.size();

        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                Product temp = products.get(i);
                int j = i;
                while (j >= gap && products.get(j - gap).getQuantity() < temp.getQuantity()) {
                    products.set(j, products.get(j - gap));
                    j -= gap;
                }
                products.set(j, temp);
            }
        }
    }
}
