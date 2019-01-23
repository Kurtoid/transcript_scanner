package common;

import common.courses.Course;

import java.util.ArrayList;

public class ParsedReport {
    ArrayList<Course> courses;

    @Override
    public String toString() {
        return "ParsedReport{" +
                "courses=" + courses.toString() +
                '}';
    }
}
