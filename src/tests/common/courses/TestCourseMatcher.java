package tests.common.courses;

import main.common.courses.CourseMatcher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCourseMatcher {
    @Test
    public void testCourseMatchByDesc() {
        assertEquals(CourseMatcher.matchCourseByDesc("pre-calculus").type, "Mathematics");
    }
}
