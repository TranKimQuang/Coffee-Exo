package ExoCoffee.Repositories;

import ExoCoffee.Models.ProductDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
  private Connection getConnection() throws SQLException {
    // Kết nối đến cơ sở dữ liệu
    String url = "jdbc:mysql://localhost:3306/coffeemanager";
    String user = "root";
    String password = "";
    return DriverManager.getConnection(url, user, password);
  }

  public List<ProductDTO> getAllProducts() throws SQLException {
    String query = "SELECT * FROM products";
    List<ProductDTO> productDTOS = new ArrayList<>();
    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        ProductDTO productDTO = new ProductDTO(
            resultSet.getInt("id"),
            resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    resultSet.getString("category")
                );
        productDTOS.add(productDTO);
      }
    }
    return productDTOS;
  }

  public void addProduct(ProductDTO productDTO) throws SQLException {
    String query = "INSERT INTO products (name, id, price, category) VALUES (?, ?, ?, ?)";
    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, productDTO.getName());
      statement.setInt(2, productDTO.getProductId());
      statement.setDouble(3, productDTO.getPrice());
      statement.setString(4, productDTO.getCategory());
      statement.executeUpdate();
    }
  }

  public void updateProduct(ProductDTO productDTO) throws SQLException {
    String query = "UPDATE products SET price = ?, category = ? WHERE id = ?";
    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setDouble(1, productDTO.getPrice());
      statement.setString(2, productDTO.getCategory());
      statement.setInt(3, productDTO.getProductId());
      statement.executeUpdate();
    }
  }

  public void deleteProduct(int productId) throws SQLException {
    String query = "DELETE FROM products WHERE id = ?";
    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, productId);
      statement.executeUpdate();
    }
  }
}
