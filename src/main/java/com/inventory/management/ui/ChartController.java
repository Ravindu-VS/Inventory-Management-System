package com.inventory.management.ui;

import com.inventory.management.model.Product;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

public class ChartController {

    @FXML
    private BarChart<String, Number> barChart;

    public void initializeChart(List<Product> products) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        barChart.setTitle("Product Quantities by Category");
        xAxis.setLabel("Category");
        yAxis.setLabel("Quantity");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Quantities");

        products.stream()
                .collect(java.util.stream.Collectors.groupingBy(Product::getCategory, java.util.stream.Collectors.summingInt(Product::getQuantity)))
                .forEach((category, quantity) -> series.getData().add(new XYChart.Data<>(category, quantity)));

        barChart.getData().add(series);
    }
}
