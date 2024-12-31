package ExoCoffee.Controllers;


import ExoCoffee.Models.CartItem;
import ExoCoffee.Models.ProductDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class CartViewController {
  @FXML
  private TableView<CartItem> cartTable;
  @FXML
  private TableColumn<CartItem, Integer> productIdColumn;
  @FXML
  private TableColumn<CartItem, String> nameColumn;
  @FXML
  private TableColumn<CartItem, Integer> quantityColumn;
  @FXML
  private TableColumn<CartItem, Double> priceColumn;
  @FXML
  private TableColumn<CartItem, Double> totalColumn;

  private List<CartItem> cartItems; // Danh sách sản phẩm trong giỏ hàng

  @FXML
  public void initialize() {
    // Liên kết cột với thuộc tính trong CartItemDTO
    productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

    // Tải dữ liệu giỏ hàng vào TableView
    loadCartData();
  }

  /**
   * Thiết lập danh sách sản phẩm trong giỏ hàng.
   *
   * @param cartItems Danh sách sản phẩm trong giỏ hàng.
   */
  public void setCartItems(List<CartItem> cartItems) {
    this.cartItems = cartItems;
    loadCartData();
  }

  /**
   * Tải dữ liệu giỏ hàng vào TableView.
   */
  private void loadCartData() {
    if (cartItems != null) {
      cartTable.getItems().setAll(cartItems);
    }
  }

  /**
   * Xử lý sự kiện thanh toán.
   */
  @FXML
  public void handleCheckout() {
    // Logic thanh toán
    System.out.println("Thanh toán thành công!");
    // Xóa giỏ hàng sau khi thanh toán
    cartTable.getItems().clear();
  }

  /**
   * Xử lý sự kiện quay lại giao diện đặt hàng.
   */
  @FXML
  public void handleBack() {
    // Quay lại giao diện đặt hàng
    // Ví dụ: sử dụng Navigation để chuyển scene
    System.out.println("Quay lại giao diện đặt hàng.");
  }
}
