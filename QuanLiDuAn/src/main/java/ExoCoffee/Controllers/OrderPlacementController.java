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
    productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

    productTable.setItems(productList);
    loadProductData();
  }

  private void loadProductData() {
    ProductRepository productRepository = new ProductRepository();
    try {
      productList.setAll(productRepository.getAllProducts());
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tải dữ liệu sản phẩm.");
    }
  }

  @FXML
  public void handleAddToOrder() {
    ProductDTO selectedProduct = productTable.getSelectionModel().getSelectedItem();

    if (selectedProduct == null) {
      showError("Vui lòng chọn một sản phẩm để thêm vào đơn hàng.");
      return;
    }

    // Thêm sản phẩm vào giỏ hàng với số lượng mặc định là 1
    cart.addItem(selectedProduct, 1);
    showAlert("Thành công", "Sản phẩm đã được thêm vào đơn hàng.");
  }
  @FXML
  public void handleCreateOrder() {
    // Tạo đơn hàng mới
    currentOrder = new OrderDTO(0.0, new Date(), new ArrayList<>());

    OrderRepository orderRepository = new OrderRepository();
    try {
      int orderId = orderRepository.addOrder(currentOrder);
      currentOrder.setOrderId(orderId); // Lưu ID của đơn hàng
      showAlert("Thành công", "Đơn hàng đã được tạo. Mã đơn hàng: " + orderId);
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tạo đơn hàng.");
    }
  }

  @FXML
  public void handlePlaceOrder() {
    if (cart.getItems().isEmpty()) {
      showError("Giỏ hàng trống. Vui lòng thêm sản phẩm vào giỏ hàng trước khi đặt hàng.");
      return;
    }

    // Thêm các sản phẩm trong giỏ hàng vào đơn hàng hiện tại
    OrderRepository orderRepository = new OrderRepository();
    try {
      for (CartItem item : cart.getItems()) {
        orderRepository.addProductToOrder(currentOrder.getOrderId(), item.getProduct().getId(), item.getQuantity());
      }
      showAlert("Thành công", "Sản phẩm đã được thêm vào đơn hàng.");
      cart.clear(); // Xóa giỏ hàng sau khi thêm thành công
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi thêm sản phẩm vào đơn hàng.");
    }
  }

  @FXML
  public void handleViewOrders() {
    try {
      OrderRepository orderRepository = new OrderRepository();
      List<OrderDTO> orders = orderRepository.getAllOrders();
      showAlert("Thành công", "Đã tải danh sách đơn hàng. Số lượng đơn hàng: " + orders.size());
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tải danh sách đơn hàng.");
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
    alert.setContentText(message);
    alert.showAndWait();
  }
}