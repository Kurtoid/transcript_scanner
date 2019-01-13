package common.courses;

public class Course {
	public String courseID;
	public String courseDesc;

	String setFullName() {
		return courseDesc + " " + courseID;
	}

	@Override
	public String toString() {
		return "Course [courseID=" + courseID + ", courseDesc=" + courseDesc + "]";
	}
}
