package client.ui;

import common.courses.Course;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javax.swing.text.TableView;
import java.util.Set;

public class ResultWindowController {
    @FXML
    TableView resultTable;
    private Set<Course> courses;

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @FXML
    private void initialize() {

        Platform.runLater(() -> {

        });

    }

}
