package ExoCoffee.Controllers;

import ExoCoffee.Models.ProductDTO;
import ExoCoffee.Repositories.ProductRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.sql.SQLException;

public class ProductManagementController {
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
  private TextField productIdField;
  @FXML
  private TextField nameField;
  @FXML
  private TextField priceField;
  @FXML
  private TextField categoryField;
  @FXML
  private HBox hbox;

  private ObservableList<ProductDTO> productDTOList;

  @FXML
  public void initialize() {
    productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

    productDTOList = FXCollections.observableArrayList();
    productTable.setItems(productDTOList);

    loadProductData();

    // Thêm sự kiện khi click vào một sản phẩm trong bảng
    productTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> loadProductDetails(newValue));

    // Thiết lập Hgrow cho các thành phần trong HBox
    HBox.setHgrow(productIdField, Priority.ALWAYS);
    HBox.setHgrow(nameField, Priority.ALWAYS);
    HBox.setHgrow(priceField, Priority.ALWAYS);
    HBox.setHgrow(categoryField, Priority.ALWAYS);
  }

  private void loadProductData() {
    ProductRepository productRepository = new ProductRepository();
    try {
      productDTOList.setAll(productRepository.getAllProducts());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void loadProductDetails(ProductDTO product) {
    if (product != null) {
      productIdField.setText(String.valueOf(product.getProductId()));
      nameField.setText(product.getName());
      priceField.setText(String.valueOf(product.getPrice()));
      categoryField.setText(product.getCategory());
    } else {
      clearFields();
    }
  }

  @FXML
  private void handleAddProduct() {
    String name = nameField.getText();
    int productId = Integer.parseInt(productIdField.getText());
    double price = Double.parseDouble(priceField.getText());
    String category = categoryField.getText();

    ProductDTO productDTO = new ProductDTO(name, productId, price, category);
    ProductRepository productRepository = new ProductRepository();
    try {
      productRepository.addProduct(productDTO);
      productDTOList.add(productDTO);
      clearFields();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleUpdateProduct() {
    String name = nameField.getText();
    int productId = Integer.parseInt(productIdField.getText());
    double price = Double.parseDouble(priceField.getText());
    String category = categoryField.getText();

    ProductDTO productDTO = new ProductDTO(name, productId, price, category);
    ProductRepository productRepository = new ProductRepository();
    try {
      productRepository.updateProduct(productDTO);
      loadProductData();
      clearFields();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleDeleteProduct() {
    ProductDTO selectedProduct = productTable.getSelectionModel().getSelectedItem();
    if (selectedProduct != null) {
      int productId = selectedProduct.getProductId();
      ProductRepository productRepository = new ProductRepository();
      try {
        productRepository.deleteProduct(productId);
        loadProductData();
        clearFields();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  private void clearFields() {
    productIdField.clear();
    nameField.clear();
    priceField.clear();
    categoryField.clear();
  }
}
