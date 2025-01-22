package com.inventory.management.utils;

import com.inventory.management.model.Product;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PDFReportManager {

    /**
     * Generates a chart image file for the given inventory data.
     *
     * @param inventoryData The list of products to base the chart on.
     * @return The File object pointing to the created chart image.
     * @throws IOException if an error occurs during image writing
     */
    public static File generateChartForReport(List<Product> inventoryData) throws IOException {
        Map<String, Integer> categoryQuantity = inventoryData.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.summingInt(Product::getQuantity)));

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Category");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total Quantity");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Inventory Quantity by Category");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Quantity");

        for (Map.Entry<String, Integer> entry : categoryQuantity.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);

        WritableImage writableImage = barChart.snapshot(new SnapshotParameters(), null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);

        File tempFile = File.createTempFile("inventory_chart", ".png");
        ImageIO.write(bufferedImage, "png", tempFile);
        return tempFile;
    }

    /**
     * Generates a PDF report with the given inventory data and chart image.
     *
     * @param filePath      The path where the PDF will be saved.
     * @param inventoryData The list of products to include in the report.
     * @param chartImage    The chart image file to include in the report.
     * @throws IOException if an error occurs during PDF generation
     */
    public static void generateReport(String filePath, List<Product> inventoryData, File chartImage) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.LETTER);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Add Title and Metadata
        contentStream.beginText();
        contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 20);
        contentStream.newLineAtOffset(50, 700);
        contentStream.showText("Inventory Report");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(50, 680);
        contentStream.showText("Generated on: " + LocalDate.now().toString());
        contentStream.endText();

        // Add Table Headers
        contentStream.beginText();
        contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(50, 660);
        contentStream.showText(String.format("%-15s %-20s %-15s %-10s %-10s", "Product ID", "Name", "Category", "Price", "Quantity"));
        contentStream.endText();

        // Add Table Content with Multiple Page Support
        int yPosition = 640;
        contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 12);
        for (Product p : inventoryData) {
            if (yPosition < 100) {
                // Close current content stream
                contentStream.close();

                // Add a new page
                page = new PDPage(PDRectangle.LETTER);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);

                // Reset yPosition for the new page
                yPosition = 700;

                // Add Table Headers to the new page
                contentStream.beginText();
                contentStream.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText(String.format("%-15s %-20s %-15s %-10s %-10s", "Product ID", "Name", "Category", "Price", "Quantity"));
                contentStream.endText();
                yPosition -= 20;
            }

            // Add Product Data
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText(String.format("%-15s %-20s %-15s $%-9.2f %-10d",
                    p.getProductId(),
                    p.getName(),
                    p.getCategory(),
                    p.getPrice(),
                    p.getQuantity()));
            contentStream.endText();
            yPosition -= 20;
        }

        // Add Chart Image to the last page
        if (chartImage != null && chartImage.exists()) {
            PDImageXObject pdImage = PDImageXObject.createFromFileByContent(chartImage, document);
            contentStream.drawImage(pdImage, 50, 100, 500, 300);
        }

        // Close the content stream
        contentStream.close();

        // Save and close the document
        document.save(filePath);
        document.close();
    }
}
