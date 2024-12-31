package ExoCoffee.Models;

public class CartItem {
  private ProductDTO product;
  private int quantity;
  private double totalPrice; // Thêm thuộc tính totalPrice

  public CartItem(ProductDTO product, int quantity) {
    this.product = product;
    this.quantity = quantity;
    this.totalPrice = product.getPrice() * quantity;
  }

  public CartItem(ProductDTO product, int quantity, double totalPrice) {
    this.product = product;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
  }

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
    return product.getName();
  }

  public double getPrice() {
    return product.getPrice();
  }
}
