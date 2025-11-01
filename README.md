# Inventory Management System

A comprehensive JavaFX-based inventory management application with MongoDB integration, featuring user authentication, product management, data visualization, and PDF report generation.

## 📋 Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Key Features](#key-features)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

### Core Functionality
- **User Authentication**: Secure login system with BCrypt password hashing
- **Role-Based Access Control**: Admin and regular user roles with different permissions
- **Product Management**: Add, edit, delete, and view inventory items
- **Real-time Search**: Search products by name, ID, or category
- **Advanced Filtering**: Filter products by category and price range
- **Sorting Algorithms**: Multiple sorting options (Bubble Sort, Merge Sort, Quick Sort)
- **Data Visualization**: Interactive charts showing inventory distribution by category
- **PDF Report Generation**: Generate inventory reports with customizable frequency (Daily, Weekly, Monthly, Yearly)
- **Backup & Restore**: JSON-based backup and restore functionality for inventory data
- **MongoDB Integration**: Persistent data storage with MongoDB

### User Roles
- **Admin**: Full access to all features including product management, backups, and report generation
- **Regular User**: View-only access to inventory data

## 🛠 Technologies Used

- **Java 17**: Core programming language
- **JavaFX 23.0.1**: UI framework
- **Maven**: Build automation and dependency management
- **MongoDB 3.12.14**: Database for persistent storage
- **GSON 2.10.1**: JSON serialization/deserialization
- **BCrypt 0.4**: Password hashing
- **Apache PDFBox 2.0.29**: PDF report generation
- **JUnit 5.9.3**: Unit testing
- **SLF4J & Logback**: Logging framework

## 📦 Prerequisites

Before running this application, ensure you have the following installed:

- **Java Development Kit (JDK) 17** or higher
- **Apache Maven 3.6+**
- **MongoDB** (running locally or remote instance)
- **JavaFX SDK 23.0.1** (handled by Maven)

## 🚀 Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Ravindu-VS/Inventory-Management-System.git
   cd Inventory-Management-System
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Set up MongoDB**
   - Ensure MongoDB is running on `localhost:27017` (default)
   - Or configure your MongoDB connection in the `MongoDBClient` class

4. **Run the application**
   ```bash
   mvn javafx:run
   ```
   
   Or using exec plugin:
   ```bash
   mvn exec:java
   ```

## ⚙️ Configuration

### MongoDB Configuration
Update the MongoDB connection settings in `src/main/java/com/inventory/management/db/MongoDBClient.java`:

```java
// Default configuration
String uri = "mongodb://localhost:27017";
String dbName = "inventoryDB";
String collectionName = "products";
```

### Application Entry Point
The main class is configured in `pom.xml`:
```xml
<mainClass>com.inventory.management.ui.UIManager</mainClass>
```

## 💻 Usage

### Login
1. Launch the application
2. Enter your credentials
3. Admin users have full access; regular users have read-only access

### Product Management (Admin Only)
- **Add Product**: Click "Add Product" button and fill in the product details
- **Edit Product**: Click the "Edit" button in the product row
- **Delete Product**: Click the "Delete" button in the product row

### Search and Filter
- **Search**: Type in the search bar to find products by name or ID
- **Category Filter**: Select a category from the dropdown
- **Price Filter**: Set minimum and maximum price values and click "Apply Filter"

### Sorting
Select sorting criteria from the dropdown:
- Sort by Name
- Sort by Price
- Sort by Quantity
- Sort by Category

### Backup and Restore (Admin Only)
- **Backup**: Click "Backup" to export inventory to JSON file
- **Restore**: Click "Restore" to import inventory from JSON file

### Generate Reports (Admin Only)
1. Click "Generate Report"
2. Select frequency (Daily, Weekly, Monthly, Yearly)
3. Choose save location
4. PDF report will be generated with inventory details

### View Charts
Click the "Charts" link to view visual representations of inventory distribution by category.

## 📁 Project Structure

```
Inventory-Management-System/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/inventory/management/
│   │   │       ├── db/
│   │   │       │   └── MongoDBClient.java
│   │   │       ├── model/
│   │   │       │   ├── Product.java
│   │   │       │   └── User.java
│   │   │       ├── recursive/
│   │   │       │   ├── CalculationUtility.java
│   │   │       │   └── SearchUtility.java
│   │   │       ├── sorting/
│   │   │       │   └── SortingAlgorithms.java
│   │   │       ├── ui/
│   │   │       │   ├── UIManager.java
│   │   │       │   ├── MainController.java
│   │   │       │   └── LoginController.java
│   │   │       ├── utils/
│   │   │       │   ├── ChartManager.java
│   │   │       │   ├── InventoryBackupManager.java
│   │   │       │   ├── InventoryManager.java
│   │   │       │   ├── PDFReportManager.java
│   │   │       │   └── ValidationUtility.java
│   │   │       └── Main.java
│   │   └── resources/
│   │       └── com/inventory/management/ui/views/
│   │           ├── login.fxml
│   │           ├── main.fxml
│   │           └── charts.fxml
│   └── test/
├── pom.xml
└── README.md
```

## 🔑 Key Features

### Data Persistence
- All product data is automatically saved to MongoDB
- Changes are synchronized in real-time
- Backup system provides additional data security

### Security
- Password hashing using BCrypt
- Role-based access control
- Secure authentication system

### Advanced Algorithms
- **Sorting**: Bubble Sort, Merge Sort, Quick Sort implementations
- **Recursive Operations**: Calculation and search utilities
- **Filtering**: Multi-criteria filtering with price ranges and categories

### User Interface
- Clean and intuitive JavaFX interface
- Responsive design
- Real-time data updates
- Interactive charts and visualizations

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a new branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 👤 Author

**Ravindu-VS**
- GitHub: [@Ravindu-VS](https://github.com/Ravindu-VS)

## 📞 Support

For issues, questions, or suggestions, please open an issue on the GitHub repository.

---

**Note**: Make sure MongoDB is running before starting the application. The system will create sample products on first run if the database is empty.
