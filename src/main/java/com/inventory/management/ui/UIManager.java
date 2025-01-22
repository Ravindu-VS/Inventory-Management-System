package com.inventory.management.ui;

import com.inventory.management.db.MongoDBClient;
import com.inventory.management.model.Product;
import com.inventory.management.model.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class UIManager extends Application {

    public static ObservableList<Product> inventory = FXCollections.observableArrayList();
    public static User currentUser = null;
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        MongoDBClient.connect();
        List<Product> dbProducts = MongoDBClient.loadProducts();
        inventory.setAll(dbProducts); // Set initial inventory from DB

        // Add sample products if inventory is empty (for initial setup)
        if (inventory.isEmpty()) {
            Product p1 = new Product("F001", "Apple", 3.00, 300, "Fruits", "/images/apple.png", LocalDate.now());
            Product p2 = new Product("F002", "Banana", 1.00, 450, "Fruits", "/images/banana.png", LocalDate.now());
            Product p3 = new Product("F003", "Mango", 5.00, 200, "Fruits", "/images/mango.png", LocalDate.now());

            Product p4 = new Product("E001", "Smartphone", 699.00, 50, "Electronics", "/images/smartphone.png", LocalDate.now());
            Product p5 = new Product("E002", "Laptop", 1200.00, 30, "Electronics", "/images/laptop.png", LocalDate.now());
            Product p6 = new Product("E003", "Bluetooth Speaker", 75.00, 150, "Electronics", "/images/speaker.png", LocalDate.now());
            Product p7 = new Product("E004", "Smartwatch", 250.00, 75, "Electronics", "/images/smartwatch.png", LocalDate.now());

            Product p8 = new Product("A001", "USB Cable", 10.00, 500, "Accessories", "/images/usb.png", LocalDate.now());
            Product p9 = new Product("A002", "Wireless Mouse", 25.00, 200, "Accessories", "/images/mouse.png", LocalDate.now());
            Product p10 = new Product("A003", "Headphones", 80.00, 100, "Accessories", "/images/headphones.png", LocalDate.now());
            Product p11 = new Product("A004", "Keyboard", 45.00, 150, "Accessories", "/images/keyboard.png", LocalDate.now());
            Product p12 = new Product("A005", "Power Bank", 50.00, 250, "Accessories", "/images/powerbank.png", LocalDate.now());

            Product p13 = new Product("B001", "Fiction Book", 15.00, 120, "Books", "/images/fictionbook.png", LocalDate.now());
            Product p14 = new Product("B002", "Science Textbook", 40.00, 50, "Books", "/images/sciencetextbook.png", LocalDate.now());

            Product p15 = new Product("C001", "T-Shirt", 20.00, 300, "Clothing", "/images/tshirt.png", LocalDate.now());
            Product p16 = new Product("C002", "Jeans", 40.00, 200, "Clothing", "/images/jeans.png", LocalDate.now());
            Product p17 = new Product("C003", "Jacket", 100.00, 50, "Clothing", "/images/jacket.png", LocalDate.now());
            Product p18 = new Product("C004", "Sneakers", 60.00, 150, "Clothing", "/images/sneakers.png", LocalDate.now());

            Product p19 = new Product("FD001", "Pasta", 3.00, 500, "Food", "/images/pasta.png", LocalDate.now());
            Product p20 = new Product("FD002", "Pizza Base", 8.00, 250, "Food", "/images/pizzabase.png", LocalDate.now());
            Product p21 = new Product("FD003", "Chips", 2.00, 1000, "Food", "/images/chips.png", LocalDate.now());

            inventory.addAll(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21);
            MongoDBClient.addOrUpdateProduct(p1);
            MongoDBClient.addOrUpdateProduct(p2);
            MongoDBClient.addOrUpdateProduct(p3);
            MongoDBClient.addOrUpdateProduct(p4);
            MongoDBClient.addOrUpdateProduct(p5);
            MongoDBClient.addOrUpdateProduct(p6);
            MongoDBClient.addOrUpdateProduct(p7);
            MongoDBClient.addOrUpdateProduct(p8);
            MongoDBClient.addOrUpdateProduct(p9);
            MongoDBClient.addOrUpdateProduct(p10);
            MongoDBClient.addOrUpdateProduct(p11);
            MongoDBClient.addOrUpdateProduct(p12);
            MongoDBClient.addOrUpdateProduct(p13);
            MongoDBClient.addOrUpdateProduct(p14);
            MongoDBClient.addOrUpdateProduct(p15);
            MongoDBClient.addOrUpdateProduct(p16);
            MongoDBClient.addOrUpdateProduct(p17);
            MongoDBClient.addOrUpdateProduct(p18);
            MongoDBClient.addOrUpdateProduct(p19);
            MongoDBClient.addOrUpdateProduct(p20);
            MongoDBClient.addOrUpdateProduct(p21);
        }


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/management/ui/views/login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setTitle("Inventory Management System - Login");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load the login UI.");
        }
    }

    public static void loadLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("/com/inventory/management/ui/views/login.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Inventory Management System - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadMainScene() {
        try {
            FXMLLoader loader = new FXMLLoader(UIManager.class.getResource("/com/inventory/management/ui/views/main.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Inventory Management System - Main");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveProductToDB(Product product) {
        MongoDBClient.addOrUpdateProduct(product); // Update the database
    }

    // Ensure methods that add/remove/edit products directly modify inventory
    public static void addProduct(Product product) {
        inventory.add(product);
        MongoDBClient.addOrUpdateProduct(product); // Update DB
    }

    public static void removeProduct(Product product) {
        inventory.remove(product);
        MongoDBClient.deleteProduct(product.getProductId()); // Update DB
    }

    public static void updateProduct(Product product) {
        int index = inventory.indexOf(product); // Find product index
        if (index != -1) {
            inventory.set(index, product); // Update product in list
            MongoDBClient.addOrUpdateProduct(product); // Update DB
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}