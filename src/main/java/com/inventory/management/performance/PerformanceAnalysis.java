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

    /**
     * Compares the performance of Merge Sort, Quick Sort, and Shell Sort
     * when sorting by Product ID in ascending order.
     *
     * @param products the original list of products
     */
    public static void compareSortingAlgorithms(List<Product> products) {
        // Generate a large dataset for performance analysis
        List<Product> largeDataset = generateLargeDataset(products, 10000); // Adjust size as needed

        // Clone the product list to ensure each sort operates on identical data
        List<Product> mergeSortProducts = new ArrayList<>(largeDataset);
        List<Product> quickSortProducts = new ArrayList<>(largeDataset);
        List<Product> shellSortProducts = new ArrayList<>(largeDataset);

        // Define the comparator for sorting by Product ID
        Comparator<Product> productIdComparator = Comparator.comparing(Product::getProductId, String.CASE_INSENSITIVE_ORDER);

        // Measure Merge Sort
        long startTime = System.nanoTime();
        MergeSort.sort(mergeSortProducts, productIdComparator);
        long endTime = System.nanoTime();
        long mergeSortTime = endTime - startTime;
        System.out.println("Merge Sort Time: " + (mergeSortTime / 1_000_000) + " ms");

        // Measure Quick Sort
        startTime = System.nanoTime();
        QuickSort.sort(quickSortProducts, 0, quickSortProducts.size() - 1, productIdComparator);
        endTime = System.nanoTime();
        long quickSortTime = endTime - startTime;
        System.out.println("Quick Sort Time: " + (quickSortTime / 1_000_000) + " ms");

        // Measure Shell Sort
        startTime = System.nanoTime();
        ShellSort.sort(shellSortProducts, productIdComparator);
        endTime = System.nanoTime();
        long shellSortTime = endTime - startTime;
        System.out.println("Shell Sort Time: " + (shellSortTime / 1_000_000) + " ms");
    }

    /**
     * Generates a large dataset by duplicating and modifying the original products.
     *
     * @param originalProducts the original list of products
     * @param size             the desired size of the large dataset
     * @return a new list containing the large dataset
     */
    private static List<Product> generateLargeDataset(List<Product> originalProducts, int size) {
        List<Product> largeDataset = new ArrayList<>();
        int originalSize = originalProducts.size();
        for (int i = 0; i < size; i++) {
            Product original = originalProducts.get(i % originalSize);
            // Create a new Product with a unique ID and current date as dateAdded
            Product clone = new Product(
                    "P" + String.format("%05d", i + 1000), // Ensures unique Product IDs
                    original.getName() + " " + (i + 1),
                    original.getPrice(),
                    original.getQuantity(),
                    original.getCategory(),
                    original.getImagePath(), // Assuming this is a valid image path
                    LocalDate.now() // Sets dateAdded to current date
            );
            largeDataset.add(clone);
        }
        return largeDataset;
    }
}
