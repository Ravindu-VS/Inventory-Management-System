package com.inventory.management.ui;

import com.inventory.management.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.time.LocalDate;

public class EditProductController {
    @FXML private TextField productIdField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private ChoiceBox<String> categoryChoiceBox;
    @FXML private TextField imagePathField;
    @FXML private DatePicker dateAddedPicker;

    private Product updatedProduct;

    @FXML
    public void initialize() {
        categoryChoiceBox.getItems().addAll("Electronics", "Accessories", "Fruits", "Books", "Clothing", "Food");
    }

    public void setProduct(Product product) {
        productIdField.setText(product.getProductId());
        productIdField.setEditable(false);
        nameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getPrice()));
        quantityField.setText(String.valueOf(product.getQuantity()));
        categoryChoiceBox.setValue(product.getCategory());
        imagePathField.setText(product.getImagePath());
        dateAddedPicker.setValue(product.getDateAdded());
    }

    public void collectProductDetails() {
        String productId = productIdField.getText().trim();
        String name = nameField.getText().trim();
        double price;
        int quantity;
        String category = categoryChoiceBox.getValue();
        String imagePath = imagePathField.getText().trim();
        LocalDate dateAdded = dateAddedPicker.getValue();

        try {
            price = Double.parseDouble(priceField.getText().trim());
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException e) {
            updatedProduct = null;
            return;
        }

        updatedProduct = new Product(productId, name, price, quantity, category, imagePath, dateAdded);
    }

    public Product getUpdatedProduct() {
        return updatedProduct;
    }
}