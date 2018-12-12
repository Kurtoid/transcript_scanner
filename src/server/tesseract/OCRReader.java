package server.tesseract;

import common.ScannedPaper;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import server.imaging.ImagePreprocessor;

import java.io.File;

public class OCRReader {
	public static void main(String[] args) {
		ITesseract instance = new Tesseract1();
		System.out.println(System.getenv("TESSDATA_PREFIX"));
//        instance.setDatapath("../" + System.getenv("TESSDATA_PREFIX"));
//		instance.setDatapath(LoadLibs.extractTessResources("tessdata").getParent());

		File imageFile = new File("rot.png");
		System.out.println(imageFile.exists());
		try {
			long time = System.currentTimeMillis();
			String result = instance.doOCR(imageFile);
			System.out.println(time - System.currentTimeMillis());
			System.out.println(result);
		} catch (TesseractException e) {
			e.printStackTrace();
		}

	}

	public static void scanImage(ScannedPaper selectedImage) {
		ITesseract instance = new Tesseract1();
		System.out.println(System.getenv("TESSDATA_PREFIX"));
//        instance.setDatapath("../" + System.getenv("TESSDATA_PREFIX"));
//		instance.setDatapath(LoadLibs.extractTessResources("tessdata").getParent());
		ImagePreprocessor.splitImage(selectedImage.file);
		File imageFile = selectedImage.file;
		System.out.println(imageFile.exists());
		try {
			long time = System.currentTimeMillis();
			String result = instance.doOCR(imageFile);
			System.out.println(time - System.currentTimeMillis());
			System.out.println(result);
		} catch (TesseractException e) {
			e.printStackTrace();
		}

	}
}
