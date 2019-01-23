package common;

import java.io.File;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.image.Image;

public class ScannedPaper {
	public File file;
	public String id;
	private Image img;

	private static final Logger logger = LoggerFactory.getLogger(ScannedPaper.class);

	public ScannedPaper(File file) {
		this.file = file;
		id = UUID.randomUUID().toString();
		try {
			img = new Image(file.toURI().toString());
		} catch (RuntimeException e) {
			logger.error("problem loading image", e);
		}

	}

	public void updateImage() {
		img = new Image(file.toURI().toString());
	}

	public Image getImage() {
		return img;
	}
}
