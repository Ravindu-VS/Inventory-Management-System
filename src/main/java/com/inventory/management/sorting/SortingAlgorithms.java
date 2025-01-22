package com.inventory.management.sorting;

import com.inventory.management.model.Product;
import java.util.List;

public class SortingAlgorithms {
    public static void quickSortByPriceDesc(List<Product> products, int low, int high) {
        if (low < high) {
            int pi = partition(products, low, high);
            quickSortByPriceDesc(products, low, pi - 1);
            quickSortByPriceDesc(products, pi + 1, high);
        }
    }

    private static int partition(List<Product> products, int low, int high) {
        double pivot = products.get(high).getPrice();
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (products.get(j).getPrice() > pivot) {
                i++;
                swap(products, i, j);
            }
        }
        swap(products, i + 1, high);
        return i + 1;
    }

    public static void mergeSortByName(List<Product> products) {
        if (products.size() > 1) {
            int mid = products.size() / 2;
            List<Product> left = new java.util.ArrayList<>(products.subList(0, mid));
            List<Product> right = new java.util.ArrayList<>(products.subList(mid, products.size()));

            mergeSortByName(left);
            mergeSortByName(right);

            merge(products, left, right);
        }
    }

    private static void merge(List<Product> products, List<Product> left, List<Product> right) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i).getName().compareToIgnoreCase(right.get(j).getName()) <= 0) {
                products.set(k++, left.get(i++));
            } else {
                products.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) {
            products.set(k++, left.get(i++));
        }
        while (j < right.size()) {
            products.set(k++, right.get(j++));
        }
    }

    public static void shellSortByQuantityAsc(List<Product> products) {
        int n = products.size();
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                Product temp = products.get(i);
                int j = i;
                while (j >= gap && products.get(j - gap).getQuantity() > temp.getQuantity()) {
                    products.set(j, products.get(j - gap));
                    j -= gap;
                }
                products.set(j, temp);
            }
        }
    }

    private static void swap(List<Product> products, int i, int j) {
        Product temp = products.get(i);
        products.set(i, products.get(j));
        products.set(j, temp);
    }
}
