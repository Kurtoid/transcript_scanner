package main.client.ui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import main.common.ParsedReport;
import main.common.courses.Course;
import main.common.courses.CourseMatcher;
import org.slf4j.Logger;

import javax.swing.text.html.ImageView;

/**
 * controls the table where individual paper scan results are shown
 */
public class ResultWindowController {
    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(ResultWindowController.class);
    @FXML
    TableView<Course> resultTable;
    private ParsedReport report;
    private EditAction onEdit;

    void setOnEdit(EditAction onEdit) {
        this.onEdit = onEdit;
    }

    void setReport(ParsedReport reports) {
        this.report = reports;
        final ObservableList<Course> data = FXCollections.observableArrayList(
                report.getCourses()
        );
        resultTable.setItems(data);

    }

    Course c;

    /**
     * utility method to set column properties
     */
    private void setColumns() {
        resultTable.getColumns().addAll(getColumns());

    }

    /**
     * generates required display columns and binds properties to them
     *
     * @return a list of columns used by the table
     */
    public TableColumn[] getColumns() {
        TableColumn<Course, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(p -> {
            // p.getValue() returns the Course instance for a particular TableView row
            return new SimpleStringProperty(p.getValue().courseDesc);
        });
        courseCol.setCellFactory(new Callback<TableColumn<Course, String>, TableCell<Course, String>>() {
            @Override
            public TableCell<Course, String> call(TableColumn<Course, String> param) {
                TextFieldTableCell<Course, String> cell = new TextFieldTableCell<Course, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                    }
                };
                cell.setConverter(new DefaultStringConverter());
                cell.setEditable(true);
                return cell;
            }
        });
        courseCol.setOnEditCommit(
                t -> {
                    String item = t.getNewValue();
                    logger.trace("editing " + item);
                    if (item == null) {
                        logger.error("this row is null!");
                    } else {
                        Course c = CourseMatcher.matchCourseByDesc(item.toUpperCase());
                        Course current = t.getTableView().getItems().get(t.getTablePosition().getRow());
                        current.setDesc(c.courseDesc);
                        current.type = c.type;
                        t.getTableView().getItems().set(t.getTablePosition().getRow(), current);
                    }
                    onEdit.afterEdit();
                }
        );

        courseCol.setEditable(true);


        TableColumn<Course, String> gradeCol = new TableColumn<>("Grade");
        gradeCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().grade));

//            gradeCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        gradeCol.setEditable(true);
        gradeCol.setCellFactory(new Callback<TableColumn<Course, String>, TableCell<Course, String>>() {
            @Override
            public TextFieldTableCell<Course, String> call(TableColumn<Course, String> param) {
                TextFieldTableCell<Course, String> cell = new TextFieldTableCell<Course, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? "" : item);
                        TableRow row = getTableRow();
                        if (row != null && !empty) {
                            // highlight missing or low grades
                            if (item == null || item.equals("")) {
                                row.setStyle("-fx-background-color:lightcoral");
                            } else if (item.equals("F") || item.equals("D")) {
                                row.setStyle("-fx-background-color:orange");
                            } else if (item.equals("C")) {
                                row.setStyle("-fx-background-color:yellow");
                            } else {
                                row.setStyle("");
                            }
                        } else if (row != null) {
                            row.setStyle("");
                        }
                    }

                };
                cell.setConverter(new DefaultStringConverter());
                // we want operator to be able to correct mistakes
                cell.setEditable(true);
                return cell;
            }
        });

        gradeCol.setOnEditCommit(
                t -> {
                    t.getTableView().getItems().get(
                            t.getTablePosition().getRow()).setGrade(t.getNewValue().toUpperCase());
                    onEdit.afterEdit();
                }
        );
        gradeCol.setEditable(true);
        TableColumn<Course, ImageView> correctionCol = new TableColumn<>("Original");
        correctionCol.setCellValueFactory(new PropertyValueFactory<Course, ImageView>("grade"));
        correctionCol.setEditable(false);
        TableColumn<Course, String> typeCol = new TableColumn<>("Subject");
        typeCol.setCellValueFactory(p -> {
            // p.getValue() returns the Person instance for a particular TableView row
            return new SimpleStringProperty(p.getValue().type);
        });
        typeCol.setEditable(false);
        return new TableColumn[]{courseCol, gradeCol, correctionCol, typeCol};
    }
    @FXML
    private void initialize() {

        Platform.runLater(() -> {
            setColumns();
            final ObservableList<Course> data = FXCollections.observableArrayList(
                    report.getCourses()
            );
            resultTable.setEditable(true);
            resultTable.setItems(data);


        });

    }

    public interface EditAction {
        void afterEdit();
    }

}
