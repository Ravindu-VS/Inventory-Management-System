package com.inventory.management.recursive;

import com.inventory.management.model.Product;
import java.util.List;

public class CalculationUtility {

    public static int countProductsInCategory(List<Product> products, String category) {
        return RecursiveOperations.countProductsInCategory(products, category, 0);
    }

    public static double calculateTotalInventoryValue(List<Product> products) {
        return RecursiveOperations.calculateTotalValue(products, 0);
    }
}
