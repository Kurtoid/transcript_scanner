package common.courses;

import common.ParsedReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * makes sure a list of Courses matches the minimum requirements to attend
 */
public class PrereqChecker {
    final static private Logger logger = LoggerFactory.getLogger(PrereqChecker.class);

    static final String[] classTypes = {"Art - Visual Arts", "CLEP Credits via CAP", "Computer Education", "Dance", "Drama - Theatre Arts", "Driver Education and Traffic Safety", "English/Language Arts", "Experiential Education", "Health Education", "Humanities", "JROTC and Military Training", "Leadership Skills Development", "Library Media", "Mathematics", "Music Education", "Peer Counseling", "Physical Education", "Research and Critical Thinking", "Science", "Social Studies", "Study Hall", "Temporary Instructional Placement", "World Languages"};
    static double MIN_GPA = 2.5;
    private static HashMap<String, Integer> requirements;

    public static boolean hadRequiredClasses(ParsedReport report) {
        initRequirements();
        return false;
    }

    private static void initRequirements() {
        if (requirements == null) {
            requirements = new HashMap<>();
        }
    }

}
