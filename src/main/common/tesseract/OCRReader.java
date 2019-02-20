package main.common.tesseract;

import main.common.GradeReport;
import main.common.ParsedReport;
import main.common.courses.Course;
import main.common.courses.CourseMatcher;
import main.common.courses.GradeParser;
import main.common.imaging.ImagePreprocessor;
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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static main.common.imaging.ImagePreprocessor.getFileName;

/**
 * convenience functions for using Tesseract
 */
public class OCRReader {
	final static Logger logger = LoggerFactory.getLogger(OCRReader.class);


	/**
	 * scans an image in tesseract according to mode, with left and right
	 * boundaries. Prints result for now uses a temporary image to store
	 * intermediate crop TODO: crop in memory only TODO: clean-up cropped images
	 *
	 * @param selectedImage the split image line
	 */
	public static ParsedReport scanImage(GradeReport selectedImage, double nameSelectedLeft, double nameSelectedRight,
										 double gradeSelectedLeft, double gradeSelectedRight) {
		ITesseract instance = new Tesseract1();

		logger.trace("TESSDATA_PREFIX: {}", System.getenv("TESSDATA_PREFIX"));
//        instance.setDatapath("../" + System.getenv("TESSDATA_PREFIX"));
//		instance.setDatapath(LoadLibs.extractTessResources("tessdata").getParent());
		LinkedList<File> folder = ImagePreprocessor.splitImage(selectedImage.file);
		Set<Course> foundCourses = new HashSet<>();
		logger.debug("looking for course header");
		logger.debug("looking for GPA");
		boolean headerFound = false;
		boolean gpaFound = false;
		double gpa = 0;
		for (File f : folder) {
			try {
				if (!gpaFound) {
					String wholeLine = doOcr(f);
					logger.trace("read whole line: {}", wholeLine.trim());
					if (isGPALine(wholeLine.trim())) {
						Pattern pat = Pattern.compile("([0-9]\\.[0-9]{4})");
						Matcher mat = pat.matcher(wholeLine);
						mat.find();
//						String gpaFilter = wholeLine.replaceAll("[^0-9]+", " ");
						if (!mat.hitEnd()) {
							logger.debug("GPA should be here somewhere: {}", mat.group(1));
							gpa = Double.parseDouble(mat.group(1));
						} else {
							gpa = 0;
						}
						gpaFound = true;
					}
				}
				instance.setTessVariable("psm", "7");
				File cropped = cropImage(f, nameSelectedLeft, nameSelectedRight);
				String result = (instance.doOCR(cropped));
				logger.trace("read class line {}", result.trim());
				if (!result.trim().equals("")) {
					if (!headerFound) {
						if (FuzzySearch.ratio(result.toLowerCase(), "course") > 50) {
							logger.info("course header found!");
							headerFound = true;
						}
					} else {
//                result = result.replaceAll("IB", "International Baccalaureate");
						logger.debug("Line: " + result.replace("\n", ""));
						Course course = (CourseMatcher.matchCourse(result.toLowerCase(), 1).get(0));
						instance.setTessVariable("psm", "10");
						File cropped_letter = cropImage(f, gradeSelectedLeft, gradeSelectedRight);
						String grade = instance.doOCR(cropped_letter);
						course.setGrade(GradeParser.parseGrade(grade), cropped_letter);
						logger.info("detected course {}", course);

						if (course.grade.equals("P")) {
							logger.info("found elementary school classes, so lets quit here");
							break;
						}
						foundCourses.add(course);
					}
				}
			} catch (TesseractException e) {
				logger.error("problem parsing paper", e);
			}

		}
		if (!headerFound) {
			logger.warn("We never found a header!!! (thats bad)");
		}
		ParsedReport p = new ParsedReport();
		p.setCourses(foundCourses);
		p.setGPA(gpa);
		return p;

	}

	private static boolean isGPALine(String wholeLine) {
//        logger.trace("GPA search score: {}", FuzzySearch.partialRatio(wholeLine, "Cumulative GPA: "));
//        logger.trace("CUM WEIGHTED GPA search score: {}", FuzzySearch.partialRatio(wholeLine, "Cumulative Weighted GPA: "));
		double cumGPAScore = FuzzySearch.partialRatio(wholeLine, "Cumulative GPA:");
		double cumWeightedGPAScore = FuzzySearch.partialRatio(wholeLine, "Cumulative Weighted GPA:");
		return cumGPAScore > 50 && cumGPAScore > cumWeightedGPAScore;
	}

	public static String doOcr(File f) {
		return doOcr(f, 7);
	}

	/**
	 * runs tesseract on an image by PSM mode
	 *
	 * @param f    file to be read
	 * @param mode mode to use; see https://github.com/tesseract-ocr/tesseract/wiki/Command-Line-Usage
	 * @return
	 */
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

	/**
	 * crops an image given left and right percentage values
	 * creates a new file and manipulates that
	 * @param f the file to crop
	 * @param selectedLeft
	 * @param selectedRight
	 * @return a cropped image
	 */
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
		logger.trace("saving file at {}", result.getAbsolutePath());
		Imgcodecs.imwrite(result.getAbsolutePath(), cropped);
		return (result);
	}

}
