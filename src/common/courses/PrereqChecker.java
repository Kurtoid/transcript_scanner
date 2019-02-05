package common.courses;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * makes sure a list of Courses matches the minimum requirements to attend
 */
public class PrereqChecker {
    final static private Logger logger = LoggerFactory.getLogger(PrereqChecker.class);

    static final String[] classTypes = {"Art - Visual Arts", "CLEP Credits via CAP", "Computer Education", "Dance", "Drama - Theatre Arts", "Driver Education and Traffic Safety", "English/Language Arts", "Experiential Education", "Health Education", "Humanities", "JROTC and Military Training", "Leadership Skills Development", "Library Media", "Mathematics", "Music Education", "Peer Counseling", "Physical Education", "Research and Critical Thinking", "Science", "Social Studies", "Study Hall", "Temporary Instructional Placement", "World Languages"};

    static String getClassType(String className) {
        String result = "";
        Double maxscore = Double.MIN_VALUE;
        for (String s : classTypes) {
            double dist = FuzzySearch.ratio(className, s);
            if (dist > maxscore) {
                maxscore = dist;
                result = s;
            }
        }
        return result;
    }
}
