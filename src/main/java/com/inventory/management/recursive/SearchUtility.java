package com.inventory.management.recursive;

import com.inventory.management.model.Product;
import java.util.List;

public class SearchUtility {

    public static int findProductById(List<Product> products, String targetId) {
        products.sort((p1, p2) -> p1.getProductId().compareToIgnoreCase(p2.getProductId()));
        return RecursiveOperations.binarySearch(products, targetId, 0, products.size() - 1);
    }
}
