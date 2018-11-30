package client.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class ReadingWindow {

    public HBox imageContainer;
    public ScrollPane imageScroller;
    public ImageView imagePreview;
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
//                imageViews[i].setOnMouseClicked(smallImageClicked);
//               imageViews[i].setF
            }
            imageContainer.getChildren().addAll(imageViews);
/*
           for(File f : images){
               System.out.println(ImagePreprocessor.getImageAngle(f));
           }*/
        }
    }

}
