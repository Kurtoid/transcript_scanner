package tests;

import common.FileManager;
import common.ScannedPaper;
import common.imaging.ImagePreprocessor;
import common.tesseract.OCRReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class QuickScanRunner {

	private static final Logger logger = LoggerFactory.getLogger(QuickScanRunner.class);

	public static void main(String[] args) {
		try {
		FileManager.removeTempFiles();
		}catch(IOException e) {
			logger.error("couldnt clear temporary files!", e);
			logger.error("quitting to prevent further problems");
			System.exit(-1);
		}
		String imgPath = "image.jpg";
		File f = ImagePreprocessor.alignImage(new File(imgPath));
		double nameColumnLeft = 0.1681027868852459;
		double nameColumnRight = 0.337431693989071;
		double gradeColumnLeft = 0.4620879120879121;
		double gradeColumnRight = 0.4800693989071038;
		OCRReader.scanImage(new ScannedPaper(f), nameColumnLeft, nameColumnRight, gradeColumnLeft, gradeColumnRight);

	}

}
