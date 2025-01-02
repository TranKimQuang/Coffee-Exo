package ExoCoffee.Controllers;

import ExoCoffee.Models.Cart;
import ExoCoffee.Models.CartItem;
import ExoCoffee.Models.OrderProductDTO;
import ExoCoffee.Repositories.OrderRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class CartViewController {
  @FXML
  private TableView<CartItem> cartTable;
  @FXML
  private TableColumn<CartItem, String> productNameColumn;
  @FXML
  private TableColumn<CartItem, Integer> quantityColumn;
  @FXML
  private TableColumn<CartItem, Double> priceColumn;
  @FXML
  private TableColumn<CartItem, Double> totalPriceColumn;
  @FXML
  private Label totalAmountLabel; // Label để hiển thị tổng tiền

  private OrderRepository orderRepository = new OrderRepository();
  private Cart cart = new Cart(); // Khởi tạo giỏ hàng

  @FXML
  public void initialize() {
    // Liên kết cột với thuộc tính trong CartItem
    productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

    // Tải dữ liệu giỏ hàng vào TableView
    loadCartData();
  }

  /**
   * Thiết lập giỏ hàng.
   *
   * @param cart Giỏ hàng.
   */
  public void setCart(Cart cart) {
    this.cart = cart;
    loadCartData();
  }

  /**
   * Tải dữ liệu giỏ hàng vào TableView.
   */
  private void loadCartData() {
    try {
      List<OrderProductDTO> orderProducts = orderRepository.getOrderProducts();
      cartTable.getItems().setAll(orderProducts);
    } catch (SQLException e) {
      e.printStackTrace();
      showAlert("Lỗi","Lỗi khi tải giỏ hàng: " + e.getMessage());
    }
  }

  /**
   * Cập nhật tổng tiền và hiển thị.
   */
  private void updateTotalAmount() {
    if (totalAmountLabel != null) {
      double totalAmount = cart.getItems().stream()
          .mapToDouble(CartItem::getTotalPrice)
          .sum();
      totalAmountLabel.setText(String.format("Tổng tiền: %.2f", totalAmount));
    } else {
      System.err.println("totalAmountLabel chưa được khởi tạo.");
    }
  }

  @FXML
  public void handlePlaceOrder() {
    if (cart.getItems().isEmpty()) {
      showAlert("Cảnh báo", "Giỏ hàng trống. Vui lòng thêm sản phẩm vào giỏ hàng trước khi đặt hàng.");
      return;
    }

    try {
      // Bước 1: Tạo đơn hàng mới
      double totalAmount = cart.getItems().stream()
          .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
          .sum();
      int orderId = orderRepository.addOrder(totalAmount, new Date());

      // Bước 2: Thêm các sản phẩm vào bảng order_products
      for (CartItem item : cart.getItems()) {
        orderRepository.addProductToOrder(orderId, item.getProduct().getProductId(), item.getQuantity(),);
      }

      showAlert("Thành công", "Đơn hàng đã được đặt thành công.");
      cart.clear(); // Xóa giỏ hàng sau khi đặt hàng thành công
      loadCartData(); // Cập nhật lại TableView
    } catch (SQLException e) {
      e.printStackTrace();
      showAlert("Cảnh báo", "Lỗi khi đặt hàng: " + e.getMessage());
    }
  }

  /**
   * Hiển thị thông báo.
   *
   * @param title   Tiêu đề thông báo.
   * @param message Nội dung thông báo.
   */
  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}