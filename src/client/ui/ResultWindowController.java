package client.ui;

import common.courses.Course;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import javax.swing.text.html.ImageView;
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
            TableColumn<Course, String> courseCol = new TableColumn("Course");
            courseCol.setCellValueFactory(p -> {
                // p.getValue() returns the Person instance for a particular TableView row
                return new SimpleStringProperty(p.getValue().courseDesc);
            });


            TableColumn<Course, String> gradeCol = new TableColumn("Grade");
            gradeCol.setCellValueFactory(p -> {
                // p.getValue() returns the Person instance for a particular TableView row
//                    Object grade = p.getValue().grade;
//                    if(grade instanceof String)
                return new SimpleStringProperty(p.getValue().grade);
//                    else
//                        return new SimpleObjectProperty<Image>(p.getValue().getGrade())
            });
            gradeCol.setEditable(true);
            gradeCol.setCellFactory(TextFieldTableCell.forTableColumn());
//            gradeCol.setOnEditCommit(
//                    t -> t.getTableView().getItems().get(
//                            t.getTablePosition().getRow()).setGrade(t.getNewValue())
//            );

            TableColumn<Course, ImageView> correctionCol = new TableColumn("Correction");
            correctionCol.setCellValueFactory(new PropertyValueFactory<Course, ImageView>("grade"));

            resultTable.getColumns().addAll(courseCol, gradeCol, correctionCol);

            final ObservableList<Course> data = FXCollections.observableArrayList(
                    courses
            );
            resultTable.setEditable(true);
            resultTable.setItems(data);


        });

    }

}
