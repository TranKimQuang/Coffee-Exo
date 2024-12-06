package ExoCoffee.Models;

import java.util.ArrayList;
import java.util.List;

public class Cart {
  private List<CartItem> items = new ArrayList<>();

  public void addItem(ProductDTO product, int quantity) {
    for (CartItem item : items) {
      if (item.getProduct().getProductId() == product.getProductId()) {
        item.setQuantity(item.getQuantity() + quantity);
        return;
      }
    }
    items.add(new CartItem(product, quantity));
  }

  public void removeItem(ProductDTO product) {
    items.removeIf(item -> item.getProduct().getProductId() == product.getProductId());
  }

  public double getTotalPrice() {
    double total = 0;
    for (CartItem item : items) {
      total += item.getProduct().getPrice() * item.getQuantity();
    }
    return total;
  }

  public List<CartItem> getItems() {
    return items;
  }
}
