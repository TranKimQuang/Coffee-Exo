package ExoCoffee.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Cart {
  private ObservableList<CartItem> items = FXCollections.observableArrayList();

  /**
   * Thêm sản phẩm vào giỏ hàng.
   *
   * @param product  Sản phẩm cần thêm.
   * @param quantity Số lượng sản phẩm.
   */
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
    items.add(new CartItem(product, quantity));
  }

  /**
   * Xóa tất cả sản phẩm trong giỏ hàng.
   */
  public void clear() {
    items.clear();
  }

  /**
   * Lấy danh sách sản phẩm trong giỏ hàng.
   *
   * @return Danh sách sản phẩm.
   */
  public ObservableList<CartItem> getItems() {
    return items;
  }
}