package ExoCoffee.Repositories;

import ExoCoffee.Models.ProductDTO;
import ExoCoffee.Utils.DBUtils;

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
    List<ProductDTO> products = new ArrayList<>();
    String query = "SELECT product_id, name, price, category FROM products"; // Đổi id thành product_id
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query);
         ResultSet rs = pstmt.executeQuery()) {
      while (rs.next()) {
        ProductDTO product = new ProductDTO();
        product.setId(rs.getInt("product_id")); // Đổi id thành product_id
        product.setName(rs.getString("name"));
        product.setPrice(rs.getDouble("price"));
        product.setCategory(rs.getString("category"));
        products.add(product);
      }
    }
    return products;
  }

  public void addProduct(ProductDTO productDTO) throws SQLException {
    String query = "INSERT INTO products (name, product_id, price, category) VALUES (?, ?, ?, ?)";
    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, productDTO.getName());
      statement.setInt(2, productDTO.getId());
      statement.setDouble(3, productDTO.getPrice());
      statement.setString(4, productDTO.getCategory());
      statement.executeUpdate();
    }
  }

  public void updateProduct(ProductDTO productDTO) throws SQLException {
    String query = "UPDATE products SET price = ?, category = ? WHERE product_id = ?";
    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setDouble(1, productDTO.getPrice());
      statement.setString(2, productDTO.getCategory());
      statement.setInt(3, productDTO.getId());
      statement.executeUpdate();
    }
  }

  public void deleteProduct(int productId) throws SQLException {
    String query = "DELETE FROM products WHERE product_id = ?";
    try (Connection connection = getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, productId);
      statement.executeUpdate();
    }
  }
}
