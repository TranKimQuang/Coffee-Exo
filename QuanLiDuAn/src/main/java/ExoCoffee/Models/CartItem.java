package ExoCoffee.Models;

import java.util.Objects;

public class CartItem {
  private ProductDTO product; // Sản phẩm
  private int quantity; // Số lượng

  // Constructor
  public CartItem(ProductDTO product, int quantity) {
    if (product == null) {
      throw new IllegalArgumentException("Sản phẩm không được null");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
    }
    this.product = product;
    this.quantity = quantity;
  }

  // Getters
  public ProductDTO getProduct() {
    return product;
  }

  public int getQuantity() {
    return quantity;
  }

  // Setters
  public void setQuantity(int quantity) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
    }
    this.quantity = quantity;
  }

  // Tính giá trị của mục trong giỏ hàng
  public double getItemTotal() {
    return product.getPrice() * quantity;
  }

  // Phương thức toString
  @Override
  public String toString() {
    return "CartItem{" +
        "product=" + product.getName() +
        ", quantity=" + quantity +
        ", total=" + getItemTotal() +
        '}';
  }

  // Phương thức equals
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CartItem cartItem = (CartItem) o;
    return Objects.equals(product, cartItem.product);
  }

  // Phương thức hashCode
  @Override
  public int hashCode() {
    return Objects.hash(product);
  }
}