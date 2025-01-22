package com.inventory.management.utils;

import com.inventory.management.model.Product;
import java.util.List;
import java.util.stream.Collectors;

public class FilteringUtility {

    public static List<Product> filterByPriceRange(List<Product> products, double minPrice, double maxPrice) {
        return products.stream()
                .filter(product -> product.getPrice() >= minPrice && product.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public static List<Product> filterByCategory(List<Product> products, String category) {
        return products.stream()
                .filter(product -> product.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public static List<Product> filterByQuantity(List<Product> products, int minQuantity) {
        return products.stream()
                .filter(product -> product.getQuantity() >= minQuantity)
                .collect(Collectors.toList());
    }
}
