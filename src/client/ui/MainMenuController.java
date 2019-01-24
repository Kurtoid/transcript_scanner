package client.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * controls main menu, and includes buttons for accessing areas of the application
 *
 * @author Kurt Wilson
 */
public class MainMenuController extends Application {
    final static Logger logger = LoggerFactory.getLogger(MainMenuController.class);
    MainMenuController mControl;

    /**
     * called when image read button is pressed
     * @param actionEvent
     */
    public void startReadPaper(ActionEvent actionEvent) {
        Parent root = null;
        try {
            /**
             * load the FXML files needed for reader layout
             */
            root = FXMLLoader.load(getClass().getResource("ReadingWindow.fxml"));
        } catch (IOException e) {
            logger.error("couldnt load ui", e);
        }

        Scene scene = new Scene(root);
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).setScene(scene);
    }

    /**
     * creates and shows main menu from layout file
     * @param primaryStage
     * @throws Exception when layout file cant be read
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("stage started");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 300, 275);
//        scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());

        mControl = loader.getController();

        primaryStage.setTitle("Suncoast Transcript Scanner");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
