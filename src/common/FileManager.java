package common;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileManager {
    public static File temp = new File("temp");

    public static void removeTempFiles() throws IOException {
        if (temp.exists())
            FileUtils.cleanDirectory(temp);
    }
}
