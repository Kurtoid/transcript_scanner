package common.courses;

/**
 * represents a course taken by a student
 */
public class Course {
	public String courseID;
	public String courseDesc;
	public String grade;

	String getFullName() {
		return courseDesc + " " + courseID;
	}

	@Override
	public String toString() {
		return "Course{" +
				"courseID='" + courseID + '\'' +
				", courseDesc='" + courseDesc + '\'' +
				", grade='" + grade + '\'' +
				'}';
	}
}
