package ExoCoffee.Models;

public class OrderState {
  private static int currentOrderId = -1;

  public static int getCurrentOrderId() {
    return currentOrderId;
  }

  public static void setCurrentOrderId(int currentOrderId) {
    OrderState.currentOrderId = currentOrderId;
  }
}

