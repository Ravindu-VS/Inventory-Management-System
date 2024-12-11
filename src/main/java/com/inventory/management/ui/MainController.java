package com.inventory.management.ui;

import com.inventory.management.model.Product;
import com.inventory.management.sorting.MergeSort;
import com.inventory.management.sorting.QuickSort;
import com.inventory.management.sorting.ShellSort;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {

    @FXML private Label welcomeLabel;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colQuantity;
    @FXML private TableColumn<Product, Void> colActions;

    @FXML private ChoiceBox<String> categoryChoiceBox;
    @FXML private TextField minValueField;
    @FXML private TextField maxValueField;
    @FXML private ChoiceBox<String> sortChoiceBox;
    @FXML private TextField searchBar;
    @FXML private Hyperlink homeLink;
    @FXML private Hyperlink chartsLink;
    @FXML private Hyperlink reportsLink;
    @FXML private Hyperlink logoutLink;

    private List<Product> fullInventory;
    private boolean isAdmin;

    @FXML
    public void initialize() {
        isAdmin = (UIManager.currentUser != null && "Admin".equalsIgnoreCase(UIManager.currentUser.getRole()));

        configureTableColumns();
        fullInventory = UIManager.inventory;

        categoryChoiceBox.getItems().addAll("All Categories", "Electronics", "Clothing", "Books", "Food");
        sortChoiceBox.getItems().addAll(
                "Name (A-Z)",
                "Name (Z-A)",
                "Price (High-Low)",
                "Price (Low-High)",
                "Quantity (High-Low)",
                "Quantity (Low-High)"
        );

        categoryChoiceBox.getSelectionModel().select("All Categories");
        sortChoiceBox.getSelectionModel().select("Name (A-Z)");

        productTable.getItems().setAll(fullInventory);

        if (UIManager.currentUser != null) {
            welcomeLabel.setText("Welcome, " + UIManager.currentUser.getUsername() + " (" + UIManager.currentUser.getRole() + ")");
        } else {
            welcomeLabel.setText("Welcome to the Inventory Management System");
        }

        // Show reports link only if admin
        reportsLink.setVisible(isAdmin);

        // Show Actions column only if admin
        colActions.setVisible(isAdmin);
        if (isAdmin) {
            addActionButtonsToTable();
        }

        highlightActiveLink(homeLink); // Default active link
    }

    private void configureTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
    }

    private void addActionButtonsToTable() {
        colActions.setCellFactory(tc -> new TableCell<>() {
            private final HBox container = new HBox(10);
            private final Button deleteButton = new Button("Delete");
            private final Button editButton = new Button("Edit");

            {
                deleteButton.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius:5;");
                editButton.setStyle("-fx-background-color: #4A90E2; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius:5;");

                deleteButton.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(product);
                    fullInventory.remove(product);
                });

                editButton.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    // Implement edit dialog logic
                });

                container.getChildren().addAll(deleteButton, editButton);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    @FXML
    private void onHome() {
        highlightActiveLink(homeLink);
        applyFiltersAndSort();
    }

    @FXML
    private void onCharts() {
        highlightActiveLink(chartsLink);
        UIManager.loadChartScene();
    }

    @FXML
    private void onReports() {
        highlightActiveLink(reportsLink);
        // Implement report logic
    }

    @FXML
    private void onLogout() {
        UIManager.currentUser = null;
        UIManager.loadLoginScene();
    }

    @FXML
    private void onCategoryChanged() {
        applyFiltersAndSort();
    }

    @FXML
    private void onSortChanged() {
        applyFiltersAndSort();
    }

    @FXML
    private void onApplyPriceFilter() {
        applyFiltersAndSort();
    }

    @FXML
    private void onSearch(KeyEvent keyEvent) {
        applyFiltersAndSort();
    }

    private void applyFiltersAndSort() {
        if (fullInventory == null) return;

        String selectedCategory = categoryChoiceBox.getValue() == null ? "All Categories" : categoryChoiceBox.getValue();

        double minVal = parseDouble(minValueField.getText(), -1);
        double maxVal = parseDouble(maxValueField.getText(), Double.MAX_VALUE);

        String searchTerm = searchBar.getText().toLowerCase();

        List<Product> filteredProducts = fullInventory.stream()
                .filter(p -> ("All Categories".equals(selectedCategory) || p.getCategory().equalsIgnoreCase(selectedCategory)))
                .filter(p -> p.getPrice() >= minVal && p.getPrice() <= maxVal)
                .filter(p -> searchTerm.isEmpty() || p.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());

        sortProducts(filteredProducts);
        productTable.getItems().setAll(filteredProducts);
    }

    private void sortProducts(List<Product> products) {
        String sortOption = sortChoiceBox.getValue() == null ? "Name (A-Z)" : sortChoiceBox.getValue();
        switch (sortOption) {
            case "Name (A-Z)":
                MergeSort.sortByName(products);
                break;
            case "Name (Z-A)":
                MergeSort.sortByName(products);
                Collections.reverse(products);
                break;
            case "Price (High-Low)":
                QuickSort.sortByPriceDesc(products, 0, products.size() - 1);
                break;
            case "Price (Low-High)":
                QuickSort.sortByPriceDesc(products, 0, products.size() - 1);
                Collections.reverse(products);
                break;
            case "Quantity (High-Low)":
                ShellSort.sortByQuantity(products);
                Collections.reverse(products);
                break;
            case "Quantity (Low-High)":
                ShellSort.sortByQuantity(products);
                break;
        }
    }

    private double parseDouble(String text, double defaultValue) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void highlightActiveLink(Hyperlink activeLink) {
        homeLink.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-weight: bold;");
        chartsLink.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-weight: bold;");
        reportsLink.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-weight: bold;");
        logoutLink.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-weight: bold;");

        activeLink.setStyle("-fx-font-size: 14px; -fx-text-fill: blue; -fx-font-weight: bold;");
    }
}
