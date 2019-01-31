package tests;

import client.ui.MainMenuController;
import client.ui.ResultWindowController;
import common.FileManager;
import common.ScannedPaper;
import common.courses.Course;
import common.imaging.ImagePreprocessor;
import common.tesseract.OCRReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class QuickScanRunner extends Application {

	private static final Logger logger = LoggerFactory.getLogger(QuickScanRunner.class);


	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FileManager.removeTempFiles();
		}catch(IOException e) {
			logger.error("couldnt clear temporary files!", e);
			logger.error("quitting to prevent further problems");
			System.exit(-1);
		}
		String imgPath = "C:/Users/s26083758/Desktop/source.jpg";
		File f = new File(imgPath);
		logger.info(" file {} exists: {}", f.getAbsolutePath(), f.exists());
		f = ImagePreprocessor.alignImage(f);
		double nameColumnLeft = 0.1681027868852459;
		double nameColumnRight = 0.337431693989071;
		double gradeColumnLeft = 0.4620879120879121;
		double gradeColumnRight = 0.4800693989071038;
		Set<Course> courses = OCRReader.scanImage(new ScannedPaper(f), nameColumnLeft, nameColumnRight, gradeColumnLeft, gradeColumnRight);

		logger.info("stage started");
		FXMLLoader loader = new FXMLLoader(MainMenuController.class.getResource("ResultWindow.fxml"));
		Parent root = loader.load();

		Scene scene = new Scene(root, 300, 275);
		ResultWindowController controller = loader.getController();
		controller.setCourses(courses);

		primaryStage.setTitle("Suncoast Transcript Scanner: TEST");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
}
