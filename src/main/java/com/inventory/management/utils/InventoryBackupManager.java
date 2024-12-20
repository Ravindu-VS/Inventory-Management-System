package com.inventory.management.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventory.management.model.Product;

import java.io.*;
import java.util.List;

public class InventoryBackupManager {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void backupInventory(List<Product> inventory, String filePath) throws IOException {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(inventory, writer);
        }
    }

    public static List<Product> restoreInventory(String filePath) throws IOException {
        try (Reader reader = new FileReader(filePath)) {
            Product[] productsArray = gson.fromJson(reader, Product[].class);
            return List.of(productsArray);
        }
    }
}
