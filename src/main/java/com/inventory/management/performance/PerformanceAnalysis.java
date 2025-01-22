package com.inventory.management.performance;

import com.inventory.management.model.Product;
import com.inventory.management.sorting.MergeSort;
import com.inventory.management.sorting.QuickSort;
import com.inventory.management.sorting.ShellSort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PerformanceAnalysis {

    public static void compareSortingAlgorithms(List<Product> products) {
        List<Product> largeDataset = generateLargeDataset(products, 10000);

        List<Product> mergeSortProducts = new ArrayList<>(largeDataset);
        List<Product> quickSortProducts = new ArrayList<>(largeDataset);
        List<Product> shellSortProducts = new ArrayList<>(largeDataset);

        Comparator<Product> productIdComparator = Comparator.comparing(Product::getProductId, String.CASE_INSENSITIVE_ORDER);

        long startTime = System.nanoTime();
        MergeSort.sort(mergeSortProducts, productIdComparator);
        long endTime = System.nanoTime();
        long mergeSortTime = endTime - startTime;
        System.out.println("Merge Sort Time: " + (mergeSortTime / 1_000_000) + " ms");

        startTime = System.nanoTime();
        QuickSort.sort(quickSortProducts, 0, quickSortProducts.size() - 1, productIdComparator);
        endTime = System.nanoTime();
        long quickSortTime = endTime - startTime;
        System.out.println("Quick Sort Time: " + (quickSortTime / 1_000_000) + " ms");

        startTime = System.nanoTime();
        ShellSort.sort(shellSortProducts, productIdComparator);
        endTime = System.nanoTime();
        long shellSortTime = endTime - startTime;
        System.out.println("Shell Sort Time: " + (shellSortTime / 1_000_000) + " ms");
    }

    private static List<Product> generateLargeDataset(List<Product> originalProducts, int size) {
        List<Product> largeDataset = new ArrayList<>();
        int originalSize = originalProducts.size();
        for (int i = 0; i < size; i++) {
            Product original = originalProducts.get(i % originalSize);
            Product clone = new Product(
                    "P" + String.format("%05d", i + 1000),
                    original.getName() + " " + (i + 1),
                    original.getPrice(),
                    original.getQuantity(),
                    original.getCategory(),
                    original.getImagePath(),
                    LocalDate.now()
            );
            largeDataset.add(clone);
        }
        return largeDataset;
    }
}
