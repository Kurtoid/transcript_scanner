package client.ui;

import client.core.ApplicationState;
import common.GradeReport;
import common.ParsedReport;
import common.courses.Course;
import common.imaging.ColumnDetector;
import common.imaging.ImagePreprocessor;
import common.tesseract.OCRReader;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * the meat of  the application
 * Used by user to load images, annotate them, and export extracted class information
 */
public class ReadingWindow {
    final static Logger logger = org.slf4j.LoggerFactory.getLogger(ReadingWindow.class);
    public HBox imageContainer;
    public ScrollPane imageScroller;
    public Canvas imagePreview;
    public Button LoadImagesButton;
    public GridPane basePane;
    private GradeReport selectedImage;
    @FXML
    private CheckBox columnSnapBox;
    private double selectedLeft;
    private double selectedRight;
    private boolean selectionStarted = false;

    private double nameColumnLeft = -1;
    private double nameColumnRight = -1;

    private double gradeColumnRight = -1;
    private double gradeColumnLeft = -1;
    private ArrayList<Double> columnLocations;
    /**
     * runs when any image in the bottom scroll pane is clicked
     */
    private EventHandler<MouseEvent> smallImageClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ImageView source = (ImageView) event.getSource();
//            imagePreview.setImage(source.getImage());
            logger.trace("clicked on " + source.getProperties().get("imageID"));
            for (GradeReport s : ApplicationState.scannedImages) {
                if (s.id.equals(source.getProperties().get("imageID"))) {
                    // found it; make it the big image
                    selectedImage = s;
                    selectedImage.updateImage();
                }
            }
            ChangeListener<Number> stageSizeListener = (observable, oldV, newV) -> updateCanvas();
            basePane.getScene().getWindow().widthProperty().addListener(stageSizeListener);
            basePane.getScene().getWindow().widthProperty().addListener(stageSizeListener);
            columnLocations = ColumnDetector.findColumns(selectedImage.file);
            updateCanvas();
        }

    };
    public Pane canvasContainer;

    /**
     * load image button was pressed
     * opens a filechooser and allows user to pick multiple images
     *
     * @param actionEvent
     */
    @FXML
    public void loadImages(ActionEvent actionEvent) {
        logger.trace("loading image");
        logger.trace("selecting image {}" + columnSnapBox.isSelected());
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));
        List<File> images = fc.showOpenMultipleDialog(((Node) actionEvent.getTarget()).getScene().getWindow());
        if (images != null) {
            for (int i = 0; i < images.size(); i++) {
                GradeReport p = new GradeReport(images.get(i));
                ApplicationState.scannedImages.add(p);

//               imageViews[i].setF
            }
            showImages();
        }
    }

    /**
     * updates bottom row of images for display
     */
    private void showImages() {
        ImageView[] imageViews = new ImageView[ApplicationState.scannedImages.size()];
        for (int i = 0; i < ApplicationState.scannedImages.size(); i++) {
            imageViews[i] = new ImageView(ApplicationState.scannedImages.get(i).file.toURI().toString());
//               imageViews[i].setFitHeight();
            imageViews[i].fitHeightProperty().bind(imageScroller.heightProperty());
            imageViews[i].setPreserveRatio(true);
            imageViews[i].setOnMouseClicked(smallImageClicked);
            imageViews[i].getProperties().put("imageID", ApplicationState.scannedImages.get(i).id);

        }
        imageContainer.getChildren().clear();
        imageContainer.getChildren().addAll(imageViews);

    }

    /**
     * runs opencv align on all of the images
     *
     * @param actionEvent
     */
    public void alignAllImages(ActionEvent actionEvent) {
        for (GradeReport f : ApplicationState.scannedImages) {
            f.file = ImagePreprocessor.alignImage(f.file);
        }
        showImages();
        updateCanvas();
    }

    @FXML
    public void scanSelectedImage(ActionEvent actionEvent) {
        Set<ParsedReport> reports = new HashSet<>();

        for (GradeReport f : ApplicationState.scannedImages) {
//        	System.out.println(selectedImage.file.getName());
//    		System.out.println(ImagePreprocessor.splitImage(selectedImage.file).getAbsolutePath());

            Set<Course> courses = OCRReader.scanImage(f, nameColumnLeft, nameColumnRight, gradeColumnLeft, gradeColumnRight);
            ParsedReport pr = new ParsedReport();
            pr.setCourses(courses);
            reports.add(pr);

        }
        Parent root = null;
        FXMLLoader loader = null;

        try {
            /**
             * load the FXML files needed for reader layout
             */
            loader = new FXMLLoader(getClass().getResource("ResultBrowser.fxml"));
            root = loader.load();
        } catch (IOException e) {
            logger.error("couldnt load ui", e);
        }

        ResultBrowserController controller = loader.getController();
        controller.setReports(reports);
        Scene scene = new Scene(root);
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).setScene(scene);

    }

    @FXML
    public void setGradeColumn(ActionEvent e) {
        gradeColumnLeft = selectedLeft;
        gradeColumnRight = selectedRight;
        updateCanvas();
    }

    @FXML
    public void setNameColumn(ActionEvent e) {
        nameColumnLeft = selectedLeft;
        nameColumnRight = selectedRight;
        updateCanvas();
    }

    /**
     * drawing code for top image view
     * shows sliders for selecting columns
     */
    private void updateCanvas() {
        if (imagePreview == null || selectedImage == null)
            return;
        if (!selectionStarted) {
            imagePreview.widthProperty().bind(canvasContainer.widthProperty());
            imagePreview.heightProperty().bind(canvasContainer.heightProperty());

            selectedLeft = 0.2;
            selectedRight = 0.8;
            selectionStarted = true;
            imagePreview.setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (inLeftBuffer(event.getX()) || inRightBuffer(event.getX())) {
                        imagePreview.getScene().setCursor(Cursor.H_RESIZE);
                    } else {
                        imagePreview.getScene().setCursor(Cursor.DEFAULT);

                    }
//                    System.out.println(event.getX());
                }
            });
            imagePreview.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                /**
                 * this is a mess
                 * keeps the sliders a distance away from each other. if in column snap, find nearest snap point
                 * TODO: better drag
                 */
                public void handle(MouseEvent event) {
                    if (!columnSnapBox.isSelected()) {
                        if (event.getX() > imagePreview.getWidth() || event.getY() > imagePreview.getHeight()
                                || event.getY() < 0 || event.getX() < 0)
                            return;
                        if (inLeftBuffer(event.getX()) && inRightBuffer(event.getX())) {
                            double mousePos = event.getX() / imagePreview.getWidth();
                            if (Math.abs(mousePos - selectedLeft) > Math.abs(mousePos - selectedRight)) {
                                moveLeft(event.getX());
                            } else {
                                moveRight(event.getX());
                            }
                        } else if (inLeftBuffer(event.getX())) {
                            moveLeft(event.getX());

                        } else if (inRightBuffer(event.getX())) {
                            moveRight(event.getX());

                        }
                    } else {
                        double mPercent = event.getX() / imagePreview.getWidth();
                        double distance = Math.abs(columnLocations.get(0) - mPercent);
                        int idx = 0;
                        for (int c = 1; c < columnLocations.size(); c++) {
                            double cdistance = Math.abs(columnLocations.get(c) - mPercent);
                            if (cdistance < distance) {
                                idx = c;
                                distance = cdistance;
                            }
                        }
                        double closestColumnNumber = columnLocations.get(idx);

                        if (Math.abs(mPercent - selectedLeft) < Math.abs(mPercent - selectedRight)) { // if closer to
                            // right
                            selectedLeft = closestColumnNumber;
                        } else {
                            selectedRight = closestColumnNumber;
                        }
                    }
                    updateCanvas();
                }

                private void moveRight(double x) {
                    selectedRight = x / imagePreview.getWidth();
                    logger.trace("right selection bound: {}", selectedRight);
                    if (selectedRight > 0.8)
                        selectedRight = 0.8;
                    if (Math.abs(selectedLeft - selectedRight) < 0.02)
                        selectedRight = selectedLeft + 0.02;

                }

                private void moveLeft(double x) {
                    selectedLeft = x / imagePreview.getWidth();
                    logger.trace("left selection bound: {}", selectedLeft);
                    if (selectedLeft > 0.8)
                        selectedLeft = 0.8;
                    if (Math.abs(selectedLeft - selectedRight) < 0.02)
                        selectedLeft = selectedRight - 0.02;

                }
            });

        }
        GraphicsContext gc = imagePreview.getGraphicsContext2D();
        double scaleFactor;
