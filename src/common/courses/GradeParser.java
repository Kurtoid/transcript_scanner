package common.courses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses scanned grade cells from the scanner handles duplicate letters,
 * extraneous letters, and extra whitespace
 * 
 * @author Kurt Wilson
 *
 */
public class GradeParser {

	private static final Logger logger = LoggerFactory.getLogger(GradeParser.class);

	static String[] validGrade = { "A", "B", "C", "D", "F", "P", "M", "W", "E" };
	// M: No grade, W: Withdraw, I: Incomplete, F: elementary school fail, E: exempt

	/**
	 * reads in a line from a paper cell and returns a grade
	 * does several simple stringent string slinging strategies to pick parsed pass grades
	 * @param grade String scanned from paper, raw from Tesseract
	 * @return Processed grade
	 */
	public static String parseGrade(String grade) {
		grade = grade.trim().toUpperCase();
		// remove extra characters
		grade = grade.replaceAll("[^A-Z]+", "");

		// empty string case
		if (grade.equals("")) {
			return "NONE";
		}

		// oh look, just one letter! that must be it
		if (grade.length() == 1) {
			return grade.toUpperCase();
		}
		// some ocr engines (looking at you tesseract) like to throw duplicates wherever it wants
		// so cover for that
		if (grade.charAt(0) == grade.charAt(1)) {
			return String.valueOf(grade.charAt(0));
		}
		// ok, that didn't work
		return null;
	}

	public static void main(String[] args) {
		logger.info(parseGrade("=a"));
	}

}
