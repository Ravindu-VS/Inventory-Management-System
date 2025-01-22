package com.inventory.management.sorting;

import com.inventory.management.model.Product;
import java.util.Comparator;
import java.util.List;

public class MultiCriteriaSorting {
    public static void sortByCategoryAndPrice(List<Product> products) {
        products.sort(Comparator
                .comparing(Product::getCategory, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Comparator.comparingDouble(Product::getPrice).reversed()));
    }
}
