package main.common;

import main.common.courses.Course;

import java.util.ArrayList;
import java.util.Set;

public class ParsedReport {
    ArrayList<Course> courses;

    @Override
    public String toString() {
        return "ParsedReport{" +
                "courses=" + courses.toString() +
                '}';
    }

    double GPA;

    public double getGPA() {
        return GPA;
    }

    public void setCourses(Set<Course> c) {
        courses = new ArrayList<>();
        courses.addAll(c);
        this.GPA = calculateGPA();

    }

    /**
     * super complicated patent-pending top secret proprietary GPA calculator
     */
    private double calculateGPA() {
        double total = 0;
        for (Course c : courses) {
            switch (c.grade) {
                case "A":
                    total += 4;
                    break;
                case "B":
                    total += 3;
                    break;
                case "C":
                    total += 2;
                    break;
                case "D":
                    total += 1;
                    break;
                default:
                    total += 0;
                    break;
            }
        }
        return total / courses.size();
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setGPA(double gpa) {
        this.GPA = gpa;
    }
}
