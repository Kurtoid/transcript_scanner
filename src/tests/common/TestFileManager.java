package tests.common;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFileManager {
    final static Logger logger = LoggerFactory.getLogger(TestFileManager.class);

    @Test
    public void testRemoveTempFiles() {
        main.common.FileManager.createTempFolder();
        File f = main.common.FileManager.TEMP_FOLDER;
        File newFile = new File(f, "empty.file");
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(newFile.exists());
        try {
            main.common.FileManager.removeTempFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertFalse(newFile.exists());
    }
}
