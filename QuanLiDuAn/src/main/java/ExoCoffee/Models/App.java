package ExoCoffee.Models;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
  private static Stage primaryStage;

  @Override
  public void start(Stage stage) throws IOException {
    primaryStage = stage;
    setRoot("login"); // Mở giao diện đăng nhập khi khởi động ứng dụng
  }

  public static void setRoot(String fxml) {
    try {
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/org/ExoCoffee/ExoCoffee/" + fxml + ".fxml"));
      Scene scene = new Scene(loader.load());
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
