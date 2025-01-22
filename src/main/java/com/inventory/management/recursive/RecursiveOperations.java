package com.inventory.management.recursive;

import com.inventory.management.model.Product;
import java.util.List;

public class RecursiveOperations {

    public static int binarySearch(List<Product> products, String targetId, int left, int right) {
        if (left > right) {
            return -1;
        }
        int mid = left + (right - left) / 2;
        Product midProduct = products.get(mid);
        int comparison = midProduct.getProductId().compareToIgnoreCase(targetId);

        if (comparison == 0) {
            return mid;
        } else if (comparison < 0) {
            return binarySearch(products, targetId, mid + 1, right);
        } else {
            return binarySearch(products, targetId, left, mid - 1);
        }
    }

    public static int countProductsInCategory(List<Product> products, String category, int index) {
        if (index >= products.size()) {
            return 0;
        }
        int count = products.get(index).getCategory().equalsIgnoreCase(category) ? 1 : 0;
        return count + countProductsInCategory(products, category, index + 1);
    }

    public static double calculateTotalValue(List<Product> products, int index) {
        if (index >= products.size()) {
            return 0.0;
        }
        double value = products.get(index).getPrice() * products.get(index).getQuantity();
        return value + calculateTotalValue(products, index + 1);
    }
}
