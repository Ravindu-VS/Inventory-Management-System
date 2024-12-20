package com.inventory.management.sorting;

import com.inventory.management.model.Product;
import java.util.List;

public class SortingUtility {
    public static void sortByPriceDesc(List<Product> products) {
        SortingAlgorithms.quickSortByPriceDesc(products, 0, products.size() - 1);
    }

    public static void sortByNameAsc(List<Product> products) {
        SortingAlgorithms.mergeSortByName(products);
    }

    public static void sortByQuantityAsc(List<Product> products) {
        SortingAlgorithms.shellSortByQuantityAsc(products);
    }

    public static void sortByQuantityDesc(List<Product> products) {
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
