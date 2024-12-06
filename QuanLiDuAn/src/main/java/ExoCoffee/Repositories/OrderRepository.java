package ExoCoffee.Repositories;

import ExoCoffee.Models.OrderDTO;
import ExoCoffee.Utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
  public List<OrderDTO> getAllOrders() throws SQLException {
    List<OrderDTO> orders = new ArrayList<>();
    String query = "SELECT * FROM orders";

    try (Connection connection = DBUtils.getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(query)) {
      while (resultSet.next()) {
        int orderId = resultSet.getInt("order_id");
        String productName = resultSet.getString("product_name");
        int quantity = resultSet.getInt("quantity");
        double totalPrice = resultSet.getDouble("total_price");

        orders.add(new OrderDTO(orderId, productName, quantity, totalPrice));
      }
    }
    return orders;
  }

  public void deleteOrder(int orderId) throws SQLException {
    String query = "DELETE FROM orders WHERE order_id = ?";
    try (Connection connection = DBUtils.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      preparedStatement.setInt(1, orderId);
      preparedStatement.executeUpdate();
    }
  }
}
