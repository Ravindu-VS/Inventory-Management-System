package com.inventory.management.utils;

import com.inventory.management.model.Product;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartManager {
    public static BarChart<String, Number> generateCategoryChart(List<Product> productData) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Category");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Quantity");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Product Quantities by Category");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Quantities");

        Map<String, Integer> categoryQuantities = productData.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.summingInt(Product::getQuantity)));

        categoryQuantities.forEach((category, quantity) -> {
            series.getData().add(new XYChart.Data<>(category, quantity));
        });

        barChart.getData().add(series);

        return barChart;
    }
}
