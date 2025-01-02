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

      statement.executeUpdate();

      System.out.println("Thêm sản phẩm thành công vào order_products với orderId: " + orderId);
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Lỗi SQL: " + e.getMessage());
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
   * Tính tổng giá trị của giỏ hàng (order_products).
   *
   * @return Tổng giá trị của giỏ hàng.
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public double calculateTotalAmount() throws SQLException {
    String query = "SELECT SUM(price * quantity) AS total_amount FROM order_products";

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
   * Lấy tất cả sản phẩm từ giỏ hàng (order_products) và thông tin sản phẩm từ bảng products.
   *
   * @return Danh sách CartItem.
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public List<CartItem> getOrderProducts(int orderId) throws SQLException {
    String query = "SELECT p.product_id, p.name, op.quantity, op.price, (op.quantity * op.price) AS total_product " +
        "FROM products p " +
        "JOIN order_products op ON p.product_id = op.product_id " +
        "WHERE op.order_id = ?";
    List<CartItem> items = new ArrayList<>();

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, orderId);
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        int productId = resultSet.getInt("product_id");
        String productName = resultSet.getString("name");
        int quantity = resultSet.getInt("quantity");
        double price = resultSet.getDouble("price");
        double totalProduct = resultSet.getDouble("total_product");

        ProductDTO product = new ProductDTO(productId, productName, price);
        CartItem item = new CartItem(product, quantity, totalProduct);
        items.add(item);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi lấy sản phẩm từ đơn hàng.", e);
    }

    return items;
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
   * Kiểm tra trạng thái thanh toán của đơn hàng.
   *
   * @param orderId ID của đơn hàng.
   * @return true nếu đơn hàng đã được thanh toán, false nếu chưa.
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public boolean isOrderPaid(int orderId) throws SQLException {
    String query = "SELECT paid FROM orders WHERE order_id = ?";
    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, orderId);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getBoolean("paid");
      } else {
        throw new SQLException("Lỗi: Không tìm thấy orderId.");
      }
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
   * Xóa các sản phẩm không liên quan đến bất kỳ đơn hàng nào.
   *
   * @throws SQLException Nếu có lỗi khi thực hiện truy vấn.
   */
  public void clearOrderProducts() throws SQLException {
    String query = "DELETE FROM order_products WHERE order_id NOT IN (SELECT order_id FROM orders)";

    try (Connection connection = DBUtils.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {
      int rowsAffected = statement.executeUpdate();
      System.out.println("Đã xoá " + rowsAffected + " hàng trong order_products không liên quan đến bất kỳ đơn hàng nào.");
    } catch (SQLException e) {
      e.printStackTrace();
      throw new SQLException("Lỗi khi xoá dữ liệu trong order_products.", e);
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
