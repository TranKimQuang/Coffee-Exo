package ExoCoffee.Models;

public class CartItem {
  private ProductDTO product;
  private int quantity;

  public CartItem(ProductDTO product, int quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  public ProductDTO getProduct() {
    return product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
