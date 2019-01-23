package tests;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.FileManager;
import common.ScannedPaper;
import common.imaging.ImagePreprocessor;
import server.tesseract.OCRReader;

import java.io.File;
import java.io.IOException;

public class QuickScanRunner {
	static double nameColumnLeft = 0.1681027868852459;
	static double nameColumnRight = 0.337431693989071;

	static double gradeColumnLeft = 0.4620879120879121;
	static double gradeColumnRight = 0.4800693989071038;

	static String imgPath = "image.jpg";

	private static final Logger logger = LoggerFactory.getLogger(QuickScanRunner.class);

	public static void main(String[] args) {
		try {
		FileManager.removeTempFiles();
		}catch(IOException e) {
			logger.error("couldnt clear temporary files!");
			logger.error("quitting to prevent further problems");
			System.exit(-1);
		}
		File f = ImagePreprocessor.alignImage(new File(imgPath));
		OCRReader.scanImage(new ScannedPaper(f), nameColumnLeft, nameColumnRight, gradeColumnLeft, gradeColumnRight);


	}

}
