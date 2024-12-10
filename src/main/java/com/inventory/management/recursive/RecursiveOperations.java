package com.inventory.management.recursive;

import com.inventory.management.model.Product;
import java.util.List;

public class RecursiveOperations {

    public static Product recursiveBinarySearch(Product[] arr, String targetId, int left, int right) {
        if (left > right) return null;
        int mid = (left + right) / 2;
        int cmp = arr[mid].getProductId().compareTo(targetId);
        if (cmp == 0) return arr[mid];
        else if (cmp < 0) return recursiveBinarySearch(arr, targetId, mid + 1, right);
        else return recursiveBinarySearch(arr, targetId, left, mid - 1);
    }

    public static int countProductsInCategory(List<Product> products, String category, int index) {
        if (index == products.size()) return 0;
        int count = products.get(index).getCategory().equalsIgnoreCase(category) ? 1 : 0;
        return count + countProductsInCategory(products, category, index + 1);
    }

    public static double totalValue(List<Product> products, int index) {
        if (index == products.size()) return 0;
        double value = products.get(index).getPrice() * products.get(index).getQuantity();
        return value + totalValue(products, index + 1);
    }
}
