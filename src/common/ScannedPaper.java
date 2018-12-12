package common;

import java.io.File;
import java.util.UUID;

public class ScannedPaper {
    public File file;
    public String id;

    public ScannedPaper(File file) {
        this.file = file;
        id = UUID.randomUUID().toString();
    }
}
