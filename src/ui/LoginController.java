package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button actiontarget;

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

}
