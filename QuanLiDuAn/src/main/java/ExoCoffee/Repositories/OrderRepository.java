package ExoCoffee.Repositories;

import ExoCoffee.Models.OrderDTO;
import ExoCoffee.Models.CartItem;
import ExoCoffee.Models.ProductDTO;
import ExoCoffee.Utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

  /**
   * Lấy tất cả đơn hàng từ database.
   *
   * @return Danh sách đơn hàng.
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public List<OrderDTO> getAllOrders() throws SQLException {
    List<OrderDTO> orders = new ArrayList<>();
    String query = "SELECT order_id, total_amount, order_date FROM orders";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

      while (resultSet.next()) {
        int orderId = resultSet.getInt("order_id");
        double totalAmount = resultSet.getDouble("total_amount");
        Date orderDate = resultSet.getDate("order_date");
        OrderDTO order = new OrderDTO(orderId, totalAmount, orderDate);
        orders.add(order);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi lấy đơn hàng từ database.", e);
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
  public int addOrder(double totalAmount, java.util.Date orderDate) throws SQLException {
    if (orderDate == null) {
      throw new IllegalArgumentException("Ngày đặt hàng không được để trống.");
    }

    java.sql.Date sqlDate = new java.sql.Date(orderDate.getTime());
    String query = "INSERT INTO orders (total_amount, order_date) VALUES (?, ?)";

    try (Connection conn = DBUtils.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

      pstmt.setDouble(1, totalAmount);
      pstmt.setDate(2, sqlDate);
      pstmt.executeUpdate();

      try (ResultSet rs = pstmt.getGeneratedKeys()) {
        if (rs.next()) {
          System.out.println("Order ID created: " + rs.getInt(1)); // Debug log
          return rs.getInt(1); // Trả về order_id vừa được tạo
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi thêm đơn hàng vào database.", e);
    }
    return -1; // Trả về -1 nếu thêm không thành công
  }

  /**
   * Thêm một sản phẩm vào đơn hàng.
   *
   * @param orderId   ID của đơn hàng.
   * @param productId ID của sản phẩm.
   * @param quantity  Số lượng sản phẩm.
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public void addProductToOrder(int orderId, int productId, int quantity) throws SQLException {
    String query = "INSERT INTO order_products (order_id, product_id, quantity) VALUES (?, ?, ?)";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

      statement.setInt(1, orderId);
      statement.setInt(2, productId);
      statement.setInt(3, quantity);
      statement.executeUpdate();
      System.out.println("Product added to order: " + productId + " in order: " + orderId); // Debug log
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi thêm sản phẩm vào đơn hàng.", e);
    }
  }

  /**
   * Xóa một đơn hàng và các sản phẩm liên quan.
   *
   * @param orderId ID của đơn hàng.
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
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
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi xóa đơn hàng.", e);
    }
  }

  /**
   * Tính tổng giá trị của giỏ hàng (order_products).
   *
   * @return Tổng giá trị của giỏ hàng.
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public double calculateTotalAmount() throws SQLException {
    String query = "SELECT SUM(p.price * op.quantity) AS total_amount " +
        "FROM order_products op " +
        "JOIN products p ON op.product_id = p.product_id";

    try (Connection conn = DBUtils.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query);
         ResultSet rs = pstmt.executeQuery()) {

      if (rs.next()) {
        return rs.getDouble("total_amount");
      }
    }
    return 0.0; // Trả về 0 nếu không có sản phẩm
  }

  /**
   * Xóa tất cả sản phẩm trong bảng order_products.
   *
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public void clearOrderProducts() throws SQLException {
    String query = "DELETE FROM order_products";

    try (Connection conn = DBUtils.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
      int rowsAffected = pstmt.executeUpdate();
      System.out.println("Number of products deleted: " + rowsAffected); // Debug log
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi xóa sản phẩm trong order_products.", e);
    }
  }

  /**
   * Lấy tất cả sản phẩm từ giỏ hàng (order_products) và thông tin sản phẩm từ bảng products.
   *
   * @return Danh sách CartItem.
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public List<CartItem> getOrderProducts(int orderId) throws SQLException {
    List<CartItem> cartItems = new ArrayList<>();
    String query = "SELECT op.order_id, op.product_id, op.quantity, p.name AS product_name, p.price " +
        "FROM order_products op " +
        "JOIN products p ON op.product_id = p.product_id " +
        "WHERE op.order_id = ?";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, orderId);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        int productId = resultSet.getInt("product_id");
        int quantity = resultSet.getInt("quantity");
        String productName = resultSet.getString("product_name");
        double price = resultSet.getDouble("price");
        String category =resultSet.getString("category");
        double totalPrice = price * quantity;

        ProductDTO product = new ProductDTO(productId, productName, price, category);
        CartItem cartItem = new CartItem(product, quantity,totalPrice);
        cartItems.add(cartItem);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi lấy sản phẩm từ order_products.", e);
    }
    return cartItems;
  }
}
