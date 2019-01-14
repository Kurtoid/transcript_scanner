package common;

import java.io.File;
import java.util.UUID;

import javafx.scene.image.Image;

public class ScannedPaper {
	public File file;
	public String id;
	private Image img;

	public ScannedPaper(File file) {
		this.file = file;
		id = UUID.randomUUID().toString();
		try {
			img = new Image(file.toURI().toString());
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public void updateImage() {
		img = new Image(file.toURI().toString());
	}

	public Image getImage() {
		return img;
	}
}
