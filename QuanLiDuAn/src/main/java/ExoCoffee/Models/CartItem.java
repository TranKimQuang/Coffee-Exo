package ExoCoffee.Models;

public class CartItem {
  private ProductDTO product;
  private int quantity;
  private double totalProduct;

  public CartItem(ProductDTO product, int quantity, double totalProduct) {
    this.product = product;
    this.quantity = quantity;
    this.totalProduct = totalProduct;
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

  public double getTotalProduct() {
    return this.getQuantity() * this.getPrice();
  }

  public void setTotalProduct(double totalProduct) {
    this.totalProduct = totalProduct;
  }

  public String getProductName() {
    return product.getName();
  }

  public double getPrice() {
    return product.getPrice();
  }
}
