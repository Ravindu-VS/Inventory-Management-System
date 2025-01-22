package com.inventory.management.sorting;

import com.inventory.management.model.Product;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuickSort {
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
