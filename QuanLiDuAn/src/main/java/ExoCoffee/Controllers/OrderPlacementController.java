package ExoCoffee.Controllers;

import ExoCoffee.App;
import ExoCoffee.Models.ProductDTO;
import ExoCoffee.Repositories.ProductRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
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

  private ObservableList<ProductDTO> productList = FXCollections.observableArrayList();

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
    }
  }

  @FXML
  public void handlePlaceOrder() {
    // Xử lý đặt hàng (Ví dụ: thêm sản phẩm vào giỏ hàng)
  }

  @FXML
  public void handleViewOrders() {
    App.setRoot("view_orders"); // Chuyển đến giao diện xem hàng đã đặt
  }
}
