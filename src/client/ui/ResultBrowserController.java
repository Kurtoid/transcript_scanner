package client.ui;

import common.FileManager;
import common.ParsedReport;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

public class ResultBrowserController extends Application implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(ResultBrowserController.class);
    public Label gpaLabel;
    int index = 0;
    ResultWindowController rwController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("stage started");
        FileManager.createTempFolder();
        FileManager.removeTempFiles();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ResultBrowser.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());

        primaryStage.setTitle("Suncoast Transcript Scanner");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ArrayList<ParsedReport> reports;


    public void setReports(Set<ParsedReport> reports) {
        this.reports = new ArrayList<>();
        this.reports.addAll(reports);
        updateReport();
    }

    @FXML
    SubScene gridSubScene;
    @FXML
    Pane fitPane;
    @FXML
    Button nextButton;
    @FXML
    Button lastButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.trace("initialized resultbrowser");
        configureTable();
    }

    private void configureTable() {
        Parent root = null;
        FXMLLoader loader = null;
        try {
            /**
             * load the FXML files needed for reader layout
             */
            loader = new FXMLLoader(getClass().getResource("ResultWindow.fxml"));
            root = loader.load();
        } catch (IOException e) {
            logger.error("couldnt load ui", e);
        }
        rwController = loader.getController();
        gridSubScene.setRoot(root);
        gridSubScene.widthProperty().bind(fitPane.widthProperty());
        gridSubScene.heightProperty().bind(fitPane.heightProperty());

    }

    private void updateReport() {
        if (index < 0)
            index = 0;
        if (index >= reports.size())
            index = reports.size() - 1;
        lastButton.setDisable(index == 0);
        nextButton.setDisable(index == reports.size() - 1);

        rwController.setReport(reports.get(index));
        gpaLabel.setText("GPA: " + reports.get(index).getGPA());
    }

    public void previousPage(ActionEvent actionEvent) {
        index--;
        updateReport();
    }

    public void nextPage(ActionEvent actionEvent) {
        index++;
        updateReport();
    }
}
