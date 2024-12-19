package com.inventory.management.utils;

import com.inventory.management.model.Product;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Utility class to manage PDF report generation using Apache PDFBox.
 */
public class PDFReportManager {

    /**
     * Generates a comprehensive PDF report of the inventory.
     *
     * @param filePath      The path where the PDF will be saved.
     * @param inventoryData The list of products to include in the report.
     * @param chartImage    The image of the chart to include.
     * @throws IOException If an error occurs during PDF generation.
     */
    public static void generateReport(String filePath, List<Product> inventoryData, File chartImage) throws IOException {
        // Create a new document
        PDDocument document = new PDDocument();

        // Add a page
        PDPage page = new PDPage(PDRectangle.LETTER);
        document.addPage(page);

        // Start a new content stream
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Define starting positions
        float margin = 50;
        float yStart = PDRectangle.LETTER.getHeight() - margin;
        float yPosition = yStart;

        // Add Title
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Inventory Report");
        contentStream.endText();

        yPosition -= 25;

        // Add Report Date
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Report Generated on: " + LocalDate.now());
        contentStream.endText();

        yPosition -= 20;

        // Draw a line separator
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(PDRectangle.LETTER.getWidth() - margin, yPosition);
        contentStream.stroke();

        yPosition -= 20;

        // Add Table Headers
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText(String.format("%-15s %-20s %-15s %-10s %-10s",
                "Product ID", "Name", "Category", "Price", "Quantity"));
        contentStream.endText();

        yPosition -= 15;

        // Draw another line separator
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(PDRectangle.LETTER.getWidth() - margin, yPosition);
        contentStream.stroke();

        yPosition -= 15;

        // Add Table Content
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        for (Product product : inventoryData) {
            if (yPosition < margin) {
                // Add new page if space is insufficient
                contentStream.close();
                page = new PDPage(PDRectangle.LETTER);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = yStart;
            }

            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(String.format("%-15s %-20s %-15s $%-9.2f %-10d",
                    product.getProductId(),
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getQuantity()));
            contentStream.endText();

            yPosition -= 15;
        }

        yPosition -= 20;

        // Add Chart Image if available
        if (chartImage != null && chartImage.exists()) {
            PDImageXObject pdImage = PDImageXObject.createFromFile(chartImage.getAbsolutePath(), document);
            float imageWidth = PDRectangle.LETTER.getWidth() - 2 * margin;
            float imageHeight = imageWidth * pdImage.getHeight() / pdImage.getWidth(); // Maintain aspect ratio

            if (yPosition - imageHeight < margin) {
                // Add new page if space is insufficient
                contentStream.close();
                page = new PDPage(PDRectangle.LETTER);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = yStart;
            }

            contentStream.drawImage(pdImage, margin, yPosition - imageHeight, imageWidth, imageHeight);
            yPosition -= (imageHeight + 20);
        }

        // Close the content stream
        contentStream.close();

        // Save the document
        document.save(filePath);
        document.close();
    }

    /**
     * Sample main method to test PDF report generation.
     * You can remove or modify this method as needed.
     */
    public static void main(String[] args) {
        try {
            // Sample inventory data
            List<Product> sampleData = List.of(
                    new Product("P001", "Apple", 2.50, 100, "Fruits", "/images/apple.png", LocalDate.now().minusDays(2)),
                    new Product("P002", "Banana", 1.20, 200, "Fruits", "/images/banana.png", LocalDate.now().minusDays(1)),
                    new Product("P003", "Orange", 2.00, 150, "Fruits", "/images/orange.png", LocalDate.now().minusDays(3)),
                    new Product("P010", "Laptop", 1000.00, 20, "Electronics", "/images/laptop.png", LocalDate.now().minusDays(10)),
                    new Product("P011", "Smartphone", 800.00, 50, "Electronics", "/images/smartphone.png", LocalDate.now().minusDays(5)),
                    new Product("P012", "Headphones", 150.00, 80, "Accessories", "/images/headphones.png", LocalDate.now().minusDays(7)),
                    new Product("P013", "Keyboard", 70.00, 60, "Accessories", "/images/keyboard.png", LocalDate.now().minusDays(4))
            );

            // Path to save the PDF report
            String pdfPath = "Inventory_Report.pdf";

            // Path to the chart image (replace with actual path if you have a chart)
            File chartFile = new File("chart.png"); // Ensure this file exists or set to null

            // Generate PDF report
            generateReport(pdfPath, sampleData, chartFile);
            System.out.println("Report generated successfully at " + pdfPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
