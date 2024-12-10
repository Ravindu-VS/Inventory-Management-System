package com.inventory.management.performance;

import com.inventory.management.model.Product;
import com.inventory.management.sorting.MergeSort;
import com.inventory.management.sorting.QuickSort;
import com.inventory.management.sorting.ShellSort;

import java.util.List;

public class PerformanceAnalysis {

    public static void compareSortingAlgorithms(List<Product> products) {
        long startTime, endTime;

        // Clone the product list to avoid sorting the same list multiple times
        List<Product> quickSortProducts = List.copyOf(products);
        List<Product> mergeSortProducts = List.copyOf(products);
        List<Product> shellSortProducts = List.copyOf(products);

        // Measure Quick Sort
        startTime = System.nanoTime();
        QuickSort.sortByPriceDesc(quickSortProducts, 0, quickSortProducts.size() - 1);
        endTime = System.nanoTime();
        System.out.println("Quick Sort Time: " + (endTime - startTime) + " ns");

        // Measure Merge Sort
        startTime = System.nanoTime();
        MergeSort.sortByName(mergeSortProducts);
        endTime = System.nanoTime();
        System.out.println("Merge Sort Time: " + (endTime - startTime) + " ns");

        // Measure Shell Sort
        startTime = System.nanoTime();
        ShellSort.sortByQuantity(shellSortProducts);
        endTime = System.nanoTime();
        System.out.println("Shell Sort Time: " + (endTime - startTime) + " ns");
    }
}
