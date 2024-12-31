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
        int orderId = resultSet.getInt("id");
        double totalAmount = resultSet.getDouble("total_amount");
        Date orderDate = resultSet.getDate("order_date");

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
    String query = "SELECT product_id, quantity FROM order_products WHERE order_id = ?"; // Đổi id thành product_id
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
      pstmt.setInt(1, orderId);
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          OrderProductDTO orderProduct = new OrderProductDTO();
          orderProduct.setProductId(rs.getInt("product_id")); // Đổi id thành product_id
          orderProduct.setQuantity(rs.getInt("quantity"));
          orderProducts.add(orderProduct);
        }
      }
    }
    return orderProducts;
  }

  // Thêm đơn hàng mới
  public int addOrder(OrderDTO order) throws SQLException {
    String query = "INSERT INTO orders (total_amount, order_date) VALUES (?, ?)";
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
      pstmt.setDouble(1, order.getTotalAmount());
      pstmt.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
      pstmt.executeUpdate();

      // Lấy ID của đơn hàng vừa thêm
      ResultSet rs = pstmt.getGeneratedKeys();
      if (rs.next()) {
        return rs.getInt(1);
      }
    }
    return -1; // Trả về -1 nếu thêm không thành công
  }

  // Thêm sản phẩm vào bảng order_products
  public void addProductToOrder(int orderId, int productId, int quantity) throws SQLException {
    String query = "INSERT INTO order_products (order_id, product_id, quantity) VALUES (?, ?, ?)"; // Đổi id thành product_id
    try (Connection conn = DBUtils.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
      pstmt.setInt(1, orderId);
      pstmt.setInt(2, productId);
      pstmt.setInt(3, quantity);
      pstmt.executeUpdate();
    }
  }

  // Xóa đơn hàng và các sản phẩm liên quan
  public void deleteOrder(int orderId) throws SQLException {
    String deleteOrderProductsQuery = "DELETE FROM order_products WHERE order_id = ?";
    String deleteOrderQuery = "DELETE FROM orders WHERE id = ?";

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

  // Thêm sản phẩm vào đơn hàng (bảng order_products)
  public void addOrderProduct(int orderId, int productId, int quantity) throws SQLException {
    String query = "INSERT INTO order_products (order_id, product_id, quantity) VALUES (?, ?, ?)";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

      statement.setInt(1, orderId);
      statement.setInt(2, productId);
      statement.setInt(3, quantity);
      statement.executeUpdate();
    }
  }
}