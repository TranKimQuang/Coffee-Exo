package ExoCoffee.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Cart {
  private ObservableList<CartItem> items = FXCollections.observableArrayList();

  public ObservableList<CartItem> getItems() {
    return items;
  }

  public void addItem(ProductDTO product, int quantity) {
    if (product == null) {
      throw new IllegalArgumentException("Sản phẩm không được để trống.");
    }

    if (quantity <= 0) {
      throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
    }

    // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
    for (CartItem item : items) {
      if (item.getProduct().getProductId() == product.getProductId()) {
        // Nếu đã có, tăng số lượng
        item.setQuantity(item.getQuantity() + quantity);
        return;
      }
    }

    // Nếu chưa có, thêm sản phẩm mới vào giỏ hàng
    items.add(new CartItem(product, quantity)); // Sử dụng constructor với 2 tham số
  }

  public void clear() {
    items.clear();
  }
}