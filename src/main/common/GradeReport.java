package main.common;

import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;

public class GradeReport {
	public File file;
	public final String id;
	private Image img;
	public String name = "";
	private static final Logger logger = LoggerFactory.getLogger(GradeReport.class);

	public GradeReport(File file) {
		this.file = file;
		id = UUID.randomUUID().toString();
		try {
			img = new Image(file.toURI().toString());
		} catch (RuntimeException e) {
            // handle it silently
            // logger.error("problem loading image", e);
		}

	}

	public void updateImage() {
		img = new Image(file.toURI().toString());
	}

	public Image getImage() {
		return img;
	}
}
