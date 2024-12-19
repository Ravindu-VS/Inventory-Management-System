package com.inventory.management.ui;

import com.inventory.management.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import java.time.LocalDate;

public class AddProductController {

    @FXML private TextField productIdField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private ChoiceBox<String> categoryChoiceBox;
    @FXML private TextField imagePathField; // If you have image handling
    @FXML private DatePicker dateAddedPicker;

    private Product newProduct;

    /**
     * Initializes the Add Product dialog.
     */
    @FXML
    public void initialize() {
        categoryChoiceBox.getItems().addAll("Electronics", "Accessories", "Fruits", "Books", "Clothing", "Food");
        categoryChoiceBox.getSelectionModel().selectFirst();
        dateAddedPicker.setValue(LocalDate.now());
    }

    /**
     * Collects product details from the form fields.
     */
    public void collectProductDetails() {
        String productId = productIdField.getText().trim();
        String name = nameField.getText().trim();
        double price = 0;
        int quantity = 0;
        String category = categoryChoiceBox.getValue();
        String imagePath = imagePathField.getText().trim(); // If applicable
        LocalDate dateAdded = dateAddedPicker.getValue();

        try {
            price = Double.parseDouble(priceField.getText().trim());
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException e) {
            // Handle invalid number formats
            newProduct = null;
            return;
        }

        newProduct = new Product(productId, name, price, quantity, category, imagePath, dateAdded);
    }

    /**
     * Retrieves the new product.
     *
     * @return the newly created product
     */
    public Product getNewProduct() {
        return newProduct;
    }

    /**
     * Clears all input fields.
     */
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
