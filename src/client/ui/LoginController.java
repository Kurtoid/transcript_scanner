package client.ui;

import java.io.IOException;

import client.core.ApplicationState.NetworkConnectionStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private Button actiontarget;

    @FXML
    private Text networkStatusLabel;
    
    @FXML
    protected void loginUser(ActionEvent event) {
        System.out.println("button pressed");
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root, 300, 275);

        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);
    }

	public void setNetworkStatus(NetworkConnectionStatus status) {
		if(status==NetworkConnectionStatus.CONNECTED) {
			networkStatusLabel.setText("Connected");
		}else {
			networkStatusLabel.setText("Connection Failure, check log");
		}
	}

}
