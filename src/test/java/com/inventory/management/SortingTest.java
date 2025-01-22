package com.inventory.management;

import com.inventory.management.model.Product;
import com.inventory.management.sorting.SortingUtility;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SortingTest {
    @Test
    public void testSortByPriceDesc() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("P1", "A", 10, 1, "Cat", "", LocalDate.now()));
        products.add(new Product("P2", "B", 20, 1, "Cat", "", LocalDate.now()));
        SortingUtility.sortByPriceDesc(products);
        assertTrue(products.get(0).getPrice() >= products.get(1).getPrice());
    }
}
