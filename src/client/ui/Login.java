package client.ui;

import java.io.IOException;

import client.core.ApplicationState;
import common.FileManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Login extends Application {
    LoginController lControl;

    public static void main(String[] args) {
    	try {
			FileManager.removeTempFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginLayout.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 300, 275);
//        scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());

        lControl = loader.getController();
        ApplicationState.startNetworkClient(status -> lControl.setNetworkStatus(status));

        primaryStage.setTitle("Suncoast Transcript Scanner");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}