package client.ui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import server.imaging.ImagePreprocessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadingWindow {

    public HBox imageContainer;
    public ScrollPane imageScroller;
    public ImageView imagePreview;
    public Button LoadImagesButton;

    List<File> allimages = new ArrayList<>();

    EventHandler<MouseEvent> smallImageClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ImageView source = (ImageView) event.getSource();
            imagePreview.setImage(source.getImage());
            System.out.println("clicked on " + source.getImage().getWidth());
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
            ImageView[] imageViews = new ImageView[images.size()];
            for (int i = 0; i < images.size(); i++) {

                imageViews[i] = new ImageView(images.get(i).toURI().toString());
//               imageViews[i].setFitHeight();
                imageViews[i].fitHeightProperty().bind(imageScroller.heightProperty());
                imageViews[i].setPreserveRatio(true);
                imageViews[i].setOnMouseClicked(smallImageClicked);
//               imageViews[i].setF
            }
            imageContainer.getChildren().addAll(imageViews);
            allimages.addAll(images);
/*
           for(File f : images){
               System.out.println(ImagePreprocessor.getImageAngle(f));
           }*/
        }
    }

    public void alignAllImages(ActionEvent actionEvent) {
        for (File f : allimages) {
            ImagePreprocessor.alignImage(f);
        }
        updateImages();
    }

    private void updateImages() {
        ObservableList<Node> children = imageContainer.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node iv = children.get(i);
            ImageView v = (ImageView) iv;
            v.setImage(new Image(allimages.get(i).toURI().toString()));

        }
    }
}
