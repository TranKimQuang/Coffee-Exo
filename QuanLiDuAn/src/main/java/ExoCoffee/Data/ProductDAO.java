package ExoCoffee.Data;

import ExoCoffee.Models.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
  private Connection getConnection() throws SQLException {
    // Kết nối đến cơ sở dữ liệu
    String url = "jdbc:mysql://localhost:3306/coffeemanager";
    String user = "root";
    String password = "";
    return DriverManager.getConnection(url, user, password);
  }

  public List<Product> getAllProducts() throws SQLException {
    String query = "SELECT * FROM products";
    List<Product> products = new ArrayList<>();
    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        Product product = new Product(
            resultSet.getString("name"),
            resultSet.getInt("productId"),
            resultSet.getDouble("price"),
            resultSet.getString("category")
        );
        products.add(product);
      }
    }
    return products;
  }

  public void addProduct(Product product) throws SQLException {
    String query = "INSERT INTO products (name, productId, price, category) VALUES (?, ?, ?, ?)";
    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, product.getName());
      statement.setInt(2, product.getProductId());
      statement.setDouble(3, product.getPrice());
      statement.setString(4, product.getCategory());
      statement.executeUpdate();
    }
  }

  public void updateProduct(Product product) throws SQLException {
    String query = "UPDATE products SET price = ?, category = ? WHERE productId = ?";
    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setDouble(1, product.getPrice());
      statement.setString(2, product.getCategory());
      statement.setInt(3, product.getProductId());
      statement.executeUpdate();
    }
  }

  public void deleteProduct(int productId) throws SQLException {
    String query = "DELETE FROM products WHERE productId = ?";
    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, productId);
      statement.executeUpdate();
    }
  }
}
