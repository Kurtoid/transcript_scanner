package tests.common.courses;

import main.common.ParsedReport;
import main.common.courses.Course;
import main.common.courses.PrereqChecker;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestPrereqChecker {
    @Test
    public void testCheckClassType() {
        Course math1 = new Course();
        math1.type = "MATH";
        math1.setGrade("A");
        Course math2 = new Course();
        math2.setGrade("A");
        math2.type = "MATH";

        Course sci1 = new Course();
        sci1.setGrade("A");
        sci1.type = "SCIENCE";
        Course sci2 = new Course();
        sci2.setGrade("A");
        sci2.type = "SCIENCE";

        Course wl1 = new Course();
        wl1.setGrade("A");
        wl1.type = "WORLDLANG";

        Set<Course> courses = new HashSet<>();
        courses.add(math1);
        courses.add(math2);
        courses.add(sci1);
        courses.add(sci2);
        courses.add(wl1);
        ParsedReport pr = new ParsedReport();
        pr.setCourses(courses);
        assertEquals(PrereqChecker.getMissingClasses(pr).size(), 0);

    }
}
