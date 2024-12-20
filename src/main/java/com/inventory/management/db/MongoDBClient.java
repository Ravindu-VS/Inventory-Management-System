// src/main/java/com/inventory/management/db/MongoDBClient.java
package com.inventory.management.db;

import com.inventory.management.model.Product;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class MongoDBClient {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    public static void connect() {
        try {
            MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017");
            mongoClient = new MongoClient(uri);
            database = mongoClient.getDatabase("inventorydb");
            collection = database.getCollection("products");
            System.out.println("Connected to MongoDB successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to connect to MongoDB.");
        }
    }

    public static List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        FindIterable<Document> docs = collection.find();
        for (Document doc : docs) {
            Product p = new Product(
                    doc.getString("productId"),
                    doc.getString("name"),
                    doc.getDouble("price"),
                    doc.getInteger("quantity"),
                    doc.getString("category"),
                    doc.getString("imagePath"),
                    doc.getDate("dateAdded").toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            );
            products.add(p);
        }
        return products;
    }

    public static void addOrUpdateProduct(Product product) {
        Document doc = new Document("productId", product.getProductId())
                .append("name", product.getName())
                .append("price", product.getPrice())
                .append("quantity", product.getQuantity())
                .append("category", product.getCategory())
                .append("imagePath", product.getImagePath())
                .append("dateAdded", java.sql.Date.valueOf(product.getDateAdded()));
        collection.replaceOne(Filters.eq("productId", product.getProductId()), doc, new com.mongodb.client.model.ReplaceOptions().upsert(true));
    }

    public static void deleteProduct(String productId) {
        collection.deleteOne(Filters.eq("productId", productId));
    }
}
