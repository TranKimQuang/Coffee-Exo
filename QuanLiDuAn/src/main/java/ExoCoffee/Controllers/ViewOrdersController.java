package ExoCoffee.Controllers;

import ExoCoffee.Models.OrderDTO;
import ExoCoffee.Repositories.OrderRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class ViewOrdersController {
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
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
