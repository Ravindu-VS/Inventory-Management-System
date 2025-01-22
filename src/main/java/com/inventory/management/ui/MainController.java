package com.inventory.management.ui;

import com.inventory.management.model.Product;
import com.inventory.management.model.User;
import com.inventory.management.utils.InventoryBackupManager;
import com.inventory.management.recursive.CalculationUtility;
import com.inventory.management.recursive.SearchUtility;
import com.inventory.management.utils.PDFReportManager;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
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
    @FXML private Hyperlink logoutLink;

    @FXML private Button addProductButton;
    @FXML private Button backupButton;
    @FXML private Button restoreButton;
    @FXML private Button generateReportButton;

    private User currentUser;
    private boolean isAdmin;

    private FilteredList<Product> filteredData;
    private SortedList<Product> sortedData;

    @FXML
    public void initialize() {
        currentUser = UIManager.currentUser;
        if (currentUser == null) {
            UIManager.loadLoginScene();
            return;
        }

        isAdmin = currentUser.getRole() == User.Role.ADMIN;

        configureTableColumns();

        // Initialize FilteredList and SortedList
        filteredData = new FilteredList<>(UIManager.inventory, p -> true);
        sortedData = new SortedList<>(filteredData);
        // Remove the binding to allow setting comparator programmatically
        // sortedData.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.setItems(sortedData);
        // Optionally disable TableView's built-in sorting
        // productTable.setSortPolicy(null);

        initializeChoiceBoxes();
        setWelcomeMessage(currentUser);
        configureUIElementsBasedOnRole(currentUser);
        highlightActiveLink(homeLink);
    }

    private void configureTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    private void initializeChoiceBoxes() {
        categoryChoiceBox.getItems().addAll("All Categories", "Electronics", "Accessories", "Fruits", "Books", "Clothing", "Food");
        sortChoiceBox.getItems().addAll(
                "Name (A-Z)",
                "Name (Z-A)",
                "Price (High-Low)",
                "Price (Low-High)",
                "Quantity (High-Low)",
                "Quantity (Low-High)",
                "Category (A-Z) then Price (Low-High)"
        );

        categoryChoiceBox.getSelectionModel().select("All Categories");
        sortChoiceBox.getSelectionModel().select("Name (A-Z)");

        // Add listeners to apply filters and sorts when selections change
        categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            applyFiltersAndSort();
        });

        sortChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            applyFiltersAndSort();
        });

        // Add listener to searchBar for real-time searching
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFiltersAndSort();
        });
    }

    private void setWelcomeMessage(User user) {
        welcomeLabel.setText("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
    }

    private void configureUIElementsBasedOnRole(User user) {
        colActions.setVisible(isAdmin);
        if (isAdmin) {
            addActionButtonsToTable();
        }

        addProductButton.setVisible(isAdmin);
        backupButton.setVisible(isAdmin);
        restoreButton.setVisible(isAdmin);
        generateReportButton.setVisible(isAdmin);
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
                    boolean confirmed = showConfirmationDialog("Delete Product", "Are you sure you want to delete " + product.getName() + "?");
                    if (confirmed) {
                        UIManager.inventory.remove(product);
                        UIManager.removeProduct(product);
                        // productTable.refresh(); // Not necessary with ObservableList
                        showAlert("Success", "Product deleted successfully.");
                    }
                });

                editButton.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    openEditProductDialog(product);
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

    private void openEditProductDialog(Product product) {
        try {
            URL fxmlLocation = getClass().getResource("/com/inventory/management/ui/views/EditProductDialog.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            DialogPane dialogPane = loader.load();

            EditProductController controller = loader.getController();
            controller.setProduct(product);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Product - " + product.getName());

            // Set button types for the dialog
            dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    controller.collectProductDetails();
                    return ButtonType.OK;
                }
                return null;
            });

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Product updatedProduct = controller.getUpdatedProduct();
                if (updatedProduct == null) {
                    showAlert("Error", "Invalid input. Please check the entered values.");
                    return;
                }
                if (validateProduct(updatedProduct, false)) {
                    int index = UIManager.inventory.indexOf(product);
                    if (index != -1) {
                        UIManager.inventory.set(index, updatedProduct);
                        UIManager.saveProductToDB(updatedProduct);
                        productTable.refresh(); // Refresh the TableView immediately
                    }
                    showAlert("Success", "Product updated successfully.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the edit dialog.");
        }
    }

    @FXML
    private void onAddProduct() {
        try {
            URL fxmlLocation = getClass().getResource("/com/inventory/management/ui/views/AddProductDialog.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            DialogPane dialogPane = loader.load();

            AddProductController controller = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add New Product");

            // Set button types for the dialog
            dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    controller.collectProductDetails();
                    return ButtonType.OK;
                }
                return null;
            });

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Product newProduct = controller.getNewProduct();
                if (newProduct == null) {
                    showAlert("Error", "Invalid input. Please check the entered values.");
                    return;
                }

                if (validateProduct(newProduct, true)) {
                    UIManager.addProduct(newProduct);
                    productTable.refresh();
                    showAlert("Success", "Product added successfully.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the add product dialog.");
        }
    }

    @FXML
    private void onHome() {
        highlightActiveLink(homeLink);
        applyFiltersAndSort();
    }

    @FXML
    private void onCharts() {
        highlightActiveLink(chartsLink);
        openChartWindow();
    }

    @FXML
    private void onLogout() {
        UIManager.currentUser = null;
        UIManager.loadLoginScene();
    }

    @FXML
    private void onApplyPriceFilter() {
        applyFiltersAndSort();
    }

    @FXML
    private void onSearch(javafx.scene.input.KeyEvent keyEvent) {
        applyFiltersAndSort();
    }

    @FXML
    private void onBackup() {
        if (!isAdmin) return;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Backup Inventory");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showSaveDialog(UIManager.primaryStage);
        if (selectedFile != null) {
            try {
                InventoryBackupManager.backupInventory(UIManager.inventory, selectedFile.getAbsolutePath());
                showAlert("Success", "Inventory backed up successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to backup inventory.");
            }
        }
    }

    @FXML
    private void onRestore() {
        if (!isAdmin) return;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Restore Inventory");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(UIManager.primaryStage);
        if (selectedFile != null) {
            try {
                List<Product> restoredInventory = InventoryBackupManager.restoreInventory(selectedFile.getAbsolutePath());
                Platform.runLater(() -> {
                    UIManager.inventory.setAll(restoredInventory);
                    for (Product p : UIManager.inventory) {
                        UIManager.saveProductToDB(p);
                    }
                    // productTable.refresh(); // Not necessary with ObservableList
                    showAlert("Success", "Inventory restored successfully.");
                });
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to restore inventory.");
            }
        }
    }

    @FXML
    private void onGenerateReport() {
        if (!isAdmin) return;
        if (UIManager.inventory.isEmpty()) {
            showAlert("Error", "No inventory data available to generate report.");
            return;
        }

        String frequency = showFrequencyDialog();
        if (frequency == null) return;

        LocalDate endDate = LocalDate.now();
        LocalDate startDate;
        switch (frequency) {
            case "Daily": startDate = endDate.minusDays(1); break;
            case "Weekly": startDate = endDate.minusWeeks(1); break;
            case "Monthly": startDate = endDate.minusMonths(1); break;
            case "Yearly": startDate = endDate.minusYears(1); break;
            default: startDate = endDate.minusDays(1);
        }

        List<Product> filteredDataList = filterInventoryByDate(startDate, endDate);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Inventory_Report_" + frequency + ".pdf");
        File saveFile = fileChooser.showSaveDialog(UIManager.primaryStage);
        if (saveFile == null) return;

        final List<Product> finalList = new ArrayList<>(filteredDataList);
        final File finalSaveFile = saveFile;

        Platform.runLater(() -> {
            try {
                File chartFile = PDFReportManager.generateChartForReport(finalList);
                PDFReportManager.generateReport(finalSaveFile.getAbsolutePath(), finalList, chartFile);
                showAlert("Success", "Report generated successfully at:\n" + finalSaveFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to generate the PDF report.");
            }
        });
    }

    private String showFrequencyDialog() {
        ChoiceDialog<String> frequencyDialog = new ChoiceDialog<>("Daily", "Daily", "Weekly", "Monthly", "Yearly");
        frequencyDialog.setTitle("Select Report Frequency");
        frequencyDialog.setContentText("Choose the frequency for the report:");

        Optional<String> frequencyResult = frequencyDialog.showAndWait();
        return frequencyResult.orElse(null);
    }

    @FXML
    private void onSearchProductById() {
        String searchText = searchBar.getText();
        if (searchText == null) searchText = "";
        searchText = searchText.trim().toLowerCase();
        String productId = searchText;
        if (productId.isEmpty()) {
            showAlert("Error", "Please enter a Product ID to search.");
            return;
        }

        // Sort by ID before search
        List<Product> sortedList = new ArrayList<>(UIManager.inventory);
        sortedList.sort(Comparator.comparing(Product::getProductId, String.CASE_INSENSITIVE_ORDER));
        int index = SearchUtility.findProductById(sortedList, productId);
        if (index != -1) {
            Product foundProduct = sortedList.get(index);
            showAlert("Product Found", "Product Details:\n" + foundProduct);
        } else {
            showAlert("Not Found", "No product found with Product ID: " + productId);
        }
    }

    @FXML
    private void onCountProductsInCategory() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Count Products in Category");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Category:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(category -> {
            if (category.trim().isEmpty()) {
                showAlert("Error", "Category cannot be empty.");
                return;
            }
            int count = CalculationUtility.countProductsInCategory(UIManager.inventory, category.trim());
            showAlert("Count Result", "Total number of products in '" + category + "': " + count);
        });
    }

    @FXML
    private void onCalculateTotalValue() {
        double totalValue = CalculationUtility.calculateTotalInventoryValue(UIManager.inventory);
        showAlert("Total Inventory Value", "The total value of the inventory is: $" + String.format("%.2f", totalValue));
    }

    @FXML
    private void onPerformRecursiveOperations() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Recursive Operations");
        alert.setHeaderText("Choose an Operation");
        alert.setContentText("Select the recursive operation you want to perform:");

        ButtonType binarySearch = new ButtonType("Binary Search by Product ID");
        ButtonType countCategory = new ButtonType("Count Products in Category");
        ButtonType totalValue = new ButtonType("Calculate Total Inventory Value");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(binarySearch, countCategory, totalValue, cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == binarySearch) {
                onSearchProductById();
            } else if (result.get() == countCategory) {
                onCountProductsInCategory();
            } else if (result.get() == totalValue) {
                onCalculateTotalValue();
            }
        }
    }

    @FXML
    private void onCategoryChanged() {
        applyFiltersAndSort();
    }

    @FXML
    private void onSortChanged() {
        applyFiltersAndSort();
    }

    private List<Product> filterInventoryByDate(LocalDate startDate, LocalDate endDate) {
        return UIManager.inventory.stream()
                .filter(p -> {
                    LocalDate dateAdded = p.getDateAdded();
                    return (dateAdded == null || (!dateAdded.isBefore(startDate) && !dateAdded.isAfter(endDate)));
                })
                .collect(Collectors.toList());
    }

    private void applyFiltersAndSort() {
        String selectedCategory = categoryChoiceBox.getValue();
        if (selectedCategory == null) selectedCategory = "All Categories";

        String searchText = searchBar.getText();
        if (searchText == null) searchText = "";
        searchText = searchText.trim().toLowerCase();

        String sortOption = sortChoiceBox.getValue();
        if (sortOption == null) sortOption = "Name (A-Z)";

        double minPrice = 0;
        double maxPrice = Double.MAX_VALUE;

        try {
            if (!minValueField.getText().trim().isEmpty()) {
                minPrice = Double.parseDouble(minValueField.getText().trim());
            }
            if (!maxValueField.getText().trim().isEmpty()) {
                maxPrice = Double.parseDouble(maxValueField.getText().trim());
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numerical values for price filters.");
            return;
        }

        if (minPrice > maxPrice) {
            showAlert("Error", "Minimum price cannot be greater than maximum price.");
            return;
        }

        final String finalSelectedCategory = selectedCategory;
        final String finalSearchText = searchText;
        final double finalMinPrice = minPrice;
        final double finalMaxPrice = maxPrice;

        filteredData.setPredicate(p -> {
            if (!"All Categories".equals(finalSelectedCategory)) {
                if (p.getCategory() == null || !p.getCategory().equalsIgnoreCase(finalSelectedCategory)) return false;
            }
            if (p.getPrice() < finalMinPrice || p.getPrice() > finalMaxPrice) return false;

            if (!finalSearchText.isEmpty()) {
                boolean matchesName = p.getName() != null && p.getName().toLowerCase().contains(finalSearchText);
                boolean matchesId = p.getProductId() != null && p.getProductId().toLowerCase().contains(finalSearchText);
                if (!matchesName && !matchesId) return false;
            }
            return true;
        });


        // Apply sorting based on sortOption
        Comparator<Product> comparator = null;
        switch (sortOption) {
            case "Name (A-Z)":
                comparator = Comparator.comparing(p -> p.getName().toLowerCase());
                break;
            case "Name (Z-A)":
                comparator = Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER).reversed();
                break;
            case "Price (High-Low)":
                comparator = Comparator.comparingDouble(Product::getPrice).reversed();
                break;
            case "Price (Low-High)":
                comparator = Comparator.comparingDouble(Product::getPrice);
                break;
            case "Quantity (High-Low)":
                comparator = Comparator.comparingInt(Product::getQuantity).reversed();
                break;
            case "Quantity (Low-High)":
                comparator = Comparator.comparingInt(Product::getQuantity);
                break;
            case "Category (A-Z) then Price (Low-High)":
                comparator = Comparator.comparing(Product::getCategory, String.CASE_INSENSITIVE_ORDER)
                        .thenComparingDouble(Product::getPrice);
                break;
            default:
                comparator = Comparator.comparing(p -> p.getName().toLowerCase());
        }

        if (comparator != null) {
            sortedData.setComparator(comparator);
        }

        // Update table view with the latest sorted data
        productTable.setItems(sortedData);
    }

    private void openChartWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/management/ui/views/chart.fxml"));
            Scene scene = new Scene(loader.load());

            Stage chartStage = new Stage();
            chartStage.setTitle("Inventory Charts");
            chartStage.setScene(scene);
            chartStage.initModality(Modality.NONE);
            chartStage.initOwner(UIManager.primaryStage);
            chartStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the chart window.");
        }
    }

    private boolean validateProduct(Product product, boolean isNewProduct) {
        StringBuilder errors = new StringBuilder();

        if (product.getProductId() == null || product.getProductId().trim().isEmpty()) {
            errors.append("- Product ID cannot be empty.\n");
        } else if (isNewProduct && UIManager.inventory.stream().anyMatch(p -> p.getProductId().equalsIgnoreCase(product.getProductId()))) {
            errors.append("- Product ID already exists.\n");
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            errors.append("- Product name cannot be empty.\n");
        }
        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            errors.append("- Product category cannot be empty.\n");
        }
        if (product.getPrice() < 0) {
            errors.append("- Price cannot be negative.\n");
        }
        if (product.getQuantity() < 0) {
            errors.append("- Quantity cannot be negative.\n");
        }

        if (errors.length() > 0) {
            showAlert("Error", "Please address the following issues:\n" + errors);
            return false;
        }

        return true;
    }

    private void highlightActiveLink(Hyperlink activeLink) {
        homeLink.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-weight: bold;");
        chartsLink.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-weight: bold;");
        logoutLink.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-weight: bold;");

        activeLink.setStyle("-fx-font-size: 14px; -fx-text-fill: blue; -fx-font-weight: bold;");
    }

    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(ButtonType.NO) == ButtonType.YES;
    }

    private void showAlert(String title, String message) {
        Alert alert = ("Error".equalsIgnoreCase(title)) ? new Alert(Alert.AlertType.ERROR) : new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
