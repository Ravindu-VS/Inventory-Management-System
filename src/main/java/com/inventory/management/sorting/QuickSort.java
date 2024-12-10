package com.inventory.management.sorting;

import com.inventory.management.model.Product;

import java.util.List;

public class QuickSort {

    public static void sortByPriceDesc(List<Product> products, int low, int high) {
        if (low < high) {
            int pi = partition(products, low, high);
            sortByPriceDesc(products, low, pi - 1);
            sortByPriceDesc(products, pi + 1, high);
        }
    }

    private static int partition(List<Product> products, int low, int high) {
        double pivot = products.get(high).getPrice();
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (products.get(j).getPrice() > pivot) { // Descending order
                i++;
                swap(products, i, j);
            }
        }
        swap(products, i + 1, high);
        return i + 1;
    }

    private static void swap(List<Product> products, int i, int j) {
        Product temp = products.get(i);
        products.set(i, products.get(j));
        products.set(j, temp);
    }
}
