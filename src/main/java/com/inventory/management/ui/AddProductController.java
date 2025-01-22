package com.inventory.management.ui;

import com.inventory.management.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.time.LocalDate;

public class AddProductController {
    @FXML private TextField productIdField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private ChoiceBox<String> categoryChoiceBox;
    @FXML private TextField imagePathField;
    @FXML private DatePicker dateAddedPicker;

    private Product newProduct;

    @FXML
    public void initialize() {
        categoryChoiceBox.getItems().addAll("Electronics", "Accessories", "Fruits", "Books", "Clothing", "Food");
        categoryChoiceBox.getSelectionModel().selectFirst();
        dateAddedPicker.setValue(LocalDate.now());
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

            newProduct = new Product(productId, name, price, quantity, category, imagePath, dateAdded);
        } catch (NumberFormatException e) {
            newProduct = null;
        }
    }

    public Product getNewProduct() {
        return newProduct;
    }

    public void clearFields() {
        productIdField.clear();
        nameField.clear();
        priceField.clear();
        quantityField.clear();
        categoryChoiceBox.getSelectionModel().selectFirst();
        imagePathField.clear();
        dateAddedPicker.setValue(LocalDate.now());
    }
}
