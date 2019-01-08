package common;

import javafx.scene.image.Image;

import java.io.File;
import java.util.UUID;

public class ScannedPaper {
    public File file;
    public String id;
    private Image img;

    public ScannedPaper(File file) {
        this.file = file;
        id = UUID.randomUUID().toString();
        img = new Image(file.toURI().toString());
    }

    public void updateImage() {
        img = new Image(file.toURI().toString());
    }

    public Image getImage() {
        return img;
    }
}
