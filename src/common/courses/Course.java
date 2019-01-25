package common.courses;

import javafx.scene.image.ImageView;

import java.io.File;

/**
 * represents a course taken by a student
 */
public class Course {
    public String courseID;
    public String courseDesc;
    public String grade;
    public File gradeBox;

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

    public void setGrade(String parseGrade, File i) {
            grade = parseGrade;
            gradeBox = i;

    }

    public ImageView getGrade() {
        return new ImageView(gradeBox.toURI().toString());
    }
}
