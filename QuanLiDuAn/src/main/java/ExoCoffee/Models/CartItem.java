package ExoCoffee.Models;

public class CartItem {
  private int productId;
  private String productName;
  private int quantity;
  private double price;
  private double totalProduct;

  // Constructor hiện tại
  public CartItem(int productId, String productName, int quantity, double price, double totalProduct) {
    this.productId = productId;
    this.productName = productName;
    this.quantity = quantity;
    this.price = price;
    this.totalProduct = totalProduct;
  }

  // Constructor mới chấp nhận ProductDTO
  public CartItem(ProductDTO product, int quantity) {
    this.productId = product.getProductId();
    this.productName = product.getName();
    this.quantity = quantity;
    this.price = product.getPrice();
    this.totalProduct = this.price * this.quantity;
  }

  // Getters and setters
  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public double getTotalProduct() {
    return totalProduct;
  }

  public void setTotalProduct(double totalProduct) {
    this.totalProduct = totalProduct;
  }
}
