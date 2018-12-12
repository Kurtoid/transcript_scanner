package client.ui;

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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginController {
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
        System.out.println("button pressed");
        try {
            // TODO: refactor
            MessageDigest digest;

            // TODO: use bCrypt
            digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(passwordField.getText().getBytes(StandardCharsets.UTF_8));

            LoginMessage m = new LoginMessage(usernameField.getText(), hash);
            System.out.println("hi " + usernameField.getText());
            System.out.println("password hash: " + Arrays.toString(hash));
/*
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02X ", b));
            }
            System.out.println(sb.toString());
            System.out.println(hash.length);
            System.out.println(m.message_size);
*/
            ApplicationState.connection.send(m);
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scene scene = new Scene(root, 300, 275);

            ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(scene);

        } catch (NoSuchAlgorithmException | IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
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
