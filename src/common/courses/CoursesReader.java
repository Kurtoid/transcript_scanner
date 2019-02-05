package common.courses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * reads a list of courses in a csv file exported from CPALMS
 */
public class CoursesReader {
	static final Logger logger = LoggerFactory.getLogger(CoursesReader.class);
	public static List<Course> getCoursesFromFile(File f) throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(f));
		ArrayList<Course> courses = new ArrayList<>();
		r.readLine();
		String l = r.readLine();
		while (l != null) {
			Course c = new Course();
			c.courseID = (l.split(",")[0]);
			c.courseDesc = (l.split(",")[1]);
			c.courseDesc = c.courseDesc.replace(";", ",");
			c.type = l.split(",")[2].split("Subject: ")[1].split(" >")[0];
			courses.add(c);
			l = r.readLine();
		}
		return courses;
	}

	public static void main(String[] args) {
		File f = new File("resources/mathCourses.csv");
		try {
			logger.info(getCoursesFromFile(f).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("problem loading courses", e);
		}
	}
}
