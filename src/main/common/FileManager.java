package main.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileManager {
    public static final File TEMP_FOLDER = new File("temp");
	public static final File COURSES_FILE = new File("resources/allCourses.csv");

    public static void removeTempFiles() throws IOException {
        if (TEMP_FOLDER.exists())
            FileUtils.cleanDirectory(TEMP_FOLDER);
    }

    public static void createTempFolder() {
        TEMP_FOLDER.mkdirs();
    }
}
