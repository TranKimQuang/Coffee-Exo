package ExoCoffee.Controllers;

import ExoCoffee.Models.*;
import ExoCoffee.Repositories.OrderRepository;
import ExoCoffee.Repositories.ProductRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class OrderPlacementController {
  @FXML
  private TableView<ProductDTO> productTable;
  @FXML
  private TableColumn<ProductDTO, Integer> productIdColumn;
  @FXML
  private TableColumn<ProductDTO, String> nameColumn;
  @FXML
  private TableColumn<ProductDTO, Double> priceColumn;
  @FXML
  private TableColumn<ProductDTO, String> categoryColumn;
  @FXML
  private TextField quantityField;

  private ObservableList<ProductDTO> productList = FXCollections.observableArrayList();
  private Cart cart = new Cart();
  private OrderRepository orderRepository = new OrderRepository();

  @FXML
  public void initialize() {
    // Liên kết cột với thuộc tính trong ProductDTO
    productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

    // Tải dữ liệu sản phẩm vào TableView
    productTable.setItems(productList);
    loadProductData();
  }

  private void loadProductData() {
    ProductRepository productRepository = new ProductRepository();
    try {
      productList.setAll(productRepository.getAllProducts());
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tải dữ liệu sản phẩm: " + e.getMessage());
    }
  }

  @FXML
  public void handleAddToOrder() {
    ProductDTO selectedProduct = productTable.getSelectionModel().getSelectedItem();
    if (selectedProduct == null) {
      showError("Vui lòng chọn một sản phẩm để thêm vào đơn hàng.");
      return;
    }

    // Lấy giá trị số lượng từ quantityField
    int quantity;
    try {
      quantity = Integer.parseInt(quantityField.getText());
      if (quantity <= 0) {
        showError("Vui lòng nhập số lượng hợp lệ (lớn hơn 0).");
        return;
      }
    } catch (NumberFormatException e) {
      showError("Vui lòng nhập số lượng hợp lệ.");
      return;
    }

    // Lấy giỏ hàng từ CartManager
    Cart cart = CartManager.getCart();

    // Thêm sản phẩm vào giỏ hàng với số lượng từ quantityField
    cart.addItem(selectedProduct, quantity);
    showAlert("Thành công", "Sản phẩm đã được thêm vào giỏ hàng.");

    // Debug: Kiểm tra nội dung giỏ hàng sau khi thêm sản phẩm
    System.out.println("Nội dung giỏ hàng sau khi thêm sản phẩm:");
    for (CartItem item : cart.getItems()) {
      System.out.println("Sản phẩm: " + item.getProductName() + ", Số lượng: " + item.getQuantity() + ", Giá: " + item.getPrice() + ", Tổng giá: " + item.getTotalProduct());
    }

    try {
      int currentOrderId = OrderState.getCurrentOrderId();
      if (currentOrderId == -1) {
        // Nếu chưa có đơn hàng, kiểm tra và reset ID đơn hàng nếu cần
        System.out.println("currentOrderId hiện tại là -1. Tiến hành reset ID đơn hàng nếu cần.");
        orderRepository.resetOrderIdIfEmpty();

        // Tạo đơn hàng mới
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        currentOrderId = orderRepository.addOrder(0.0, sqlDate);
        OrderState.setCurrentOrderId(currentOrderId);
        System.out.println("Đã tạo đơn hàng mới với orderId: " + currentOrderId);
      }

      // Đảm bảo rằng đơn hàng tồn tại trước khi thêm sản phẩm vào bảng order_products
      if (currentOrderId != -1) {
        System.out.println("Thêm sản phẩm vào order_products với orderId: " + currentOrderId);
        orderRepository.addProductToOrder(currentOrderId, selectedProduct.getProductId(), quantity, selectedProduct.getPrice());
        System.out.println("Đã thêm sản phẩm vào bảng order_products.");
      } else {
        System.err.println("Lỗi: Không thể thêm sản phẩm, orderId không hợp lệ.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi thêm sản phẩm vào đơn hàng: " + e.getMessage());
    }
  }

  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Lỗi");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
