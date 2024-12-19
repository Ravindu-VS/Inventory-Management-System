package com.inventory.management.sorting;

import com.inventory.management.model.Product;

import java.util.Comparator;
import java.util.List;
import java.util.Collections;

/**
 * Implements Quick Sort algorithm for sorting products.
 */
public class QuickSort {

    /**
     * Sorts the list of products using the provided comparator.
     *
     * @param products   the list of products to sort
     * @param low        the starting index
     * @param high       the ending index
     * @param comparator the comparator defining the sort order
     */
    public static void sort(List<Product> products, int low, int high, Comparator<Product> comparator) {
        if (low < high) {
            int pi = partition(products, low, high, comparator);
            sort(products, low, pi - 1, comparator);
            sort(products, pi + 1, high, comparator);
        }
    }

    private static int partition(List<Product> products, int low, int high, Comparator<Product> comparator) {
        Product pivot = products.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (comparator.compare(products.get(j), pivot) <= 0) {
                i++;
                Collections.swap(products, i, j);
            }
        }
        Collections.swap(products, i + 1, high);
        return i + 1;
    }
}
