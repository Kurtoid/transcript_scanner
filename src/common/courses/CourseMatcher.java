package common.courses;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * uses fuzzy search to find a course similar to a scanned one
 */
public class CourseMatcher {
	static final Logger logger = LoggerFactory.getLogger(CourseMatcher.class);

    /**
     * @param scannedLine the course scanned from a paper
     * @param matchLimit  maximum number of matches that can be returned
     * @return a list of courses similar to the scanned course
     */
    public static List<Course> matchCourse(String scannedLine, int matchLimit) {
        CoursesReader cr = new CoursesReader();
        List<Course> courses = new ArrayList<>();
        try {
            //TODO: move this string to constants
            courses = CoursesReader.getCoursesFromFile(new File("resources/allCourses.csv"));
        } catch (IOException e) {
			logger.error("problem loading courses", e);
        }
        PriorityQueue<dPair> matches = new PriorityQueue<>();
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
			double dist = FuzzySearch.ratio(c.courseDesc.toLowerCase() + " " + c.courseID.toLowerCase(), scannedLine);
            matches.add(new dPair(i, dist));
        }
        List<Course> c = new ArrayList<>();
        for (int i = 0; i < matchLimit; i++) {
            dPair dp = matches.poll();
            c.add(courses.get(dp.key));
//            System.out.println(courses.get(dp.key) + "\t" + dp.value);

        }
        return c;


    }

    /**
     * represents a pair sortable by value
     */
    private static class dPair implements Comparable<dPair> {
        public int key;
        public double value;

        public dPair(int i, double dist) {
            key = i;
            value = dist;
        }

        @Override
        public int compareTo(dPair o) {
            return -1 * Double.compare(this.value, o.value);
        }

    }
}
