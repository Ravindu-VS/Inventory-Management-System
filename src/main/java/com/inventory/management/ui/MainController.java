package com.inventory.management.ui;

import com.inventory.management.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

    @FXML private Label welcomeLabel;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colQuantity;
    @FXML private TableColumn<Product, String> colCategory;

    public void initialize() {
        // Display welcome message with username and role
        if (UIManager.currentUser != null) {
            welcomeLabel.setText("Welcome, " + UIManager.currentUser.getUsername() + " (" + UIManager.currentUser.getRole() + ")");
        } else {
            welcomeLabel.setText("Welcome to the Inventory Management System");
        }

        // Configure table columns
        configureTableColumns();

        // Populate table with inventory data
        productTable.getItems().addAll(UIManager.inventory);
    }

    private void configureTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
    }
}
