package common.csvreader;

import common.Course;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CoursesReader {
	public static List<Course> getCoursesFromFile(File f) throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(f));
		ArrayList<Course> courses = new ArrayList<>();
		r.readLine();
		String l = r.readLine();
		while (l != null) {
			Course c = new Course();
			c.courseID = l.split(",")[0];
			c.courseDesc = l.split(",")[1];
			courses.add(c);
			l = r.readLine();
		}
		return courses;
	}

	public static void main(String[] args) {
		File f = new File("resources/mathCourses.csv");
		try {
			System.out.println(getCoursesFromFile(f).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
