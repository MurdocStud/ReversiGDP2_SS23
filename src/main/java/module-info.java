module com.example.othello1852 {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                        requires org.kordamp.bootstrapfx.core;
            
    opens com.example.othello1852 to javafx.fxml;
    exports com.example.othello1852;
}