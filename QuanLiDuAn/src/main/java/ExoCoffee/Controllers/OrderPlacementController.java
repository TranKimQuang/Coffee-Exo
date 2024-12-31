package ExoCoffee.Controllers;

import ExoCoffee.Models.Cart;
import ExoCoffee.Models.CartItem;
import ExoCoffee.Models.OrderDTO;
import ExoCoffee.Models.ProductDTO;
import ExoCoffee.Repositories.OrderRepository;
import ExoCoffee.Repositories.ProductRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.Date;

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

  private ObservableList<ProductDTO> productList = FXCollections.observableArrayList();
  private Cart cart = new Cart();

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

  /**
   * Tải dữ liệu sản phẩm từ cơ sở dữ liệu.
   */
  private void loadProductData() {
    ProductRepository productRepository = new ProductRepository();
    try {
      productList.setAll(productRepository.getAllProducts());
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tải dữ liệu sản phẩm: " + e.getMessage());
    }
  }

  /**
   * Xử lý sự kiện thêm sản phẩm vào giỏ hàng.
   */
  @FXML
  public void handleAddToOrder() {
    ProductDTO selectedProduct = productTable.getSelectionModel().getSelectedItem();

    if (selectedProduct == null) {
      showError("Vui lòng chọn một sản phẩm để thêm vào đơn hàng.");
      return;
    }

    // Thêm sản phẩm vào giỏ hàng với số lượng mặc định là 1
    cart.addItem(selectedProduct, 1);
    showAlert("Thành công", "Sản phẩm đã được thêm vào giỏ hàng.");

    // Tạo đơn hàng mới và lấy order_id
    try {
      OrderRepository orderRepository = new OrderRepository();
      double totalAmount = selectedProduct.getPrice() * 1; // Tính tổng tiền
      int orderId = orderRepository.addOrder(totalAmount, new Date()); // Tạo đơn hàng mới
      System.out.println("Đã tạo đơn hàng mới với orderId: " + orderId);

      // Thêm sản phẩm vào bảng order_products
      orderRepository.addProductToOrder(orderId, selectedProduct.getProductId(), 1);
      System.out.println("Đã thêm sản phẩm vào bảng order_products.");
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi thêm sản phẩm vào đơn hàng: " + e.getMessage());
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
