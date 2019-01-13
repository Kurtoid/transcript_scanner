package common.courses;

import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class CourseMatcher {
    public static List<Course> matchCourse(String s, int limit) {
        CoursesReader cr = new CoursesReader();
        List<Course> courses = new ArrayList<>();
        try {
            //TODO: move this string to constants
            courses = CoursesReader.getCoursesFromFile(new File("resources/allCourses.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PriorityQueue<dPair> matches = new PriorityQueue<>();
        for (int i = 0; i < courses.size(); i++) {
            Course c = courses.get(i);
            double dist = FuzzySearch.ratio(c.courseDesc + " " + c.courseID, s);
            matches.add(new dPair(i, dist));
        }
        List<Course> c = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            dPair dp = matches.poll();
            c.add(courses.get(dp.key));
//            System.out.println(courses.get(dp.key) + "\t" + dp.value);

        }
        return c;


    }

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
