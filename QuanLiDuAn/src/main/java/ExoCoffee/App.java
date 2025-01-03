package ExoCoffee;

import ExoCoffee.Custom.CustomTitleBarController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {
  private static Stage primaryStage;

  @Override
  public void start(Stage stage) throws IOException {
    primaryStage = stage;
    primaryStage.initStyle(StageStyle.UNDECORATED); // Ẩn thanh tiêu đề mặc định
    setRoot("login"); // Mở giao diện đăng nhập khi khởi động ứng dụng
  }

  public static void setRoot(String fxml) {
    try {
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/org/ExoCoffee/FXML/" + fxml + ".fxml"));
      VBox root = loader.load(); // Load FXML vào VBox

      // Tải thanh tiêu đề tùy chỉnh
      FXMLLoader titleBarLoader = new FXMLLoader(App.class.getResource("/org/ExoCoffee/FXML/custom_title.fxml"));
      HBox titleBar = titleBarLoader.load();
      CustomTitleBarController titleBarController = titleBarLoader.getController();
      titleBarController.setStage(primaryStage);

      // Thêm thanh tiêu đề vào đầu root
      root.getChildren().add(0, titleBar);

      // Tạo Scene và hiển thị Stage
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}