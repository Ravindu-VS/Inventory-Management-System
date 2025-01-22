package com.inventory.management.sorting;

import com.inventory.management.model.Product;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort {
    public static void sort(List<Product> products, Comparator<Product> comparator) {
        if (products == null || products.size() <= 1) return;
        mergeSort(products, 0, products.size() - 1, comparator);
    }

    private static void mergeSort(List<Product> products, int left, int right, Comparator<Product> comparator) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSort(products, left, mid, comparator);
        mergeSort(products, mid + 1, right, comparator);
        merge(products, left, mid, right, comparator);
    }

    private static void merge(List<Product> products, int left, int mid, int right, Comparator<Product> comparator) {
        List<Product> leftList = new ArrayList<>(products.subList(left, mid + 1));
        List<Product> rightList = new ArrayList<>(products.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < leftList.size() && j < rightList.size()) {
            if (comparator.compare(leftList.get(i), rightList.get(j)) <= 0) {
                products.set(k++, leftList.get(i++));
            } else {
                products.set(k++, rightList.get(j++));
            }
        }

        while (i < leftList.size()) {
            products.set(k++, leftList.get(i++));
        }

        while (j < rightList.size()) {
            products.set(k++, rightList.get(j++));
        }
    }
}
