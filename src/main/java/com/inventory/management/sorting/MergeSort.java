package com.inventory.management.sorting;

import com.inventory.management.model.Product;

import java.util.List;

public class MergeSort {

    public static void sortByName(List<Product> products) {
        if (products.size() < 2) return;
        int mid = products.size() / 2;

        List<Product> left = products.subList(0, mid);
        List<Product> right = products.subList(mid, products.size());

        sortByName(left);
        sortByName(right);

        merge(products, left, right);
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
}
