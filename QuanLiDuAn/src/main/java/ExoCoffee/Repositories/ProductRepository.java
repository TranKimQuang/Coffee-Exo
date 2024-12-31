package ExoCoffee.Repositories;

import ExoCoffee.Models.ProductDTO;
import ExoCoffee.Utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

  // Lấy tất cả sản phẩm
  public List<ProductDTO> getAllProducts() throws SQLException {
    List<ProductDTO> products = new ArrayList<>();
    String query = "SELECT * FROM products";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

      while (resultSet.next()) {
        int productId = resultSet.getInt("productId");
        String name = resultSet.getString("name");
        double price = resultSet.getDouble("price");
        String category = resultSet.getString("category");

        // Tạo đối tượng ProductDTO và thêm vào danh sách
        products.add(new ProductDTO(productId, name, price, category));
      }
    }
    return products;
  }
}