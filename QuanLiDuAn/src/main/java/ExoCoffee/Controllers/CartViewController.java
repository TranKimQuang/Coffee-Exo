package ExoCoffee.Controllers;

import ExoCoffee.Models.*;
import ExoCoffee.Repositories.OrderRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
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
  private TableColumn<CartItem, Double> totalProductColumn;
  @FXML
  private Label totalAmountLabel;

  private OrderRepository orderRepository = new OrderRepository();

  @FXML
  public void initialize() {
    System.out.println("Đang khởi tạo CartViewController...");

    // Liên kết cột với thuộc tính trong CartItem
    productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
    System.out.println("Debug: Đã liên kết productNameColumn với thuộc tính 'productName'.");

    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    totalProductColumn.setCellValueFactory(new PropertyValueFactory<>("totalProduct"));

    // Tải dữ liệu giỏ hàng vào TableView
    loadCartData();
  }

  private void loadCartData() {
    int currentOrderId = OrderState.getCurrentOrderId();
    if (currentOrderId == -1) {
      // Kiểm tra nếu chưa có đơn hàng hiện tại
      List<OrderDTO> unpaidOrders;
      try {
        unpaidOrders = orderRepository.getUnpaidOrders();
        if (!unpaidOrders.isEmpty()) {
          // Sử dụng đơn hàng chưa thanh toán hiện tại
          currentOrderId = unpaidOrders.get(0).getOrderId();
          OrderState.setCurrentOrderId(currentOrderId);
        } else {
          // Tạo đơn hàng mới nếu không có đơn hàng chưa thanh toán
          orderRepository.resetOrderIdIfEmpty();
          currentOrderId = orderRepository.addOrder(0.0, new java.sql.Date(new java.util.Date().getTime()));
          OrderState.setCurrentOrderId(currentOrderId);
          System.out.println("Đã tạo đơn hàng mới với orderId: " + currentOrderId);
        }
      } catch (SQLException e) {
        e.printStackTrace();
        showError("Lỗi khi tải hoặc tạo đơn hàng: " + e.getMessage());
        return;
      }
    } else {
      // In ra thông tin orderId hiện tại
      System.out.println("currentOrderId khi load dữ liệu giỏ hàng: " + currentOrderId);
    }

    try {
      System.out.println("Đang tải dữ liệu giỏ hàng cho orderId: " + currentOrderId);
      List<CartItem> cartItems = orderRepository.getOrderProducts(currentOrderId);

      // Debug: In ra danh sách sản phẩm trong giỏ hàng
      System.out.println("Nội dung giỏ hàng:");
      for (CartItem item : cartItems) {
        System.out.println("Sản phẩm: " + item.getProductName() + ", Số lượng: " + item.getQuantity() + ", Giá: " + item.getPrice() + ", Tổng giá: " + item.getTotalProduct());
      }

      cartTable.getItems().setAll(cartItems);
      updateTotalAmount(); // Cập nhật tổng tiền
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tải giỏ hàng: " + e.getMessage());
    }
  }

  private void updateTotalAmount() {
    if (totalAmountLabel != null) {
      double totalAmount = cartTable.getItems().stream()
          .mapToDouble(CartItem::getTotalProduct)
          .sum();
      totalAmountLabel.setText(String.format("Tổng tiền: %.2f", totalAmount));
    } else {
      System.err.println("totalAmountLabel chưa được khởi tạo.");
    }
  }

  @FXML
  public void handleCreateOrder() {
    // Lấy giỏ hàng từ CartManager
    Cart cart = CartManager.getCart();

    // Debug: Kiểm tra nội dung giỏ hàng
    System.out.println("Nội dung giỏ hàng khi nhấn nút tạo đơn:");
    for (CartItem item : cart.getItems()) {
      System.out.println("Sản phẩm: " + item.getProductName() + ", Số lượng: " + item.getQuantity() + ", Giá: " + item.getPrice() + ", Tổng giá: " + item.getTotalProduct());
    }

    if (cart.getItems().isEmpty()) {
      showError("Giỏ hàng trống. Vui lòng thêm sản phẩm vào giỏ hàng trước khi tạo đơn.");
      return;
    }

    try {
      double totalAmount = cart.getItems().stream()
          .mapToDouble(CartItem::getTotalProduct)
          .sum();
      System.out.println("Tổng giá trị đơn hàng: " + totalAmount);

      int currentOrderId = OrderState.getCurrentOrderId();

      // Cập nhật tổng giá trị đơn hàng hiện tại
      System.out.println("currentOrderId trước khi cập nhật tổng số tiền: " + currentOrderId);
      orderRepository.updateOrderTotalAmount(currentOrderId, totalAmount);
      System.out.println("Đã cập nhật tổng giá trị đơn hàng: " + currentOrderId);


      showAlert("Thành công", "Đơn hàng đã được tạo thành công.");
      cart.clear(); // Xóa giỏ hàng sau khi tạo đơn hàng thành công
      System.out.println("Đã xóa giỏ hàng.");

      // Tạo một đơn hàng mới và thiết lập currentOrderId
      currentOrderId = orderRepository.addOrder(0.0, new java.sql.Date(new java.util.Date().getTime()));
      OrderState.setCurrentOrderId(currentOrderId);
      System.out.println("Đã tạo đơn hàng mới với orderId: " + currentOrderId);

      // Tải lại dữ liệu giỏ hàng cho đơn hàng mới
      loadCartData();
      System.out.println("currentOrderId sau khi tạo đơn hàng mới: " + currentOrderId);

    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tạo đơn hàng: " + e.getMessage());
    }
  }

  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Lỗi");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
