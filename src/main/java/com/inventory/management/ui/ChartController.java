// src/main/java/com/inventory/management/ui/ChartController.java
package com.inventory.management.ui;

import com.inventory.management.model.Product;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ChartController {
    @FXML private BarChart<String, Number> barChart;
    @FXML private PieChart pieChart;
    @FXML private LineChart<String, Number> lineChart;

    @FXML
    public void initialize() {
        // Initial Chart Population
        updateBarChartData(UIManager.inventory);
        updatePieChartData(UIManager.inventory);
        updateLineChartData(UIManager.inventory);

        // Listen for changes in the inventory to update charts
        UIManager.inventory.addListener((ListChangeListener.Change<? extends Product> change) -> {
            updateBarChartData(UIManager.inventory);
            updatePieChartData(UIManager.inventory);
            updateLineChartData(UIManager.inventory);
        });
    }

    /**
     * Updates the BarChart with the latest inventory data.
     *
     * @param inventory The current inventory list.
     */
    private void updateBarChartData(ObservableList<Product> inventory) {
        Map<String, Integer> categoryQuantity = inventory.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.summingInt(Product::getQuantity)));

        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Quantity");
        for (Map.Entry<String, Integer> entry : categoryQuantity.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().add(series);
    }

    /**
     * Updates the PieChart with the latest inventory distribution.
     *
     * @param inventory The current inventory list.
     */
    private void updatePieChartData(ObservableList<Product> inventory) {
        Map<String, Integer> categoryQuantity = inventory.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.summingInt(Product::getQuantity)));

        pieChart.getData().clear();
        for (Map.Entry<String, Integer> entry : categoryQuantity.entrySet()) {
            pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Updates the LineChart with the latest inventory value over time.
     *
     * @param inventory The current inventory list.
     */
    private void updateLineChartData(ObservableList<Product> inventory) {
        Map<String, Double> dateValue = inventory.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getDateAdded().format(DateTimeFormatter.ISO_DATE),
                        Collectors.summingDouble(p -> p.getPrice() * p.getQuantity())
                ));

        // Sort the dates
        List<String> sortedDates = new ArrayList<>(dateValue.keySet());
        Collections.sort(sortedDates);

        lineChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Value");

        for (String date : sortedDates) {
            series.getData().add(new XYChart.Data<>(date, dateValue.get(date)));
        }

        lineChart.getData().add(series);
    }
}
