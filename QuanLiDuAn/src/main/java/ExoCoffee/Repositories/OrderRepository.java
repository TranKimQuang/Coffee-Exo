package ExoCoffee.Repositories;

import ExoCoffee.Models.OrderDTO;
import ExoCoffee.Models.OrderProductDTO;
import ExoCoffee.Models.ProductDTO;
import ExoCoffee.Utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

  // Lấy tất cả đơn hàng
  public List<OrderDTO> getAllOrders() throws SQLException {
    List<OrderDTO> orders = new ArrayList<>();
    String query = "SELECT * FROM orders";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

      while (resultSet.next()) {
        int orderId = resultSet.getInt("orderId");
        double totalAmount = resultSet.getDouble("totalAmount");
        Date orderDate = resultSet.getDate("orderDate");

        // Lấy danh sách sản phẩm trong đơn hàng
        List<OrderProductDTO> orderProducts = getOrderProductsByOrderId(orderId);

        // Tạo đối tượng OrderDTO và thêm vào danh sách
        orders.add(new OrderDTO(orderId, totalAmount, orderDate, orderProducts));
      }
    }
    return orders;
  }

  // Lấy danh sách sản phẩm trong đơn hàng
  private List<OrderProductDTO> getOrderProductsByOrderId(int orderId) throws SQLException {
    List<OrderProductDTO> orderProducts = new ArrayList<>();
    String query = "SELECT op.productId, op.quantity, p.name, p.price, p.category " +
        "FROM order_products op " +
        "JOIN products p ON op.productId = p.productId " +
        "WHERE op.orderId = ?";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

      statement.setInt(1, orderId);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          int productId = resultSet.getInt("productId");
          int quantity = resultSet.getInt("quantity");
          String name = resultSet.getString("name");
          double price = resultSet.getDouble("price");
          String category = resultSet.getString("category");

          // Tạo đối tượng ProductDTO
          ProductDTO product = new ProductDTO(productId, name, price, category);

          // Tạo đối tượng OrderProductDTO và thêm vào danh sách
          orderProducts.add(new OrderProductDTO(orderId, product, quantity));
        }
      }
    }
    return orderProducts;
  }

  // Thêm đơn hàng mới
  public int addOrder(OrderDTO order) throws SQLException {
    String query = "INSERT INTO orders (totalAmount, orderDate) VALUES (?, ?)";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

      statement.setDouble(1, order.getTotalAmount());
      statement.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
      statement.executeUpdate();

      // Lấy ID của đơn hàng vừa thêm
      try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          int orderId = generatedKeys.getInt(1);

          // Thêm các sản phẩm vào bảng order_products
          addOrderProducts(orderId, order.getOrderProducts());
          return orderId;
        }
      }
    }
    return -1; // Nếu không tạo được đơn hàng mới
  }

  // Thêm sản phẩm vào bảng order_products
  public void addOrderProducts(int orderId, List<OrderProductDTO> orderProducts) throws SQLException {
    String query = "INSERT INTO order_products (orderId, productId, quantity) VALUES (?, ?, ?)";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

      for (OrderProductDTO orderProduct : orderProducts) {
        statement.setInt(1, orderId);
        statement.setInt(2, orderProduct.getProductId());
        statement.setInt(3, orderProduct.getQuantity());
        statement.addBatch();
      }
      statement.executeBatch();
    }
  }
  public void deleteOrder(int orderId) throws SQLException {
    String deleteOrderProductsQuery = "DELETE FROM order_products WHERE orderId = ?";
    String deleteOrderQuery = "DELETE FROM orders WHERE orderId = ?";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement deleteOrderProductsStmt = connection.prepareStatement(deleteOrderProductsQuery);
         PreparedStatement deleteOrderStmt = connection.prepareStatement(deleteOrderQuery)) {

      // Xóa các sản phẩm liên quan đến đơn hàng
      deleteOrderProductsStmt.setInt(1, orderId);
      deleteOrderProductsStmt.executeUpdate();

      // Xóa đơn hàng
      deleteOrderStmt.setInt(1, orderId);
      deleteOrderStmt.executeUpdate();
    }
  }
  public void addOrderProduct(int orderId, int productId, int quantity) throws SQLException {
    String query = "INSERT INTO order_products (orderId, productId, quantity) VALUES (?, ?, ?)";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

      statement.setInt(1, orderId);
      statement.setInt(2, productId);
      statement.setInt(3, quantity);
      statement.executeUpdate();
    }
  }

}