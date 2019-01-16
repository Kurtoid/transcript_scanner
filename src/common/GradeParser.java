package common;

/**
 * Parses scanned grade cells from the scanner handles duplicate letters,
 * extraneous letters, and extra whitespace
 * 
 * @author Kurt Wilson
 *
 */
public class GradeParser {

	static String[] validGrade = { "A", "B", "C", "D", "F", "P" };

	/**
	 * reads in a line from a paper cell and returns a grade
	 * 
	 * @param grade String scanned from paper, raw from Tesseract
	 * @return Processed grade
	 */
	public static String parseGrade(String grade) {
		grade = grade.trim().toUpperCase();
		if (grade.equals("")) {
			return "NONE";
		}
		if (grade.length() == 1) {
			return grade.toUpperCase();
		}
		if (grade.charAt(0) == grade.charAt(1)) {
			return String.valueOf(grade.charAt(0));
		}
		return grade;
	}

}
