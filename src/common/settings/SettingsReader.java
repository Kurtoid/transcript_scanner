package common.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsReader {
    final static String filePath = "settings/defaultProperties";

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private boolean doesFileExist() {
        File f = new File(filePath);
        return f.isFile();
    }
}
