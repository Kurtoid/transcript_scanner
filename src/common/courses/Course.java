package common.courses;

public class Course {
	public String courseID;
	public String courseDesc;
	public String grade;
	String getFullName() {
		return courseDesc + " " + courseID;
	}

	enum COURSE_TYPE {
		MATH,
		SCIENCE,
		ENGLISH,
		LANGUAGE,

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
