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
    public String type;
    String getFullName() {
        return courseDesc + " " + courseID;
    }


    public void setGrade(String parseGrade) {
        grade = parseGrade;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseID='" + courseID + '\'' +
                ", courseDesc='" + courseDesc + '\'' +
                ", grade='" + grade + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public void setGrade(String parseGrade, File i) {
            grade = parseGrade;
            gradeBox = i;

    }
    public ImageView getGrade() {
        return new ImageView(gradeBox.toURI().toString());
    }

    public void setDesc(String toUpperCase) {
        courseDesc = toUpperCase;
    }
}
