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
  }

  private void loadProductData() {
    // Load data from database
    ProductRepository productRepository = new ProductRepository();
    try {
      productDTOList.setAll(productRepository.getAllProducts());
    } catch (SQLException e) {
      e.printStackTrace();
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
    int productId = Integer.parseInt(productIdField.getText());

    ProductRepository productRepository = new ProductRepository();
    try {
      productRepository.deleteProduct(productId);
      loadProductData();
      clearFields();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void clearFields() {
    productIdField.clear();
    nameField.clear();
    priceField.clear();
    categoryField.clear();
  }
}
