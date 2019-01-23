package client.ui;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {
	final static Logger logger = LoggerFactory.getLogger(MainMenuController.class);
    public void startReadPaper(ActionEvent actionEvent) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("ReadingWindow.fxml"));
        } catch (IOException e) {
			logger.error("couldnt load ui", e);
        }

        Scene scene = new Scene(root, 300, 275);

        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).setScene(scene);

    }
}
