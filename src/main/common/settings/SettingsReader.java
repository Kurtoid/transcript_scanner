package main.common.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * read settings from a file
 * not used yet
 */
public class SettingsReader {
    private final static String filePath = "settings/defaultProperties";
	private static Logger logger = LoggerFactory.getLogger(SettingsReader.class);
    public SettingsReader() {

    }

    public static void main(String[] args) {
        Properties defaultProps = new Properties();
        try {

            FileInputStream in = new FileInputStream("settings/defaultProperties");
            defaultProps.load(in);
            in.close();
            System.out.println(defaultProps.get("ip"));

            FileOutputStream out = new FileOutputStream("settings/defaultProperties");
            defaultProps.put("thingy", "val");
            defaultProps.store(out, "somethign");

            out.close();

        } catch (IOException e) {
			logger.error("Problem reading settings", e);
        }

    }

    private boolean doesFileExist() {
        File f = new File(filePath);
        return f.isFile();
    }
}
