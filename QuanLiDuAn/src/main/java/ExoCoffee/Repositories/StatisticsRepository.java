package ExoCoffee.Repositories;

import ExoCoffee.Models.StatisticsDTO;
import ExoCoffee.Utils.DBUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatisticsRepository {
  public List<StatisticsDTO> getStatisticsByDate(LocalDate date) throws SQLException {
    String query = "SELECT product_id, SUM(quantity) AS total_quantity, SUM(price * quantity) AS total_price " +
        "FROM order_products op " +
        "JOIN orders o ON op.order_id = o.order_id " +
        "WHERE DATE(o.order_date) = ? AND o.paid = true " +
        "GROUP BY product_id";

    List<StatisticsDTO> statistics = new ArrayList<>();

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setDate(1, Date.valueOf(date));
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          int productId = resultSet.getInt("product_id");
          int totalQuantity = resultSet.getInt("total_quantity");
          double totalPrice = resultSet.getDouble("total_price");
          statistics.add(new StatisticsDTO(productId, totalQuantity, totalPrice));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi tải dữ liệu thống kê.", e);
    }

    return statistics;
  }
}
