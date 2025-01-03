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
        product.setProductId(rs.getInt("product_id")); // Đổi id thành product_id
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
      statement.setInt(2, productDTO.getProductId());
      statement.setDouble(3, productDTO.getPrice());
      statement.setString(4, productDTO.getCategory());
      statement.executeUpdate();
    }
  }

  public void updateProduct(ProductDTO product) throws SQLException {
    String query = "UPDATE products SET name = ?, price = ?, category = ? WHERE product_id = ?";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, product.getName());
      statement.setDouble(2, product.getPrice());
      statement.setString(3, product.getCategory());
      statement.setInt(4, product.getProductId());

      System.out.println("Chuẩn bị cập nhật sản phẩm với productId: " + product.getProductId() + ", name: " + product.getName() + ", price: " + product.getPrice() + ", category: " + product.getCategory());

      int rowsUpdated = statement.executeUpdate();
      if (rowsUpdated > 0) {
        System.out.println("Cập nhật sản phẩm thành công.");
      } else {
        System.out.println("Không có sản phẩm nào được cập nhật.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Lỗi SQL: " + e.getMessage());
      throw new SQLException("Lỗi khi cập nhật sản phẩm.", e);
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
