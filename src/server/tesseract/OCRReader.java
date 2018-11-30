package server.tesseract;

import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

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

}
