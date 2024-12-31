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

  // Tăng số lượng
  public void increaseQuantity(int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Số lượng tăng phải lớn hơn 0");
    }
    this.quantity += amount;
  }

  // Giảm số lượng
  public void decreaseQuantity(int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Số lượng giảm phải lớn hơn 0");
    }
    if (this.quantity - amount < 1) {
      throw new IllegalArgumentException("Số lượng không thể nhỏ hơn 1");
    }
    this.quantity -= amount;
  }

  // Tính giá trị của mục trong giỏ hàng
  public double getItemTotal() {
    return product.getPrice() * quantity;
  }

  // Tạo bản sao
  public CartItem copy() {
    return new CartItem(this.product, this.quantity);
  }

  // Kiểm tra xem hai CartItem có cùng sản phẩm hay không
  public boolean isSameProduct(CartItem other) {
    if (other == null) {
      return false;
    }
    return this.product.equals(other.product);
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
    return quantity == cartItem.quantity && Objects.equals(product, cartItem.product);
  }

  // Phương thức hashCode
  @Override
  public int hashCode() {
    return Objects.hash(product, quantity);
  }
}