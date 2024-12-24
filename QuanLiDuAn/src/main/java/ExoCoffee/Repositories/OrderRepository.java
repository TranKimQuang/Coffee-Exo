package ExoCoffee.Repositories;

import ExoCoffee.Models.OrderDTO;
import ExoCoffee.Models.OrderProductDTO;
import ExoCoffee.Utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
  public List<OrderDTO> getAllOrders() throws SQLException {
    List<OrderDTO> orders = new ArrayList<>();
    String query = "SELECT o.order_id, p.product_name, op.quantity, o.total_amount " +
        "FROM orders o " +
        "JOIN order_products op ON o.order_id = op.order_id " +
        "JOIN products p ON op.product_id = p.product_id";

    try (Connection connection = DBUtils.getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(query)) {
      while (resultSet.next()) {
        int orderId = resultSet.getInt("order_id");
        String productName = resultSet.getString("product_name");
        int quantity = resultSet.getInt("quantity");
        double totalPrice = resultSet.getDouble("total_amount");

        orders.add(new OrderDTO(orderId, productName, quantity, totalPrice));
      }
    }
    return orders;
  }

  public void deleteOrder(int orderId) throws SQLException {
    String deleteOrderProductsQuery = "DELETE FROM order_products WHERE order_id = ?";
    String deleteOrderQuery = "DELETE FROM orders WHERE order_id = ?";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement deleteOrderProductsStmt = connection.prepareStatement(deleteOrderProductsQuery);
         PreparedStatement deleteOrderStmt = connection.prepareStatement(deleteOrderQuery)) {

      deleteOrderProductsStmt.setInt(1, orderId);
      deleteOrderProductsStmt.executeUpdate();

      deleteOrderStmt.setInt(1, orderId);
      deleteOrderStmt.executeUpdate();
    }
  }

  public void addOrder(OrderDTO order) throws SQLException {
    String insertOrder = "INSERT INTO orders (total_amount, order_date) VALUES (?, ?)";
    String insertOrderProduct = "INSERT INTO order_products (order_id, product_id, quantity) VALUES (?, ?, ?)";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement orderStmt = connection.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
         PreparedStatement orderProductStmt = connection.prepareStatement(insertOrderProduct)) {

      // Thêm đơn hàng
      orderStmt.setDouble(1, order.getTotalPrice());
      orderStmt.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
      orderStmt.executeUpdate();

      ResultSet generatedKeys = orderStmt.getGeneratedKeys();
      if (generatedKeys.next()) {
        int orderId = generatedKeys.getInt(1);

        // Thêm sản phẩm của đơn hàng
        for (OrderProductDTO op : order.getOrderProducts()) {
          orderProductStmt.setInt(1, orderId);
          orderProductStmt.setInt(2, op.getProductId());
          orderProductStmt.setInt(3, op.getQuantity());
          orderProductStmt.executeUpdate();
        }
      }
    }
  }
}
