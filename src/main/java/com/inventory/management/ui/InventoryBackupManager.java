package com.inventory.management.performance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inventory.management.model.Product;

import java.io.*;
import java.util.List;

/**
 * Manages backup and restoration of inventory data.
 */
public class InventoryBackupManager {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Backs up the inventory to a specified JSON file.
     *
     * @param inventory the list of products to back up
     * @param filePath  the file path where the backup will be saved
     * @throws IOException if an I/O error occurs
     */
    public static void backupInventory(List<Product> inventory, String filePath) throws IOException {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(inventory, writer);
        }
    }

    /**
     * Restores the inventory from a specified JSON file.
     *
     * @param filePath the file path from where the backup will be restored
     * @return the list of products restored from the backup
     * @throws IOException if an I/O error occurs
     */
    public static List<Product> restoreInventory(String filePath) throws IOException {
        try (Reader reader = new FileReader(filePath)) {
            Product[] productsArray = gson.fromJson(reader, Product[].class);
            return List.of(productsArray);
        }
    }
}
