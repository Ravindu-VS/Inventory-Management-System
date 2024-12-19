package com.inventory.management.sorting;

import com.inventory.management.model.Product;

import java.util.List;

/**
 * Implements various sorting algorithms for products.
 */
public class SortingAlgorithms {

    /**
     * Quick Sort implementation to sort products by price in descending order.
     *
     * @param products the list of products to sort
     * @param low      the starting index
     * @param high     the ending index
     */
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
            if (products.get(j).getPrice() > pivot) { // Descending order
                i++;
                swap(products, i, j);
            }
        }

        swap(products, i + 1, high);
        return i + 1;
    }

    /**
     * Merge Sort implementation to sort products by name in alphabetical order.
     *
     * @param products the list of products to sort
     */
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

    /**
     * Shell Sort implementation to sort products by quantity in ascending order.
     *
     * @param products the list of products to sort
     */
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

    /**
     * Helper method to swap two products in the list.
     *
     * @param products the list of products
     * @param i        the first index
     * @param j        the second index
     */
    private static void swap(List<Product> products, int i, int j) {
        Product temp = products.get(i);
        products.set(i, products.get(j));
        products.set(j, temp);
    }
}
