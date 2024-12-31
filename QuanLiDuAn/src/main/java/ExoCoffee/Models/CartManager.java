package ExoCoffee.Models;


public class CartManager {
  // Đối tượng Cart duy nhất
  private static Cart cart = new Cart();

  // Phương thức để lấy đối tượng Cart
  public static Cart getCart() {
    return cart;
  }

  // Phương thức để xóa giỏ hàng (nếu cần)
  public static void clearCart() {
    cart.clear();
  }

  // Phương thức để thêm sản phẩm vào giỏ hàng (nếu cần)
  public static void addItemToCart(ProductDTO product, int quantity) {
    cart.addItem(product, quantity);
  }
}