package main.client.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.common.FileManager;
import main.common.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;

/**
 * controls main menu, and includes buttons for accessing areas of the application
 *
 * @author Kurt Wilson
 */
public class MainMenuController extends Application {
    final static Logger logger = LoggerFactory.getLogger(MainMenuController.class);
    public Button scanPaperButton;
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
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            File logs = new File("logs/log.log");
            File out = new File(System.getProperty("user.home") + "/Desktop/transcriptscanner_logs.log");
            logger.error("uncaught exception!", e);
            try {
                Files.copy(logs.toPath(), out.toPath());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Something went wrong. Please send transcriptscanner_logs (on your desktop) to kurtwilson099@gmail.com", "Error", JOptionPane.ERROR_MESSAGE, null);

        });
        logger.info("stage started");
        FileManager.createTempFolder();
        FileManager.removeTempFiles();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReadingWindow.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
//        scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());

//        mControl = loader.getController();

        primaryStage.setTitle(Constants.programName);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * creates a browser in a window to render help html files
     */
    public void showHelp() {
        Stage stage = new Stage();
        stage.setTitle("Web View");
        Scene scene;
        try {
            scene = new Scene(new Browser(new File("resources/help.html").toURI().toURL().toExternalForm()),750,500, Color.web("#666970"));
            stage.setScene(scene);
            stage.show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}
