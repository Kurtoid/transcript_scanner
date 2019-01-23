package common.tesseract;

import common.GradeParser;
import common.ScannedPaper;
import common.courses.Course;
import common.courses.CourseMatcher;
import common.imaging.ImagePreprocessor;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static common.imaging.ImagePreprocessor.getFileName;

public class OCRReader {
	final static Logger logger = LoggerFactory.getLogger(OCRReader.class);

	public static void main(String[] args) {
		ITesseract instance = new Tesseract1();
		logger.debug("tessdata at {}", System.getenv("TESSDATA_PREFIX"));
//        instance.setDatapath("../" + System.getenv("TESSDATA_PREFIX"));
//		instance.setDatapath(LoadLibs.extractTessResources("tessdata").getParent());

		File imageFile = new File("rot.png");
		try {
			long time = System.currentTimeMillis();
			String result = instance.doOCR(imageFile);
			logger.info(Long.toString(time - System.currentTimeMillis()));
			logger.info(result);
		} catch (TesseractException e) {
			logger.error("some problem", e);
		}

	}

	/**
	 * scans an image in tesseract according to mode, with left and right
	 * boundaries. Prints result for now uses a temporary image to store
	 * intermediate crop TODO: crop in memory only TODO: clean-up cropped images
	 *
	 * @param selectedImage the split image line
	 */
	public static void scanImage(ScannedPaper selectedImage, double nameSelectedLeft, double nameSelectedRight,
			double gradeSelectedLeft, double gradeSelectedRight) {
		// TODO: move this out
		List<Course> courses = null;

		ITesseract instance = new Tesseract1();

		logger.trace("TESSDATA_PREFIX: {}", System.getenv("TESSDATA_PREFIX"));
//        instance.setDatapath("../" + System.getenv("TESSDATA_PREFIX"));
//		instance.setDatapath(LoadLibs.extractTessResources("tessdata").getParent());
		LinkedList<File> folder = ImagePreprocessor.splitImage(selectedImage.file);

		logger.debug("looking for course header");
		boolean headerFound = false;
		for (File f : folder) {
			try {
				File cropped = cropImage(f, nameSelectedLeft, nameSelectedRight);
				instance.setTessVariable("psm", "7");

				String result = (instance.doOCR(cropped));
				if (!result.trim().equals("")) {
					if (!headerFound) {
						if (FuzzySearch.ratio(result.toLowerCase(), "course") > 50) {
							logger.info("course header found!");
							headerFound = true;
						}
					} else {
//                result = result.replaceAll("IB", "International Baccalaureate");
						logger.debug("Line: " + result.replace("\n", ""));
						logger.info(CourseMatcher.matchCourse(result.toLowerCase(), 1).get(0).toString());
						instance.setTessVariable("psm", "10");
						String grade = instance.doOCR(cropImage(f, gradeSelectedLeft, gradeSelectedRight));
						logger.info("grade: {} Parsed: {}", grade, GradeParser.parseGrade(grade));
					}
				}
			} catch (TesseractException e) {
				logger.error("problem parsing paper", e);
			}

		}
		if (!headerFound) {
			logger.warn("We never found a header!!! (thats bad)");
		}

	}

	public static String doOcr(File f) {
		return doOcr(f, 7);
	}

	public static String doOcr(File f, int mode) {
		// TODO: maybe keep the instance as a static variable?
		ITesseract instance = new Tesseract1();

		instance.setTessVariable("psm", Integer.toString(mode));
		try {
			return (instance.doOCR(f));
		} catch (TesseractException e) {
			logger.error("ocr error", e);
			return null;
		}

	}

	private static File cropImage(File f, double selectedLeft, double selectedRight) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat img = Imgcodecs.imread(f.getAbsolutePath());
//        f.delete();

		Rect roi = new Rect((int) (selectedLeft * img.width()), 0, (int) ((selectedRight - selectedLeft) * img.width()),
				img.height());
//        System.out.println(roi.toString());
//			System.out.println(roi.toString()+"\t"+i);
		Mat cropped = new Mat(img, roi);
		File result = new File(
				f.getParent() + File.separator + getFileName(f.getName()) + "_" + ((int) (Math.random() * 1000))
						+ ".png");
		/*
		 * Imgproc.cvtColor(cropped, cropped, Imgproc.COLOR_BGR2GRAY);
		 * Imgproc.threshold(cropped, cropped, 40, 255, Imgproc.THRESH_BINARY |
		 * Imgproc.THRESH_OTSU);
		 */ /*
			 * Core.bitwise_not(cropped, cropped); double erosion_size = 1; Mat element =
			 * Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * erosion_size +
			 * 1, 2 * erosion_size + 1)); Imgproc.erode(cropped, cropped, element);
			 * erosion_size = 1; element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
			 * new Size(2 * erosion_size + 1, 2 * erosion_size + 1));
			 *
			 * Imgproc.dilate(cropped, cropped, element);
			 *
			 * Core.bitwise_not(cropped, cropped);
			 */
//		System.out.println(result.getAbsolutePath());
		Imgcodecs.imwrite(result.getAbsolutePath(), cropped);
		return (result);
	}

}