package ExoCoffee.Models;

public class CartItem {
  private ProductDTO product;
  private int quantity;
  private double totalPrice;

  // Constructor với 2 tham số (ProductDTO và int)
  public CartItem(ProductDTO product, int quantity) {
    this.product = product;
    this.quantity = quantity;
    this.totalPrice = product.getPrice() * quantity; // Tính toán totalPrice
  }

  // Constructor với 3 tham số (ProductDTO, int, và double)
  public CartItem(ProductDTO product, int quantity, double totalPrice) {
    this.product = product;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
  }

  // Các phương thức getter và setter
  public ProductDTO getProduct() {
    return product;
  }

  public void setProduct(ProductDTO product) {
    this.product = product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
  }

  public String getProductName() {
    return product != null ? product.getName() : "";
  }

  public double getPrice() {
    return product != null ? product.getPrice() : 0.0;
  }
}