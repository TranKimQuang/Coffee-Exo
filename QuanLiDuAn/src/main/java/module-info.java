module org.example.quanliduan {
  requires javafx.controls;
  requires javafx.fxml;


  opens org.example.quanliduan to javafx.fxml;
  exports org.example.quanliduan;
}