package ExoCoffee.Repositories;

import ExoCoffee.Models.CartItem;
import ExoCoffee.Models.OrderDTO;
import ExoCoffee.Models.ProductDTO;
import ExoCoffee.Models.StatisticsDTO;
import ExoCoffee.Utils.DBUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
  // Phương thức lấy danh sách các đơn hàng đã thanh toán
  public List<OrderDTO> getPaidOrders() throws SQLException {
    String query = "SELECT order_id, order_date, total_amount FROM orders WHERE paid = true";
    List<OrderDTO> paidOrders = new ArrayList<>();

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

      while (resultSet.next()) {
        int orderId = resultSet.getInt("order_id");
        String orderDate = resultSet.getString("order_date");
        double totalAmount = resultSet.getDouble("total_amount");
        paidOrders.add(new OrderDTO(orderId, totalAmount, orderDate));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi tải dữ liệu đơn hàng đã thanh toán.", e);
    }

    return paidOrders;
  }

  // Phương thức lấy danh sách các đơn hàng đã thanh toán theo ngày
  public List<OrderDTO> getPaidOrdersByDate(LocalDate date) throws SQLException {
    String query = "SELECT order_id, order_date, total_amount FROM orders WHERE paid = true AND DATE(order_date) = ?";
    List<OrderDTO> paidOrders = new ArrayList<>();

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setDate(1, Date.valueOf(date));
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          int orderId = resultSet.getInt("order_id");
          String orderDate = resultSet.getString("order_date");
          double totalAmount = resultSet.getDouble("total_amount");
          paidOrders.add(new OrderDTO(orderId, totalAmount, orderDate));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi tải dữ liệu đơn hàng đã thanh toán theo ngày.", e);
    }

    return paidOrders;
  }

  // Phương thức lấy danh sách sản phẩm của đơn hàng dựa trên order_id
  public List<CartItem> getOrderProducts(int orderId) throws SQLException {
    String query = "SELECT op.product_id, p.name, op.quantity, op.price, (op.quantity * op.price) AS total_product " +
        "FROM order_products op " +
        "JOIN products p ON op.product_id = p.product_id " +
        "WHERE op.order_id = ?";

    List<CartItem> orderProducts = new ArrayList<>();

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, orderId);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          int productId = resultSet.getInt("product_id");
          String productName = resultSet.getString("name");
          int quantity = resultSet.getInt("quantity");
          double price = resultSet.getDouble("price");
          double totalProduct = resultSet.getDouble("total_product");

          CartItem item = new CartItem(productId, productName, quantity, price, totalProduct);
          orderProducts.add(item);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi tải dữ liệu sản phẩm của đơn hàng.", e);
    }

    return orderProducts;
  }

  // Phương thức lấy thống kê sản phẩm theo ngày
  public List<StatisticsDTO> getStatisticsByDate(LocalDate date) throws SQLException {
    String query = "SELECT op.product_id, p.product_name, SUM(op.quantity) AS total_quantity, SUM(op.price * op.quantity) AS total_price " +
        "FROM order_products op " +
        "JOIN orders o ON op.order_id = o.order_id " +
        "JOIN products p ON op.product_id = p.product_id " +
        "WHERE DATE(o.order_date) = ? AND o.paid = true " +
        "GROUP BY op.product_id, p.product_name";

    List<StatisticsDTO> statistics = new ArrayList<>();

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setDate(1, Date.valueOf(date));
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          int productId = resultSet.getInt("product_id");
          String productName = resultSet.getString("product_name");
          int totalQuantity = resultSet.getInt("total_quantity");
          double totalPrice = resultSet.getDouble("total_price");

          StatisticsDTO stat = new StatisticsDTO(productId, productName, totalQuantity, totalPrice);
          statistics.add(stat);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi tải dữ liệu thống kê.", e);
    }

    return statistics;
  }
