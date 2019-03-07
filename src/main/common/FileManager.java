package main.common;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileManager {
    public final static File temp = new File("temp");

    public static void removeTempFiles() throws IOException {
        if (temp.exists())
            FileUtils.cleanDirectory(temp);
    }

    public static void createTempFolder() {
        temp.mkdirs();
    }
}
