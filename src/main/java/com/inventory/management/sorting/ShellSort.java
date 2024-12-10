package com.inventory.management.sorting;

import com.inventory.management.model.Product;

import java.util.List;

public class ShellSort {

    public static void sortByQuantity(List<Product> products) {
        int n = products.size();
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                Product temp = products.get(i);
                int j;
                for (j = i; j >= gap && products.get(j - gap).getQuantity() > temp.getQuantity(); j -= gap) {
                    products.set(j, products.get(j - gap));
                }
                products.set(j, temp);
            }
        }
    }
}
