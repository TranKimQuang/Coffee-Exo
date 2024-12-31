package ExoCoffee.Models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
  private List<CartItem> items = new ArrayList<>();

  public void addItem(ProductDTO product, int quantity) {
    // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
    for (CartItem item : items) {
      if (item.getProduct().getProductId() == product.getProductId()) {
        item.setQuantity(item.getQuantity() + quantity);
        return;
      }
    }
    // Nếu chưa có, thêm sản phẩm mới vào giỏ hàng
    items.add(new CartItem(product, quantity));
  }

  public void removeItem(int productId) {
    items.removeIf(item -> item.getProduct().getProductId() == productId);
  }

  public void clear() {
    items.clear();
  }

  public List<CartItem> getItems() {
    return items;
  }

  public double getTotalPrice() {
    return items.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
  }
}