/**
   * Lấy tất cả đơn hàng chưa thanh toán từ database.
   *
   * @return Danh sách đơn hàng chưa thanh toán.
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public List<OrderDTO> getUnpaidOrders() throws SQLException {
    String query = "SELECT order_id, total_amount, order_date FROM orders WHERE paid = false";
    List<OrderDTO> orders = new ArrayList<>();

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

      while (resultSet.next()) {
        int orderId = resultSet.getInt("order_id");
        double totalAmount = resultSet.getDouble("total_amount");
        String orderDate = resultSet.getString("order_date");

        OrderDTO order = new OrderDTO(orderId, totalAmount, orderDate);
        orders.add(order);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi lấy danh sách đơn hàng chưa thanh toán.", e);
    }
    return orders;
  }





/**
   * Thêm một đơn hàng mới vào database.
   *
   * @param totalAmount Tổng giá trị đơn hàng.
   * @param orderDate   Ngày đặt hàng.
   * @return ID của đơn hàng vừa được thêm, hoặc -1 nếu thêm không thành công.
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public int addOrder(double totalAmount, java.sql.Date orderDate) throws SQLException {
    String query = "INSERT INTO orders (total_amount, order_date, paid) VALUES (?, ?, false)";
    int orderId = -1;

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
      statement.setDouble(1, totalAmount);
      statement.setDate(2, orderDate);
      statement.executeUpdate();

      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        orderId = generatedKeys.getInt(1);
        System.out.println("Đã tạo đơn hàng với orderId: " + orderId);
      } else {
        System.err.println("Lỗi: Không thể lấy orderId.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi tạo đơn hàng.", e);
    }
    return orderId;
  }

  /**
   * Thêm sản phẩm vào đơn hàng.
   *
   * @param orderId    ID của đơn hàng.
   * @param productId  ID của sản phẩm.
   * @param quantity   Số lượng sản phẩm.
   * @param price      Giá sản phẩm.
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public void addProductToOrder(int orderId, int productId, int quantity, double price) throws SQLException {
    String query = "INSERT INTO order_products (order_id, product_id, quantity, price, total_product) VALUES (?, ?, ?, ?, ?)";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, orderId);
      statement.setInt(2, productId);
      statement.setInt(3, quantity);
      statement.setDouble(4, price);
      statement.setDouble(5, price * quantity);

      // Debug: In ra thông tin chi tiết
      System.out.println("Chuẩn bị thêm sản phẩm vào order_products với orderId: " + orderId + ", productId: " + productId + ", quantity: " + quantity + ", price: " + price);

      // Kiểm tra xem orderId có tồn tại trong bảng orders không
      if (!doesOrderExist(orderId)) {
        System.err.println("Lỗi: orderId " + orderId + " không tồn tại trong bảng orders.");
        throw new SQLException("Lỗi khi thêm sản phẩm vào đơn hàng: orderId không tồn tại.");
      }

      statement.executeUpdate();

      System.out.println("Thêm sản phẩm thành công vào order_products với orderId: " + orderId);
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Lỗi SQL: " + e.getMessage());
      throw new SQLException("Lỗi khi thêm sản phẩm vào đơn hàng.", e);
    }
  }

  private boolean doesOrderExist(int orderId) throws SQLException {
    String query = "SELECT COUNT(*) FROM orders WHERE order_id = ?";
    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, orderId);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getInt(1) > 0;
        }
      }
    }
    return false;
  }

  /**
   * Xóa một đơn hàng và các sản phẩm liên quan.
   *
   * @param orderId ID của đơn hàng.
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public void deleteOrder(int orderId) throws SQLException {
    String query = "DELETE FROM orders WHERE order_id = ?";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, orderId);
      statement.executeUpdate();
      System.out.println("Đã xóa đơn hàng với orderId: " + orderId);
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi xóa đơn hàng.", e);
    }
  }

  /**
   * Kiểm tra và reset ID đơn hàng nếu không có đơn hàng nào.
   *
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public void resetOrderIdIfEmpty() throws SQLException {
    String checkQuery = "SELECT COUNT(*) AS total FROM orders";
    String resetOrderQuery = "ALTER TABLE orders AUTO_INCREMENT = 1";
    String deleteOrderProductsQuery = "DELETE FROM order_products WHERE order_id NOT IN (SELECT order_id FROM orders)";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
         ResultSet resultSet = checkStmt.executeQuery()) {

      if (resultSet.next()) {
        int totalOrders = resultSet.getInt("total");
        if (totalOrders == 0) {
          // Không có đơn hàng nào, reset AUTO_INCREMENT và xoá dữ liệu trong order_products
          try (PreparedStatement deleteOrderProductsStmt = connection.prepareStatement(deleteOrderProductsQuery);
               PreparedStatement resetOrderStmt = connection.prepareStatement(resetOrderQuery)) {
            deleteOrderProductsStmt.executeUpdate();
            resetOrderStmt.executeUpdate();
            System.out.println("ID đơn hàng đã được reset về 1 và xoá dữ liệu không liên quan trong order_products.");
          }
        } else {
          System.out.println("Có " + totalOrders + " đơn hàng, không cần reset ID.");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi reset ID đơn hàng.", e);
    }
  }
  /**
   * Đánh dấu đơn hàng là đã thanh toán.
   *
   * @param orderId ID của đơn hàng.
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public void markOrderAsPaid(int orderId) throws SQLException {
    String query = "UPDATE orders SET paid = true WHERE order_id = ?";
    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, orderId);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi đánh dấu đơn hàng đã thanh toán.", e);
    }
  }

  /**
   * Cập nhật tổng giá trị của đơn hàng.
   *
   * @param orderId     ID của đơn hàng.
   * @param totalAmount Tổng giá trị mới của đơn hàng.
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public void updateOrderTotalAmount(int orderId, double totalAmount) throws SQLException {
    String query = "UPDATE orders SET total_amount = ? WHERE order_id = ?";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setDouble(1, totalAmount);
      statement.setInt(2, orderId);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi cập nhật tổng giá trị đơn hàng.", e);
    }
  }

}
