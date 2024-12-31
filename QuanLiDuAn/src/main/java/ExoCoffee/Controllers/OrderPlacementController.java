package ExoCoffee.Controllers;

import ExoCoffee.Models.Cart;
import ExoCoffee.Models.CartItem;
import ExoCoffee.Models.OrderDTO;
import ExoCoffee.Models.ProductDTO;
import ExoCoffee.Models.OrderProductDTO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
  private OrderDTO currentOrder; // Đơn hàng hiện tại

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
  }

  /**
   * Xử lý sự kiện tạo đơn hàng mới.
   */
  @FXML
  public void handleCreateOrder() {
    // Tạo đơn hàng mới với tổng giá trị ban đầu là 0
    currentOrder = new OrderDTO(0.0, new Date(), new ArrayList<>());

    OrderRepository orderRepository = new OrderRepository();
    try {
      int orderId = orderRepository.addOrder(currentOrder);
      currentOrder.setOrderId(orderId); // Lưu ID của đơn hàng
      showAlert("Thành công", "Đơn hàng đã được tạo. Mã đơn hàng: " + orderId);
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tạo đơn hàng: " + e.getMessage());
    }
  }

  /**
   * Xử lý sự kiện đặt hàng.
   */
  @FXML
  public void handlePlaceOrder() {
    if (currentOrder == null) {
      showError("Vui lòng tạo đơn hàng trước khi đặt hàng.");
      return;
    }

    if (cart.getItems().isEmpty()) {
      showError("Giỏ hàng trống. Vui lòng thêm sản phẩm vào giỏ hàng trước khi đặt hàng.");
      return;
    }

    // Tính tổng giá trị đơn hàng
    double totalAmount = cart.getItems().stream()
        .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
        .sum();
    currentOrder.setTotalAmount(totalAmount);

    // Thêm các sản phẩm trong giỏ hàng vào đơn hàng hiện tại
    OrderRepository orderRepository = new OrderRepository();
    try {
      // Cập nhật tổng giá trị đơn hàng trong cơ sở dữ liệu
      orderRepository.updateOrderTotalAmount(currentOrder.getOrderId(), totalAmount);

      // Thêm các sản phẩm vào đơn hàng
      for (CartItem item : cart.getItems()) {
        orderRepository.addProductToOrder(currentOrder.getOrderId(), item.getProduct().getProductId(), item.getQuantity());
      }

      showAlert("Thành công", "Đơn hàng đã được đặt thành công.");
      cart.clear(); // Xóa giỏ hàng sau khi đặt hàng thành công
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi đặt hàng: " + e.getMessage());
    }
  }

  /**
   * Xử lý sự kiện xem danh sách đơn hàng.
   */
  @FXML
  public void handleViewOrders() {
    try {
      OrderRepository orderRepository = new OrderRepository();
      List<OrderDTO> orders = orderRepository.getAllOrders();
      showAlert("Thành công", "Đã tải danh sách đơn hàng. Số lượng đơn hàng: " + orders.size());
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tải danh sách đơn hàng: " + e.getMessage());
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