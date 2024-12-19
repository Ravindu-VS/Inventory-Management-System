package com.inventory.management.ui;

import com.inventory.management.model.Product;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller for the Chart UI.
 */
public class ChartController {

    @FXML
    private BarChart<String, Number> barChart;

    /**
     * Initializes the chart with inventory data.
     */
    @FXML
    public void initialize() {
        updateChart();
    }

    /**
     * Updates the bar chart based on the current inventory.
     */
    private void updateChart() {
        barChart.getData().clear();
        barChart.setTitle("Product Quantities by Category");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Quantities");

        // Group products by category and sum their quantities
        Map<String, Integer> categoryQuantities = UIManager.inventory.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.summingInt(Product::getQuantity)));

        // Populate the series with data
        categoryQuantities.forEach((category, quantity) -> {
            series.getData().add(new XYChart.Data<>(category, quantity));
        });

        barChart.getData().add(series);
    }
}
