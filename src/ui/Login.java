package ui;

import core.ApplicationState;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import networking.NetworkClient;

public class Login extends Application {
    public static void main(String[] args) {
        ApplicationState.c = new NetworkClient("localhost", 8080);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LoginLayout.fxml"));

        Scene scene = new Scene(root, 300, 275);

        primaryStage.setTitle("FXML Welcome");
        primaryStage.setScene(scene);
        primaryStage.show();
//        ApplicationState.c.connect(new );

    }

}