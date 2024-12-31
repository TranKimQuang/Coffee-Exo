package ExoCoffee.Controllers;

import ExoCoffee.Models.OrderDTO;
import ExoCoffee.Models.OrderProductDTO;
import ExoCoffee.Models.CartItem;
import ExoCoffee.Repositories.OrderRepository;
import ExoCoffee.Models.Cart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewProductsController {
  @FXML
  private TableView<OrderProductDTO> orderTable; // Hiển thị chi tiết sản phẩm trong đơn hàng
  @FXML
  private TableColumn<OrderProductDTO, Integer> orderIdColumn; // Mã đơn hàng
  @FXML
  private TableColumn<OrderProductDTO, Integer> productIdColumn; // Mã sản phẩm
  @FXML
  private TableColumn<OrderProductDTO, Integer> quantityColumn; // Số lượng
  @FXML
  private TableColumn<OrderProductDTO, Double> totalPriceColumn; // Tổng giá

  private ObservableList<OrderProductDTO> orderProductList = FXCollections.observableArrayList();
  private Cart cart = new Cart();
  private OrderRepository orderRepository = new OrderRepository();

  @FXML
  public void initialize() {
    // Liên kết cột với thuộc tính trong OrderProductDTO
    orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
    productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

    // Tải dữ liệu đơn hàng vào TableView
    orderTable.setItems(orderProductList);
    loadOrderData();
  }

  /**
   * Tải dữ liệu đơn hàng từ cơ sở dữ liệu.
   */
  private void loadOrderData() {
    try {
      List<OrderDTO> orders = orderRepository.getAllOrders();
      orderProductList.clear(); // Xóa dữ liệu cũ
      for (OrderDTO order : orders) {
        for (OrderProductDTO product : order.getOrderProducts()) {
          orderProductList.add(product); // Thêm từng sản phẩm vào danh sách hiển thị
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tải dữ liệu đơn hàng: " + e.getMessage());
    }
  }

  /**
   * Xử lý sự kiện xóa đơn hàng.
   */
  @FXML
  public void handleDeleteOrder() {
    OrderProductDTO selectedProduct = orderTable.getSelectionModel().getSelectedItem();
    if (selectedProduct == null) {
      showError("Vui lòng chọn một sản phẩm trong đơn hàng để xóa.");
      return;
    }

    try {
      orderRepository.deleteOrderProduct(selectedProduct.getOrderId(), selectedProduct.getProductId());
      orderProductList.remove(selectedProduct); // Cập nhật TableView mà không cần tải lại toàn bộ dữ liệu
      showAlert("Thành công", "Sản phẩm đã được xóa khỏi đơn hàng.");
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi xóa sản phẩm: " + e.getMessage());
    }
  }

  /**
   * Xử lý sự kiện thêm đơn hàng.
   */
  @FXML
  public void handleAddOrder() {
    if (cart.getItems().isEmpty()) {
      showError("Giỏ hàng trống. Vui lòng thêm sản phẩm vào giỏ hàng trước khi đặt hàng.");
      return;
    }

    double totalAmount = cart.getItems().stream()
        .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
        .sum();
    List<OrderProductDTO> orderProducts = new ArrayList<>();
    for (CartItem item : cart.getItems()) {
      orderProducts.add(new OrderProductDTO(item.getProduct().getProductId(), item.getQuantity()));
    }

    OrderDTO order = new OrderDTO(totalAmount, new Date(), orderProducts);

    try {
      orderRepository.addOrder(order);
      cart.clear();
      loadOrderData(); // Tải lại dữ liệu sau khi thêm đơn hàng
      showAlert("Thành công", "Đơn hàng đã được thêm.");
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi thêm đơn hàng: " + e.getMessage());
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
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Hiển thị thông báo lỗi.
   *
   * @param message Nội dung thông báo lỗi.
   */
  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Lỗi");
    alert.setContentText(message);
    alert.showAndWait();
  }
}