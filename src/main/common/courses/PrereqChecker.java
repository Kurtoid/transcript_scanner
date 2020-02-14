package main.common.courses;

import main.common.ParsedReport;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * makes sure a list of Courses matches the minimum requirements to attend
 */
public class PrereqChecker {
    final static private Logger logger = LoggerFactory.getLogger(PrereqChecker.class);

    private static HashMap<CLASSTYPES, Integer> requirements;

    static CLASSTYPES getType(String name) {
        ArrayList<CLASSTYPES> types = new ArrayList<>(Arrays.asList(CLASSTYPES.values()));
        return FuzzySearch.extractOne(name, types, CLASSTYPES::getName).getReferent();
    }

    static double MIN_GPA = 2.5;

    public static Set<CLASSTYPES> getMissingClasses(ParsedReport report) {
        initRequirements();
        HashMap<CLASSTYPES, Integer> counts = new HashMap<>();
        for (Course c : report.getCourses()) {
            CLASSTYPES type = getType(c.type);
            if (counts.containsKey(type)) {
                counts.put(type, counts.get(type) + 1);
            } else {
                counts.put(type, 1);
            }
        }
        boolean meetsReq = true;
        Set<CLASSTYPES> missingTypes = new HashSet<>();
        for (Map.Entry<CLASSTYPES, Integer> set : requirements.entrySet()) {
            if (counts.containsKey(set.getKey())) {
                if (counts.get(set.getKey()) >= set.getValue()) {
                    // meets requirements for this category
                } else {
                    // doesnt meet requirements
                    meetsReq = false;
                    missingTypes.add(set.getKey());
                }
            } else {
                meetsReq = false;
                missingTypes.add(set.getKey());
            }
        }
        return missingTypes;
    }

    private static void initRequirements() {
        if (requirements == null) {
            requirements = new HashMap<>();
            requirements.put(CLASSTYPES.MATH, 2);
            requirements.put(CLASSTYPES.SCIENCE, 2);
            requirements.put(CLASSTYPES.WORLDLANG, 1);
        }
    }

    public static HashMap<CLASSTYPES, Integer> getRequirements() {
        initRequirements();
        return requirements;
    }

    public enum CLASSTYPES {
        ART("Art - Visual Arts"),
        CLEP("CLEP Credits via CAP"),
        COMPUTER("Computer Education"),
        DANCE("Dance"),
        DRAMA("Drama - Theatre Arts"),
        DRIVER("Driver Education and Traffic Safety"),
        ENGLISH("English/Language Arts"),
        EXPERIENTAL("Experiential Education"),
        HEALTH("Health Education"),
        HUMANITIES("Humanities"),
        JROTC("JROTC and Military Training"),
        LEAD("Leadership Skills Development"),
        LIBRARY("Library Media"),
        MATH("Mathematics"),
        MUSIC("Music Education"),
        PEERCOUNSELING("Peer Counseling"),
        PHYSICAL("Physical Education"),
        RESEARCH("Research and Critical Thinking"),
        SCIENCE("Science"),
        SOCIALSTUDIES("Social Studies"),
        STUDYHALL("Study Hall"),
        TEMP("Temporary Instructional Placement"),
        WORLDLANG("World Languages"),

        ;
        final String name;

        CLASSTYPES(String s) {
            this.name = s;
        }

        public String getName() {
            return name;
        }
    }

}