//		System.out.println(imagePreview.getHeight());
//		System.out.println(selectedImage.getImage().getHeight());
        if (imagePreview.getHeight() / selectedImage.getImage().getHeight() > imagePreview.getWidth()
                / selectedImage.getImage().getWidth()) {
            scaleFactor = imagePreview.getHeight() / selectedImage.getImage().getHeight();
        } else {
            scaleFactor = imagePreview.getWidth() / selectedImage.getImage().getWidth();

        }
//        logger.trace("scale factor: {}", scaleFactor);
        if (selectedImage != null) {
            gc.drawImage(selectedImage.getImage(), 0, 0, selectedImage.getImage().getWidth() * scaleFactor,
                    selectedImage.getImage().getHeight() * scaleFactor);
        }

        // draw current selection
        gc.setFill(new Color(0, 0, 0, .25));
        gc.fillRect(0, 0, selectedLeft * imagePreview.getWidth(), imagePreview.getHeight());
        gc.fillRect(selectedRight * imagePreview.getWidth(), 0, imagePreview.getWidth(), imagePreview.getHeight());

        // draw name column
        if (nameColumnLeft != -1 && nameColumnRight != -1) {
            gc.setFill(new Color(1, 0, 0, .25));
            gc.fillRect(nameColumnLeft * imagePreview.getWidth(), 0,
                    (nameColumnRight - nameColumnLeft) * imagePreview.getWidth(), imagePreview.getHeight());
        }

        // draw grade column
        if (gradeColumnLeft != -1 && gradeColumnRight != -1) {
            gc.setFill(new Color(0, 1, 0, .25));
            gc.fillRect(gradeColumnLeft * imagePreview.getWidth(), 0,
                    (gradeColumnRight - gradeColumnLeft) * imagePreview.getWidth(), imagePreview.getHeight());

        }

    }

    /**
     * convenience method for detecting if the mouse is eligible for selecting the edge of the slider
     *
     * @param mouseX
     * @return if the mouse is near a slider
     */
    private boolean inLeftBuffer(double mouseX) {
        return mouseX > (selectedLeft * imagePreview.getWidth() - (imagePreview.getWidth() * 0.02))
                && mouseX < (selectedLeft * imagePreview.getWidth() + (imagePreview.getWidth() * 0.02));
    }

    /**
     * convenience method for detecting if the mouse is eligible for selecting the edge of the slider
     *
     * @param mouseX
     * @return if the mouse is near a slider
     */
    private boolean inRightBuffer(double mouseX) {
        return mouseX > (selectedRight * imagePreview.getWidth() - (imagePreview.getWidth() * 0.02))
                && mouseX < (selectedRight * imagePreview.getWidth() + (imagePreview.getWidth() * 0.02));
    }

}
