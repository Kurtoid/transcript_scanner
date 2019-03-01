package main.common.tesseract;

import main.common.GradeReport;
import main.common.ParsedReport;
import main.common.courses.Course;
import main.common.courses.CourseMatcher;
import main.common.courses.GradeParser;
import main.common.imaging.ImagePreprocessor;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static main.common.imaging.ImagePreprocessor.getFileName;

/**
 * convenience functions for using Tesseract
 */
public class OCRReader {
	final static Logger logger = LoggerFactory.getLogger(OCRReader.class);
	static ITesseract instance;


	/**
	 * scans an image in tesseract according to mode, with left and right
	 * boundaries. Prints result for now uses a temporary image to store
	 * intermediate crop TODO: crop in memory only
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
		List<BoxResult> boxes = new ArrayList<>();
		for (File f : folder) {
			try {
				if (headerFound) {
					instance.setPageSegMode(ITessAPI.TessPageSegMode.PSM_SINGLE_LINE);
					Column nameCol = findColumn("Course", boxes);
					File cropped = cropImage(f, nameCol.begin, nameCol.end);
					String result = (instance.doOCR(cropped));
					if (!result.trim().equals("")) {
//                result = result.replaceAll("IB", "International Baccalaureate");

						logger.debug("Line: " + result.replace("\n", ""));
						Course course = (CourseMatcher.matchCourse(result.toLowerCase(), 1).get(0));
//						instance.setPageSegMode(ITessAPI.TessPageSegMode.PSM_SINGLE_CHAR);
						Column gradeCol = findColumn("Grade", boxes);
						logger.trace("grade found at {}", gradeCol.toString());
						File cropped_letter = cropImage(f, gradeCol.begin, gradeCol.end);
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
							logger.debug("the next line should be some table headers... lets look for that");
						} else {
							gpa = 0;
						}
						gpaFound = true;
					}
				}
				if (!headerFound) {
					String wholeLine = doOcr(f);
					logger.trace("read whole line: {}", wholeLine.trim());
					double score = FuzzySearch.ratio("Year Marking Period Course Course Number Percent Grade Grade Scale Cred. Attempted Cred. Earned GPA PTS Weighted GPA Affects GPA Teacher", wholeLine.trim());
					logger.trace("Header score: {}", score);
					if (score > 80) {
						headerFound = true;
						try {
							boxes = getBoxes(f);
						} catch (IOException e) {
							e.printStackTrace();
						}
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

	public static List<BoxResult> getBoxes(File f) throws IOException, TesseractException {

		Mat img = Imgcodecs.imread(f.getAbsolutePath());
//        f.delete();


		ITesseract instance = new Tesseract1();

		instance.setPageSegMode(ITessAPI.TessPageSegMode.PSM_SINGLE_LINE);
		int level = ITessAPI.TessPageIteratorLevel.RIL_WORD;
		List<Rectangle> result = instance.getSegmentedRegions(ImageIO.read(f), level);
		List<BoxResult> boxes = new ArrayList<>();
		String[] words = doOcr(f).trim().split(" ");
		for (int i = 0; i < result.size(); i++) {
			Rectangle rect = result.get(i);
			BoxResult b = new BoxResult();
			b.x = rect.x / (double) img.width();
			b.w = rect.width / (double) img.width();
			b.word = words[i];
			boxes.add(b);
			logger.info(String.format("Box[%d]: x=%d, y=%d, w=%d, h=%d word=%s", i, rect.x, rect.y, rect.width, rect.height, words[i]));
//				Imgproc.line(img, new org.opencv.core.Point(0, xcoords.get(i) * img.rows()), new Point(img.cols(),
//						xcoords.get(i) * img.rows()), new Scalar(0, 255, 0));
			Imgproc.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		}

		File boxed = new File("boxes.png");
//		logger.trace("saving file at {}", boxed.getAbsolutePath());
//		Imgcodecs.imwrite(boxed.getAbsolutePath(), img);
		Collections.sort(boxes);
		return boxes;

	}

	// TODO: multi word columns
	static Column findColumn(String name, List<BoxResult> boxes) {
		for (int i = 0; i < boxes.size(); i++) {
			BoxResult r = boxes.get(i);
			if (FuzzySearch.ratio(r.word, name) > 75) {
				Column c = new Column();
				c.begin = r.x;
				if (i == boxes.size() - 1) {
					c.end = c.begin + r.w;
				} else {
					c.end = boxes.get(i + 1).x;
				}
				c.header = name;
				c.begin -= 0.004;
				c.end -= 0.004;
				return c;
			}
		}
		return null;
	}
	/**
	 * runs tesseract on an image by PSM mode
	 *
	 * @param f    file to be read
	 * @param mode mode to use; see https://github.com/tesseract-ocr/tesseract/wiki/Command-Line-Usage
	 * @return
	 */
	public static String doOcr(File f, int mode) {
		if (instance == null) {
			instance = new Tesseract1();
		}

//		instance.setTessVariable("psm", Integer.toString(mode));
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

	static class BoxResult implements Comparable<BoxResult> {
		String word;
		double x;
		double w;

		@Override
		public int compareTo(BoxResult o) {
			return Double.compare(this.x, o.x);
		}
	}

	static class Column {
		double begin;
		double end;
		String header;

		@Override
		public String toString() {
			return "Column{" +
					"begin=" + begin +
					", end=" + end +
					", header='" + header + '\'' +
					'}';
		}

	}

}
