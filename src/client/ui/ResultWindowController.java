package client.ui;

import common.courses.Course;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

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
            TableColumn<Course, String> firstEmailCol = new TableColumn("Course");
            firstEmailCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Course, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Course, String> p) {
                    // p.getValue() returns the Person instance for a particular TableView row
                    return new SimpleStringProperty(p.getValue().courseDesc);
                }
            });


            TableColumn<Course, String> secondEmailCol = new TableColumn("Grade");
            secondEmailCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Course, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Course, String> p) {
                    // p.getValue() returns the Person instance for a particular TableView row
                    Object grade = p.getValue().getGrade();
//                    if(grade instanceof String)
                    return new SimpleStringProperty(p.getValue().grade);
//                    else
//                        return new SimpleObjectProperty<Image>(p.getValue().getGrade())
                }
            });

            resultTable.getColumns().addAll(firstEmailCol, secondEmailCol);

            final ObservableList<Course> data = FXCollections.observableArrayList(
                    courses
            );
            resultTable.setItems(data);


        });

    }

}
