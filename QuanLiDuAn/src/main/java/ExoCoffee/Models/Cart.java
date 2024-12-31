package ExoCoffee.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cart {
  private List<CartItem> items; // Danh sách các mục trong giỏ hàng

  // Constructor
  public Cart() {
    this.items = new ArrayList<>();
  }

  // Thêm sản phẩm vào giỏ hàng
  public void addItem(ProductDTO product, int quantity) {
    if (product == null) {
      throw new IllegalArgumentException("Sản phẩm không được null");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
    }

    // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
    Optional<CartItem> existingItem = findItemByProduct(product);
    if (existingItem.isPresent()) {
      // Nếu sản phẩm đã có, cập nhật số lượng
      existingItem.get().increaseQuantity(quantity);
    } else {
      // Nếu sản phẩm chưa có, thêm mới vào giỏ hàng
      items.add(new CartItem(product, quantity));
    }
  }

  // Xóa sản phẩm khỏi giỏ hàng
  public void removeItem(ProductDTO product) {
    if (product == null) {
      throw new IllegalArgumentException("Sản phẩm không được null");
    }
    items.removeIf(item -> item.getProduct().equals(product));
  }

  // Cập nhật số lượng sản phẩm trong giỏ hàng
  public void updateItemQuantity(ProductDTO product, int quantity) {
    if (product == null) {
      throw new IllegalArgumentException("Sản phẩm không được null");
    }
    if (quantity <= 0) {
      throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
    }

    Optional<CartItem> existingItem = findItemByProduct(product);
    if (existingItem.isPresent()) {
      existingItem.get().setQuantity(quantity);
    } else {
      throw new IllegalArgumentException("Sản phẩm không tồn tại trong giỏ hàng");
    }
  }

  // Xóa toàn bộ giỏ hàng
  public void clear() {
    items.clear();
  }

  // Tính tổng giá trị giỏ hàng
  public double getTotalPrice() {
    return items.stream()
        .mapToDouble(CartItem::getItemTotal)
        .sum();
  }

  // Lấy danh sách sản phẩm trong giỏ hàng
  public List<CartItem> getItems() {
    return new ArrayList<>(items); // Trả về bản sao để tránh thay đổi trực tiếp
  }

  // Kiểm tra xem sản phẩm có trong giỏ hàng hay không
  public boolean containsProduct(ProductDTO product) {
    if (product == null) {
      throw new IllegalArgumentException("Sản phẩm không được null");
    }
    return findItemByProduct(product).isPresent();
  }

  // Lấy tổng số lượng sản phẩm trong giỏ hàng (tính cả số lượng của từng sản phẩm)
  public int getTotalItems() {
    return items.stream()
        .mapToInt(CartItem::getQuantity)
        .sum();
  }

  // Tìm một mục trong giỏ hàng bằng sản phẩm
  private Optional<CartItem> findItemByProduct(ProductDTO product) {
    return items.stream()
        .filter(item -> item.getProduct().equals(product))
        .findFirst();
  }

  // Phương thức toString
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Cart{\n");
    for (CartItem item : items) {
      sb.append("  ").append(item.toString()).append("\n");
    }
    sb.append("  Total Price: ").append(getTotalPrice()).append("\n");
    sb.append("}");
    return sb.toString();
  }


}