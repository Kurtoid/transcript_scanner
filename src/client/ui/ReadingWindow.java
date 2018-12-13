package client.ui;

import client.core.ApplicationState;
import common.ScannedPaper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import server.imaging.ImagePreprocessor;
import server.tesseract.OCRReader;

import java.io.File;
import java.util.List;

public class ReadingWindow {

    public HBox imageContainer;
    public ScrollPane imageScroller;
    public ImageView imagePreview;
    public Button LoadImagesButton;
    ScannedPaper selectedImage;
    EventHandler<MouseEvent> smallImageClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ImageView source = (ImageView) event.getSource();
            imagePreview.setImage(source.getImage());
            System.out.println("clicked on " + source.getProperties().get("imageID"));
            for (ScannedPaper s : ApplicationState.scannedImages) {
                if (s.id.equals(source.getProperties().get("imageID"))) {
                    selectedImage = s;
                }
            }
        }
    };

    @FXML
    public void loadImages(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        List<File> images = fc.showOpenMultipleDialog(((Node) actionEvent.getTarget()).getScene().getWindow());
        if (images != null) {
            for (int i = 0; i < images.size(); i++) {
                ScannedPaper p = new ScannedPaper(images.get(i));
                ApplicationState.scannedImages.add(p);

//               imageViews[i].setF
            }
            showImages();
            /*
           for(File f : images){
               System.out.println(ImagePreprocessor.getImageAngle(f));
           }*/
        }
    }

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

    public void alignAllImages(ActionEvent actionEvent) {
        for (ScannedPaper f : ApplicationState.scannedImages) {
            f.file = ImagePreprocessor.alignImage(f.file);
        }
        showImages();
    }

    private void updateImages() {
        ObservableList<Node> children = imageContainer.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node iv = children.get(i);
            ImageView v = (ImageView) iv;
//            v.setImage(new Image(allimages.get(i).toURI().toString()));

        }
    }

    @FXML
    public void scanSelectedImage(ActionEvent actionEvent) {
        if (selectedImage != null) {
        	System.out.println(selectedImage.file.getName());
    		System.out.println(ImagePreprocessor.splitImage(selectedImage.file).getAbsolutePath());

//            OCRReader.scanImage(selectedImage);
        }
    }
}
