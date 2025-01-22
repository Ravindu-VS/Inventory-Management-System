package com.inventory.management;

import com.inventory.management.model.Product;
import com.inventory.management.recursive.CalculationUtility;
import com.inventory.management.recursive.SearchUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecursiveTest {
    @Test
    public void testBinarySearch() {
        List<Product> products = List.of(
                new Product("P001", "Apple", 2.5, 100, "Fruits", "", LocalDate.now()),
                new Product("P002", "Banana",1.2,200,"Fruits","",LocalDate.now())
        );
        int index = SearchUtility.findProductById(new ArrayList<>(products), "P002");
        Assertions.assertEquals(1, index);
    }

    @Test
    public void testCountInCategory() {
        List<Product> products = List.of(
                new Product("P001","Apple",2.5,100,"Fruits","",LocalDate.now()),
                new Product("P002","Laptop",1000,10,"Electronics","",LocalDate.now())
        );
        int count = CalculationUtility.countProductsInCategory(products, "Fruits");
        Assertions.assertEquals(1, count);
    }
}

