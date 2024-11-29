package ExoCoffee.Controllers;


import ExoCoffee.Models.Product;
import ExoCoffee.Data.ProductDAO;
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
  private TableView<Product> productTable;
  @FXML
  private TableColumn<Product, Integer> productIdColumn;
  @FXML
  private TableColumn<Product, String> nameColumn;
  @FXML
  private TableColumn<Product, Double> priceColumn;
  @FXML
  private TableColumn<Product, String> categoryColumn;
  @FXML
  private TextField productIdField;
  @FXML
  private TextField nameField;
  @FXML
  private TextField priceField;
  @FXML
  private TextField categoryField;

  private ObservableList<Product> productList;

  @FXML
  public void initialize() {
    productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

    productList = FXCollections.observableArrayList();
    productTable.setItems(productList);

    loadProductData();
  }

  private void loadProductData() {
    // Load data from database
    ProductDAO productDAO = new ProductDAO();
    try {
      productList.setAll(productDAO.getAllProducts());
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

    Product product = new Product(name, productId, price, category);
    ProductDAO productDAO = new ProductDAO();
    try {
      productDAO.addProduct(product);
      productList.add(product);
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

    Product product = new Product(name, productId, price, category);
    ProductDAO productDAO = new ProductDAO();
    try {
      productDAO.updateProduct(product);
      loadProductData();
      clearFields();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void handleDeleteProduct() {
    int productId = Integer.parseInt(productIdField.getText());

    ProductDAO productDAO = new ProductDAO();
    try {
      productDAO.deleteProduct(productId);
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
