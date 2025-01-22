package com.inventory.management;

import com.inventory.management.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class ProductTest {
    @Test
    public void testProductCreation() {
        Product p = new Product("P001", "Apple", 2.5, 100, "Fruits", "/images/apple.png", LocalDate.now());
        Assertions.assertEquals("P001", p.getProductId());
        Assertions.assertEquals("Apple", p.getName());
    }
}
