package common.courses;

import javafx.scene.image.Image;

/**
 * represents a course taken by a student
 */
public class Course {
    public String courseID;
    public String courseDesc;
    public String grade;
    public Image gradeBox;

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

    public void setGrade(String parseGrade) {
        grade = parseGrade;
    }

    public void setGrade(String parseGrade, Image i) {
        if (parseGrade != null) {
            grade = parseGrade;
        } else {
            gradeBox = i;
        }
    }

    public Object getGrade() {
        if (grade != null)
            return grade;
        else
            return gradeBox;
    }
}
