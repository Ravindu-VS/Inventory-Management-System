package com.inventory.management.ui;

import com.inventory.management.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import java.time.LocalDate;

public class EditProductController {

    @FXML private TextField productIdField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private ChoiceBox<String> categoryChoiceBox;
    @FXML private TextField imagePathField; // If you have image handling
    @FXML private DatePicker dateAddedPicker;

    private Product updatedProduct;

    /**
     * Initializes the Edit Product dialog.
     */
    @FXML
    public void initialize() {
        categoryChoiceBox.getItems().addAll("Electronics", "Accessories", "Fruits", "Books", "Clothing", "Food");
    }

    /**
     * Sets the product to be edited and populates the form fields.
     *
     * @param product the product to edit
     */
    public void setProduct(Product product) {
        productIdField.setText(product.getProductId());
        productIdField.setEditable(false); // Typically, productId is immutable
        nameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getPrice()));
        quantityField.setText(String.valueOf(product.getQuantity()));
        categoryChoiceBox.setValue(product.getCategory());
        imagePathField.setText(product.getImagePath()); // If applicable
        dateAddedPicker.setValue(product.getDateAdded());
    }

    /**
     * Collects updated product details from the form fields.
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
            updatedProduct = null;
            return;
        }

        updatedProduct = new Product(productId, name, price, quantity, category, imagePath, dateAdded);
    }

    /**
     * Retrieves the updated product.
     *
     * @return the updated product
     */
    public Product getUpdatedProduct() {
        return updatedProduct;
    }
}
