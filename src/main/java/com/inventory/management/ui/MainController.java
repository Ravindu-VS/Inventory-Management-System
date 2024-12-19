package com.inventory.management.ui;

import com.inventory.management.model.Product;
import com.inventory.management.model.User;
import com.inventory.management.recursive.CalculationUtility;
import com.inventory.management.recursive.SearchUtility;
import com.inventory.management.performance.InventoryBackupManager;
import com.inventory.management.utils.PDFReportManager;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for the Main UI.
 */
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

    @FXML private Button addProductButton;
    @FXML private Button backupButton;
    @FXML private Button restoreButton;
    @FXML private Button generateReportButton; // Added Report Button

    private ObservableList<Product> fullInventory;
    private boolean isAdmin;

    // Added FilteredList and SortedList for performance optimization
    private FilteredList<Product> filteredData;
    private SortedList<Product> sortedData;

    /**
     * Initializes the Main UI after loading the scene ```java
     /**
     * Initializes the Main UI after loading the scene.
     */
    @FXML
    public void initialize() {
        User currentUser  = UIManager.currentUser ;
        if (currentUser  == null) {
            // If no user is logged in, redirect to login
            UIManager.loadLoginScene();
            return;
        }

        isAdmin = currentUser .getRole() == User.Role.ADMIN;

        configureTableColumns();

        // Directly use UIManager.inventory instead of creating a separate list
        fullInventory = UIManager.inventory;

        // Initialize FilteredList and SortedList BEFORE initializing ChoiceBoxes and attaching listeners
        filteredData = new FilteredList<>(fullInventory, p -> true);
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.setItems(sortedData); // Bind SortedList to TableView

        initializeChoiceBoxes();

        setWelcomeMessage(currentUser );
        configureUIElementsBasedOnRole(currentUser );

        highlightActiveLink(homeLink); // Default active link

        // Add listener for search bar AFTER setting up filteredData
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFiltersAndSort();
        });
    }

    /**
     * Configures the table columns with appropriate cell value factories.
     */
    private void configureTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    /**
     * Initializes the category and sort choice boxes with options.
     */
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

        // Add listeners to choice boxes to trigger filter and sort AFTER filteredData is initialized
        categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            applyFiltersAndSort();
        });

        sortChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // No need to manually sort; SortedList handles it
            applyFiltersAndSort();
        });
    }

    /**
     * Sets the welcome message based on the logged-in user.
     *
     * @param user the logged-in user
     */
    private void setWelcomeMessage(User user) {
        welcomeLabel.setText("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
    }

    /**
     * Configures UI elements (links and actions) based on user roles.
     *
     * @param user the logged-in user
     */
    private void configureUIElementsBasedOnRole(User user) {
        // Show reports link only if admin
        reportsLink.setVisible(isAdmin);

        // Show Actions column only if admin or staff
        boolean canModify = user.getRole() == User.Role.ADMIN || user.getRole() == User.Role.STAFF; // Assuming Staff can modify
        colActions.setVisible(canModify);
        if (canModify) {
            addActionButtonsToTable();
        }

        // Show Add Product, Backup, Restore, Generate Report buttons only to Admins
        addProductButton.setVisible(isAdmin);
        backupButton.setVisible(isAdmin);
        restoreButton.setVisible(isAdmin);
        generateReportButton.setVisible(isAdmin);
    }

    /**
     * Adds action buttons (Edit, Delete) to each row in the product table.
     */
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
                        fullInventory.remove(product);
                        // Debug Statement
                        System.out.println("Deleted Product: " + product.getName());
                        showAlert("Success", "Product deleted successfully.");
                        // Reapply filters to refresh the table
                        applyFiltersAndSort();
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

    /**
     * Opens a dialog to edit the selected product's details.
     *
     * @param product the product to edit
     */
    private void openEditProductDialog(Product product) {
        try {
            URL fxmlLocation = getClass().getResource("/com/inventory/management/ui/views/EditProductDialog.fxml");
            if (fxmlLocation == null) {
                showAlert("Error", "FXML file for Edit Product dialog not found.");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            DialogPane dialogPane = loader.load();

            EditProductController controller = loader.getController();
            controller.setProduct(product);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Edit Product - " + product.getName());

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Product updatedProduct = controller.getUpdatedProduct();
                if (updatedProduct == null) {
                    showAlert("Error", "Invalid input. Please check the entered values.");
                    return;
                }
                if (validateProduct(updatedProduct, false)) {
                    // Find the index of the original product in fullInventory
                    int index = fullInventory.indexOf(product);
                    if (index != -1) {
                        // Replace the old product with the updated product in fullInventory
                        fullInventory.set(index, updatedProduct);
                        System.out.println("Edited Product: " + updatedProduct.getName());
                    }

                    // Refresh the table
                    applyFiltersAndSort(); // Ensure the table is refreshed
                    showAlert("Success", "Product updated successfully.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the edit dialog.");
        }
    }

    /**
     * Handles the Home link action.
     */
    @FXML
    private void onHome() {
        highlightActiveLink(homeLink);
        applyFiltersAndSort();
    }

    /**
     * Handles the Charts link action.
     */
    @FXML
    private void onCharts() {
        highlightActiveLink(chartsLink);
        openChartWindow();
    }

    /**
     * Handles the Reports link action.
     */
    @FXML
    private void onReports() {
        highlightActiveLink(reportsLink);
        // Implement report logic or load a new scene
        showAlert("Reports", "Reports feature is under development.");
    }

    /**
     * Handles the Logout link action.
     */
    @FXML
    private void onLogout() {
        UIManager.currentUser = null;
        UIManager.loadLoginScene();
    }

    /**
     * Handles the Apply Price Filter button action.
     */
    @FXML
    private void onApplyPriceFilter() {
        applyFiltersAndSort();
    }

    /**
     * Handles the search bar key release event.
     *
     * @param keyEvent the key event
     */
    @FXML
    private void onSearch(KeyEvent keyEvent) {
        applyFiltersAndSort();
    }

    /**
     * Handles the Add Product button action.
     */
    @FXML
    private void onAddProduct() {
        try {
            // Load the FXML for the Add Product dialog
            URL fxmlLocation = getClass().getResource("/com/inventory/management/ui/views/AddProductDialog.fxml");
            if (fxmlLocation == null) {
                showAlert("Error", "FXML file for Add Product dialog not found.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            DialogPane dialogPane = loader.load();

            final AddProductController controller = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Add New Product");

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
                    fullInventory.add(newProduct); // Ensure this is the ObservableList
                    System.out.println("Added Product: " + newProduct.getName());
                    showAlert("Success", "Product added successfully.");
                    applyFiltersAndSort(); // Refresh the table
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the add product dialog.");
        }
    }

    /**
     * Handles the Backup button action.
     */
    @FXML
    private void onBackup() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Backup Inventory");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showSaveDialog(UIManager.primaryStage);
        if (selectedFile != null) {
            try {
                InventoryBackupManager.backupInventory(fullInventory, selectedFile.getAbsolutePath());
                showAlert("Success", "Inventory backed up successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to backup inventory.");
            }
        }
    }

    /**
     * Handles the Restore button action.
     */
    @FXML
    private void onRestore() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Restore Inventory");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(UIManager.primaryStage);
        if (selectedFile != null) {
            try {
                List<Product> restoredInventory = InventoryBackupManager.restoreInventory(selectedFile.getAbsolutePath());
                // Ensure restoration is done on the JavaFX Application Thread
                Platform.runLater(() -> {
                    fullInventory.setAll(restoredInventory);
                    // Debug Statement
                    System.out.println("Restored Inventory: " + restoredInventory.size() + " products.");
                    applyFiltersAndSort(); // **Ensure the table is refreshed after restoration**
                    showAlert("Success", "Inventory restored successfully.");
                });
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to restore inventory.");
            }
        }
    }

    /**
     * Generates a bar chart based on the provided inventory data and saves it as an image.
     *
     * @param inventoryData The inventory data to base the chart on.
     * @return The File object pointing to the saved chart image.
     * @throws IOException If an error occurs during chart generation.
     */
    private File generateChart(List<Product> inventoryData) throws IOException {
        // Aggregate data by category
        Map<String, Integer> categoryQuantity = inventoryData.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.summingInt(Product::getQuantity)));

        // Create axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Category");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total Quantity");

        // Create BarChart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Inventory Quantity by Category");

        // Create series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Products");

        for (Map.Entry<String, Integer> entry : categoryQuantity.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);

        // Render the chart to an image
        WritableImage writableImage = barChart.snapshot(new SnapshotParameters(), null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);

        // Save the image to a temporary file
        File tempFile = File.createTempFile("chart", ".png");
        ImageIO.write(bufferedImage, "png", tempFile);

        return tempFile;
    }

    /**
     * Highlights the active hyperlink and resets others.
     *
     * @param activeLink the hyperlink to highlight
     */
    private void highlightActiveLink(Hyperlink activeLink) {
        // Reset styles
        homeLink.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-weight: bold;");
        chartsLink.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-weight: bold;");
        reportsLink.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-weight: bold;");
        logoutLink.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-weight: bold;");

        // Highlight active link
        activeLink.setStyle("-fx-font-size: 14px; -fx-text-fill: blue; -fx-font-weight: bold;");
    }

    /**
     * Displays a confirmation dialog with the specified title and message.
     *
     * @param title   the title of the dialog
     * @param message the message content
     * @return true if the user confirms; otherwise, false
     */
    private boolean showConfirmationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yes, no);

        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(no) == yes;
    }

    /**
     * Displays an alert dialog with the specified title and message.
     *
     * @param title   the title of the alert
     * @param message the message content
     */
    private void showAlert(String title, String message) {
        Alert alert;
        if ("Error".equalsIgnoreCase(title)) {
            alert = new Alert(Alert.AlertType.ERROR);
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Checks if a product ID already exists in the inventory.
     *
     * @param productId the product ID to check
     * @return true if the product ID exists; false otherwise
     */
    private boolean isProductIdExists(String productId) {
        return fullInventory.stream().anyMatch(p -> p.getProductId().equalsIgnoreCase(productId));
    }

    /**
     * Performs a recursive binary search to find a product by its Product ID.
     * Displays the result to the user.
     *
     * @param productId the Product ID to search for
     */
    @FXML
    private void onSearchProductById() {
        String productId = searchBar.getText().trim();
        if (productId.isEmpty()) {
            showAlert("Error", "Please enter a Product ID to search.");
            return;
        }

        int index = SearchUtility.findProductById(fullInventory, productId);
        if (index != -1) {
            Product foundProduct = fullInventory.get(index);
            showAlert("Product Found", "Product Details:\n" + foundProduct.toString());
        } else {
            showAlert("Not Found", "No product found with Product ID: " + productId);
        }
    }

    /**
     * Counts the number of products in a specific category using recursion.
     * Displays the result to the user.
     *
     * @param category the category to count
     */
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
            int count = CalculationUtility.countProductsInCategory(fullInventory, category.trim());
            showAlert("Count Result", "Total number of products in '" + category + "': " + count);
        });
    }

    /**
     * Calculates the total value of the inventory using recursion.
     * Displays the result to the user.
     */
    @FXML
    private void onCalculateTotalValue() {
        double totalValue = CalculationUtility.calculateTotalInventoryValue(fullInventory);
        showAlert("Total Inventory Value", "The total value of the inventory is: $" + String.format("%.2f", totalValue));
    }

    /**
     * Displays a confirmation dialog for performing recursive operations.
     */
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

    /**
     * Opens the Charts window.
     */
    private void openChartWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inventory/management/ui/views/chart.fxml"));
            Scene scene = new Scene(loader.load());

            Stage chartStage = new Stage();
            chartStage.setTitle("Inventory Charts");
            chartStage.setScene(scene);
            chartStage.initModality(Modality.NONE); // Allows interaction with other windows
            chartStage.initOwner(UIManager.primaryStage);
            chartStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open the chart window.");
        }
    }

    /**
     * Handles the Generate Report button action.
     */
    @FXML
    private void onGenerateReport() {
        // Ensure this method is used for both sidebar and top bar report generation
        if (fullInventory.isEmpty()) {
            showAlert("Error", "No inventory data available to generate report.");
            return;
        }

        // Step 1: Prompt user to select report frequency
        ChoiceDialog<String> frequencyDialog = new ChoiceDialog<>("Daily", "Daily", "Weekly", "Monthly", "Yearly");
        frequencyDialog.setTitle("Select Report Frequency");
        frequencyDialog.setHeaderText(null);
        frequencyDialog.setContentText("Choose the frequency for the report:");

        Optional<String> frequencyResult = frequencyDialog.showAndWait();
        if (!frequencyResult.isPresent()) {
            // User canceled the dialog
            return;
        }

        String frequency = frequencyResult.get();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        // Step 2: Determine the date range based on frequency
        switch (frequency) {
            case "Daily":
                startDate = endDate.minusDays(1);
                break;
            case "Weekly":
                startDate = endDate.minusWeeks(1);
                break;
            case "Monthly":
                startDate = endDate.minusMonths(1);
                break;
            case "Yearly":
                startDate = endDate.minusYears(1);
                break;
            default:
                startDate = endDate.minusDays(1);
        }

        // Step 3: Filter inventory data based on date range
        List<Product> filteredDataList = filterInventoryByDate(startDate, endDate);

        // Step 4: Prompt user to choose save location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("Inventory_Report_" + frequency + ".pdf");
        File saveFile = fileChooser.showSaveDialog(UIManager.primaryStage);
        if (saveFile == null) {
            // User canceled the save dialog
            return;
        }

        // Step 5: Generate PDF report on JavaFX Application Thread
        Platform.runLater(() -> {
            try {
                // Optional: Generate a chart for the report
                File chartFile = generateChartForReport(filteredDataList);

                PDFReportManager.generateReport(saveFile.getAbsolutePath(), filteredDataList, chartFile);
                showAlert("Success", "Report generated successfully at:\n" + saveFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to generate the PDF report.");
            }
        });
    }

    /**
     * Validates the product data and updates the table view.
     *
     * @param product      the product to validate
     * @param isNewProduct true if adding a new product; false if updating
     * @return true if the product is valid; false otherwise
     */
    private boolean validateProduct(Product product, boolean isNewProduct) {
        StringBuilder errors = new StringBuilder();

        if (product.getProductId() == null || product.getProductId().trim().isEmpty()) {
            errors.append("- Product ID cannot be empty.\n");
        } else if (isNewProduct && isProductIdExists(product.getProductId())) {
            errors.append("- Product ID already exists.\n");
        }

        if (product .getName() == null || product.getName().trim().isEmpty()) {
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
            showAlert("Error", "Please address the following issues:\n" + errors.toString());
            return false;
        }

        // Update the table view after adding or editing a product
        applyFiltersAndSort();
        return true;
    }

    /**
     * Filters the inventory data based on the provided date range.
     *
     * @param startDate The start date of the filter.
     * @param endDate   The end date of the filter.
     * @return A list of products within the specified date range.
     */
    private List<Product> filterInventoryByDate(LocalDate startDate, LocalDate endDate) {
        // Ensure that the Product class has a 'dateAdded' field of type LocalDate
        return fullInventory.stream()
                .filter(p -> {
                    LocalDate dateAdded = p.getDateAdded();
                    return (dateAdded.isEqual(startDate) || dateAdded.isAfter(startDate)) &&
                            (dateAdded.isEqual(endDate) || dateAdded.isBefore(endDate));
                })
                .collect(Collectors.toList());
    }

    /**
     * Implements filtering and sorting based on user inputs.
     * Utilizes FilteredList and SortedList for optimal performance.
     */
    private void applyFiltersAndSort() {
        // Get filter criteria
        String selectedCategory = categoryChoiceBox.getValue();
        String searchText = searchBar.getText().trim().toLowerCase();
        String sortOption = sortChoiceBox.getValue();

        String minPriceText = minValueField.getText().trim();
        String maxPriceText = maxValueField.getText().trim();

        double minPrice = 0;
        double maxPrice = Double.MAX_VALUE;

        // Parse min and max price
        try {
            if (!minPriceText.isEmpty()) {
                minPrice = Double.parseDouble(minPriceText);
                if (minPrice < 0) {
                    showAlert("Error", "Minimum price cannot be negative.");
                    return;
                }
            }
            if (!maxPriceText.isEmpty()) {
                maxPrice = Double.parseDouble(maxPriceText);
                if (maxPrice < 0) {
                    showAlert("Error", "Maximum price cannot be negative.");
                    return;
                }
            }
            if (minPrice > maxPrice) {
                showAlert("Error", "Minimum price cannot be greater than maximum price.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numerical values for price filters.");
            return;
        }

        final double finalMinPrice = minPrice;
        final double finalMaxPrice = maxPrice;

        // Set the predicate for filtering
        filteredData.setPredicate(p -> {
            // Category filter
            if (!"All Categories".equals(selectedCategory)) {
                if (p.getCategory() == null || !p.getCategory().equalsIgnoreCase(selectedCategory)) {
                    return false;
                }
            }

            // Price filter
            if (p.getPrice() < finalMinPrice || p.getPrice() > finalMaxPrice) {
                return false;
            }

            // Search filter (by name or ID)
            if (!searchText.isEmpty()) {
                boolean matchesName = p.getName() != null && p.getName().toLowerCase().contains(searchText);
                boolean matchesId = p.getProductId() != null && p.getProductId().toLowerCase().contains(searchText);
                if (!matchesName && !matchesId) {
                    return false;
                }
            }

            return true; // Include the product
        });

        // Sorting is handled automatically by SortedList based on TableView's comparator
        // No additional sorting logic needed here
    }

    /**
     * Generates a chart for the PDF report.
     *
     * @param inventoryData The inventory data to base the chart on.
     * @return The File object pointing to the saved chart image.
     * @throws IOException If an error occurs during chart generation.
     */
    private File generateChartForReport(List<Product> inventoryData) throws IOException {
        // Aggregate data by category
        Map<String, Integer> categoryQuantity = inventoryData.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.summingInt(Product::getQuantity)));

        // Create axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Category");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total Quantity");

        // Create BarChart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Inventory Quantity by Category");

        // Create series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Products");

        for (Map.Entry<String, Integer> entry : categoryQuantity.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);

        // Render the chart to an image
        WritableImage writableImage = barChart.snapshot(new SnapshotParameters(), null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);

        // Save the image to a temporary file
        File tempFile = File.createTempFile("inventory_chart", ".png");
        ImageIO.write(bufferedImage, "png", tempFile);

        return tempFile;
    }

    /**
     * Handler for category choice box change event.
     * This method is called when the category is changed.
     */
    @FXML
    private void onCategoryChanged() {
        // This method just calls applyFiltersAndSort
        applyFiltersAndSort();
    }

    /**
     * Handler for sort choice box change event.
     * This method is called when the sort option is changed.
     */
    @FXML
    private void onSortChanged() {
        // This method just calls applyFiltersAndSort
        applyFiltersAndSort();
    }
}
