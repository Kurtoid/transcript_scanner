
package client.ui;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.core.ApplicationState;
import common.FileManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Login extends Application {
    LoginController lControl;
	final static Logger logger = LoggerFactory.getLogger(Login.class);
    public static void main(String[] args) {
		logger.info("Started login main");
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
		logger.info("stage started");
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