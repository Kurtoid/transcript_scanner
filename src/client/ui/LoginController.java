package client.ui;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.core.ApplicationState;
import client.core.ApplicationState.NetworkConnectionStatus;
import common.networking.LoginMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController {
	final static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@FXML
	private Button actiontarget;

	@FXML
	private Text networkStatusLabel;

	@FXML
	private TextField usernameField;

	@FXML
	private PasswordField passwordField;

	@FXML
	protected void loginUser(ActionEvent event) {
		logger.trace("login button pressed");
		try {
			// TODO: refactor
			MessageDigest digest;

			// TODO: use bCrypt
			digest = MessageDigest.getInstance("SHA-256");

			byte[] hash = digest.digest(passwordField.getText().getBytes(StandardCharsets.UTF_8));

			LoginMessage m = new LoginMessage(usernameField.getText(), hash);
			logger.trace("hi " + usernameField.getText());
			ApplicationState.connection.send(m);
			Parent root = null;
			root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));

			Scene scene = new Scene(root, 300, 275);

			((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);

		} catch (IOException e) {
			logger.error("problem loading ui", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("password encryption error", e);
		}
	}

	public void setNetworkStatus(NetworkConnectionStatus status) {
		if (status == NetworkConnectionStatus.CONNECTED) {
			networkStatusLabel.setText("Connected");
		} else {
			networkStatusLabel.setText("Connection Failure, check log");
		}
	}

}
