package client.ui;

import client.core.ApplicationState;
import common.ScannedPaper;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import server.imaging.ImagePreprocessor;
import server.tesseract.OCRReader;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReadingWindow implements Initializable {

	public HBox imageContainer;
	public ScrollPane imageScroller;
	public Canvas imagePreview;
	public Button LoadImagesButton;
	public GridPane basePane;
	private ScannedPaper selectedImage;
	private double selectedLeft;
	private double selectedRight;
	private boolean selectionStarted = false;

	private double nameColumnLeft = -1;
	private double nameColumnRight = -1;

	private double gradeColumnRight = -1;
	private double gradeColumnLeft = -1;
	private EventHandler<MouseEvent> smallImageClicked = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			ImageView source = (ImageView) event.getSource();
//            imagePreview.setImage(source.getImage());
			System.out.println("clicked on " + source.getProperties().get("imageID"));
			for (ScannedPaper s : ApplicationState.scannedImages) {
				if (s.id.equals(source.getProperties().get("imageID"))) {
					selectedImage = s;
					selectedImage.updateImage();
				}
			}
			ChangeListener<Number> stageSizeListener = (observable, oldV, newV) -> updateCanvas();
			basePane.getScene().getWindow().widthProperty().addListener(stageSizeListener);
			basePane.getScene().getWindow().widthProperty().addListener(stageSizeListener);

			updateCanvas();
		}
	};
	public Pane canvasContainer;

	@FXML
	public void loadImages(ActionEvent actionEvent) {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.*"),
				new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"));
		List<File> images = fc.showOpenMultipleDialog(((Node) actionEvent.getTarget()).getScene().getWindow());
		if (images != null) {
			for (int i = 0; i < images.size(); i++) {
				ScannedPaper p = new ScannedPaper(images.get(i));
				ApplicationState.scannedImages.add(p);

//               imageViews[i].setF
			}
			showImages();
			/*
			 * for(File f : images){ System.out.println(ImagePreprocessor.getImageAngle(f));
			 * }
			 */
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
//        	System.out.println(selectedImage.file.getName());
//    		System.out.println(ImagePreprocessor.splitImage(selectedImage.file).getAbsolutePath());

			OCRReader.scanImage(selectedImage, nameColumnLeft, nameColumnRight);
//			OCRReader.scanImage(selectedImage, gradeColumnLeft, gradeColumnRight, 10);
		}
	}

	@FXML
	public void setGradeColumn(ActionEvent e) {
		gradeColumnLeft = selectedLeft;
		gradeColumnRight = selectedRight;
	}

	@FXML
	public void setNameColumn(ActionEvent e) {
		nameColumnLeft = selectedLeft;
		nameColumnRight = selectedRight;
	}

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
				public void handle(MouseEvent event) {
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
					updateCanvas();
				}

				private void moveRight(double x) {
					selectedRight = x / imagePreview.getWidth();
					if (selectedRight > 0.8)
						selectedRight = 0.8;
					if (Math.abs(selectedLeft - selectedRight) < 0.02)
						selectedRight = selectedLeft + 0.02;

				}

				private void moveLeft(double x) {
					selectedLeft = x / imagePreview.getWidth();
					if (selectedLeft > 0.8)
						selectedLeft = 0.8;
					if (Math.abs(selectedLeft - selectedRight) < 0.02)
						selectedLeft = selectedRight - 0.02;

				}
			});

		}
		GraphicsContext gc = imagePreview.getGraphicsContext2D();
		double scaleFactor;
		System.out.println(imagePreview.getHeight());
		System.out.println(selectedImage.getImage().getHeight());
		if (imagePreview.getHeight() / selectedImage.getImage().getHeight() > imagePreview.getWidth()
				/ selectedImage.getImage().getWidth()) {
			scaleFactor = imagePreview.getHeight() / selectedImage.getImage().getHeight();
		} else {
			scaleFactor = imagePreview.getWidth() / selectedImage.getImage().getWidth();

		}
		System.out.println(scaleFactor);
		if (selectedImage != null) {
			gc.drawImage(selectedImage.getImage(), 0, 0, selectedImage.getImage().getWidth() * scaleFactor,
					selectedImage.getImage().getHeight() * scaleFactor);
		}

		// draw current selection
		gc.setFill(new Color(0, 0, 0, .25));
		gc.fillRect(0, 0, selectedLeft * imagePreview.getWidth(), imagePreview.getHeight());
		gc.fillRect(selectedRight * imagePreview.getWidth(), 0, imagePreview.getWidth(), imagePreview.getHeight());

		if (nameColumnLeft != -1 && nameColumnRight != -1) {
			gc.setFill(new Color(1, 0, 0, .25));
			gc.fillRect(nameColumnLeft * imagePreview.getWidth(), 0,
					(nameColumnRight - nameColumnLeft) * imagePreview.getWidth(), imagePreview.getHeight());
		}
		if (gradeColumnLeft != -1 && gradeColumnRight != -1) {
			gc.setFill(new Color(0, 1, 0, .25));
			gc.fillRect(gradeColumnLeft * imagePreview.getWidth(), 0,
					(gradeColumnRight - gradeColumnLeft) * imagePreview.getWidth(), imagePreview.getHeight());

		}

	}

	private boolean inLeftBuffer(double mouseX) {
		return mouseX > (selectedLeft * imagePreview.getWidth() - (imagePreview.getWidth() * 0.02))
				&& mouseX < (selectedLeft * imagePreview.getWidth() + (imagePreview.getWidth() * 0.02));
	}

	private boolean inRightBuffer(double mouseX) {
		return mouseX > (selectedRight * imagePreview.getWidth() - (imagePreview.getWidth() * 0.02))
				&& mouseX < (selectedRight * imagePreview.getWidth() + (imagePreview.getWidth() * 0.02));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
}
