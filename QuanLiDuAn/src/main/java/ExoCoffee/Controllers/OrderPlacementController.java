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

  @FXML
  public void initialize() {
    productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
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
  public void handlePlaceOrder() {
    ProductDTO selectedProduct = productTable.getSelectionModel().getSelectedItem();
    if (selectedProduct != null) {
      cart.addItem(selectedProduct, 1);
      showAlert("Thành công", "Đã thêm sản phẩm vào giỏ hàng: " + selectedProduct.getName());
    } else {
      showError("Vui lòng chọn một sản phẩm để đặt hàng.");
    }
  }
  @FXML
  public void handleViewOrders() {
    try {
      // Logic để xem đơn hàng
      OrderRepository orderRepository = new OrderRepository();
      List<OrderDTO> orders = orderRepository.getAllOrders();

      // Hiển thị danh sách đơn hàng (ví dụ: trong một TableView hoặc cửa sổ mới)
      showAlert("Thành công", "Đã tải danh sách đơn hàng. Số lượng đơn hàng: " + orders.size());
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tải danh sách đơn hàng.");
    }
  }
  @FXML
  public void handleAddOrder() {
    if (cart.getItems().isEmpty()) {
      showError("Giỏ hàng trống. Vui lòng thêm sản phẩm vào giỏ hàng trước khi đặt hàng.");
      return;
    }

    double totalAmount = cart.getTotalPrice();
    List<OrderProductDTO> orderProducts = new ArrayList<>();
    for (CartItem item : cart.getItems()) {
      orderProducts.add(new OrderProductDTO(item.getProduct().getProductId(), item.getQuantity()));
    }

    OrderDTO order = new OrderDTO(totalAmount, new Date(), orderProducts);

    OrderRepository orderRepository = new OrderRepository();
    try {
      orderRepository.addOrder(order);
      cart.clear();
      showAlert("Thành công", "Đơn hàng đã được thêm.");
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi thêm đơn hàng.");
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