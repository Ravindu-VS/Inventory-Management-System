package com.inventory.management.sorting;

import com.inventory.management.model.Product;
import java.util.Comparator;
import java.util.List;

public class ShellSort {
    public static void sort(List<Product> products, Comparator<Product> comparator) {
        if (products == null || products.size() <= 1) return;
        int n = products.size();
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                Product temp = products.get(i);
                int j;
                for (j = i; j >= gap && comparator.compare(products.get(j - gap), temp) > 0; j -= gap) {
                    products.set(j, products.get(j - gap));
                }
                products.set(j, temp);
            }
        }
    }
}
