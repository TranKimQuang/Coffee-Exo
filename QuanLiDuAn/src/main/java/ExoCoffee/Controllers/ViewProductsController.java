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
  private TableView<OrderDTO> orderTable;
  @FXML
  private TableColumn<OrderDTO, Integer> orderIdColumn;
  @FXML
  private TableColumn<OrderDTO, String> productNameColumn;
  @FXML
  private TableColumn<OrderDTO, Integer> quantityColumn;
  @FXML
  private TableColumn<OrderDTO, Double> totalPriceColumn;

  private ObservableList<OrderDTO> orderList = FXCollections.observableArrayList();
  private Cart cart = new Cart();

  @FXML
  public void initialize() {
    orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
    productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
    quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

    orderTable.setItems(orderList);
    loadOrderData();
  }

  private void loadOrderData() {
    OrderRepository orderRepository = new OrderRepository();
    try {
      orderList.setAll(orderRepository.getAllOrders());
    } catch (SQLException e) {
      e.printStackTrace();
      showError("Lỗi khi tải dữ liệu đơn hàng.");
    }
  }

  @FXML
  public void handleDeleteOrder() {
    OrderDTO selectedOrder = orderTable.getSelectionModel().getSelectedItem();
    if (selectedOrder != null) {
      OrderRepository orderRepository = new OrderRepository();
      try {
        orderRepository.deleteOrder(selectedOrder.getOrderId());
        loadOrderData(); // Tải lại dữ liệu sau khi xóa
        showAlert("Thành công", "Đơn hàng đã được xóa.");
      } catch (SQLException e) {
        e.printStackTrace();
        showError("Lỗi khi xóa đơn hàng.");
      }
    } else {
      showError("Vui lòng chọn một đơn hàng để xóa.");
    }
  }

  @FXML
  public void handleAddOrder() {
    if (cart.getItems().isEmpty()) {
      showError("Giỏ hàng trống. Vui lòng thêm sản phẩm vào giỏ hàng trước khi đặt hàng.");
      return;
    }

    double totalAmount = cart.getItems().stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
    List<OrderProductDTO> orderProducts = new ArrayList<>();
    for (CartItem item : cart.getItems()) {
      orderProducts.add(new OrderProductDTO(item.getProduct().getProductId(), item.getQuantity()));
    }

    OrderDTO order = new OrderDTO(totalAmount, new Date(), orderProducts);

    OrderRepository orderRepository = new OrderRepository();
    try {
      orderRepository.addOrder(order);
      cart.clear();
      loadOrderData(); // Tải lại dữ liệu sau khi thêm đơn hàng
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
