package ExoCoffee.Controllers;

import ExoCoffee.Models.Cart;
import ExoCoffee.Models.CartItem;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

  private Cart cart; // Giỏ hàng

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
    if (cart != null) {
      cartTable.getItems().setAll(cart.getItems());
    }
  }

  /**
   * Xử lý sự kiện thanh toán.
   */
  @FXML
  public void handleCheckout() {
    if (cart.getItems().isEmpty()) {
      showAlert("Giỏ hàng trống", "Vui lòng thêm sản phẩm vào giỏ hàng trước khi thanh toán.");
      return;
    }

    // Logic thanh toán (ví dụ: gọi API hoặc cập nhật cơ sở dữ liệu)
    showAlert("Thành công", "Thanh toán thành công!");
    cart.clear(); // Xóa giỏ hàng sau khi thanh toán
    loadCartData(); // Cập nhật lại TableView
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