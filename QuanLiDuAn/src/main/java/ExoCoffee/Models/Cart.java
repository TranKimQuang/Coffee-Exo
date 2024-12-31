package ExoCoffee.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cart {
  private List<CartItem> items = new ArrayList<>();

  // Thêm sản phẩm vào giỏ hàng
  public void addItem(ProductDTO product, int quantity) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
    }

    // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
    Optional<CartItem> existingItem = items.stream()
        .filter(item -> item.getProduct().getProductId() == product.getProductId())
        .findFirst();

    if (existingItem.isPresent()) {
      // Nếu sản phẩm đã có, cập nhật số lượng
      CartItem item = existingItem.get();
      item.setQuantity(item.getQuantity() + quantity);
    } else {
      // Nếu sản phẩm chưa có, thêm mới vào giỏ hàng
      items.add(new CartItem(product, quantity));
    }
  }

  // Xóa sản phẩm khỏi giỏ hàng
  public void removeItem(ProductDTO product) {
    items.removeIf(item -> item.getProduct().getProductId() == product.getProductId());
  }

  // Cập nhật số lượng sản phẩm trong giỏ hàng
  public void updateItemQuantity(ProductDTO product, int quantity) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
    }

    items.stream()
        .filter(item -> item.getProduct().getProductId() == product.getProductId())
        .findFirst()
        .ifPresent(item -> item.setQuantity(quantity));
  }

  // Xóa toàn bộ giỏ hàng
  public void clear() {
    items.clear();
  }

  // Tính tổng giá trị giỏ hàng
  public double getTotalPrice() {
    return items.stream()
        .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
        .sum();
  }

  // Kiểm tra xem sản phẩm có trong giỏ hàng hay không
  public boolean containsProduct(ProductDTO product) {
    return items.stream()
        .anyMatch(item -> item.getProduct().getProductId() == product.getProductId());
  }

  // Lấy danh sách sản phẩm trong giỏ hàng
  public List<CartItem> getItems() {
    return new ArrayList<>(items); // Trả về bản sao để tránh thay đổi trực tiếp
  }

  // Đặt lại danh sách sản phẩm trong giỏ hàng
  public void setItems(List<CartItem> items) {
    this.items = new ArrayList<>(items); // Đảm bảo danh sách mới là một bản sao
  }
}