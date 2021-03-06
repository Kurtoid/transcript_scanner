package main.shortcuts;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.client.ui.MainMenuController;
import main.client.ui.ResultBrowserController;
import main.common.FileManager;
import main.common.GradeReport;
import main.common.ParsedReport;
import main.common.imaging.ImagePreprocessor;
import main.common.imaging.PageReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class QuickScanRunner extends Application {

	private static final Logger logger = LoggerFactory.getLogger(QuickScanRunner.class);


	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FileManager.createTempFolder();
			FileManager.removeTempFiles();
		}catch(IOException e) {
			logger.error("couldnt clear temporary files!", e);
			logger.error("quitting to prevent further problems");
			System.exit(-1);
		}
		String imgPath = "image.jpg";
		File f = new File(imgPath);
		logger.info(" file {} exists: {}", f.getAbsolutePath(), f.exists());
		f = ImagePreprocessor.alignImage(f);
		double nameColumnLeft = 0.1681027868852459;
		double nameColumnRight = 0.337431693989071;
		double gradeColumnLeft = 0.4620879120879121;
		double gradeColumnRight = 0.4800693989071038;
		ParsedReport pr = PageReader.scanImage(new GradeReport(f), nameColumnLeft, nameColumnRight, gradeColumnLeft, gradeColumnRight);

		logger.info("stage started");
		FXMLLoader loader = new FXMLLoader(MainMenuController.class.getResource("ResultBrowser.fxml"));
		Parent root = loader.load();

		ResultBrowserController controller = loader.getController();
		Set<ParsedReport> reports = new HashSet<>();
		logger.debug("GPA is {}", pr.getGPA());
		reports.add(pr);
		controller.setReports(reports);

		Scene scene = new Scene(root);

		primaryStage.setTitle("Suncoast Transcript Scanner: TEST");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
}
