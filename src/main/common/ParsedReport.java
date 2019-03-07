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
            if (c.grade.equals("A"))
                total += 4;
            else if (c.grade.equals("B"))
                total += 3;
            else if (c.grade.equals("C"))
                total += 2;
            else if (c.grade.equals("D"))
                total += 1;
            else
                total += 0;
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
