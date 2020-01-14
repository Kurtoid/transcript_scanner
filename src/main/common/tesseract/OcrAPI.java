package main.common.tesseract;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;

public class OcrAPI {
	private final static Logger logger = LoggerFactory.getLogger(OcrAPI.class);

	public static ITesseract instance;

	public static String doOcr(File f) {
		return OcrAPI.doOcr(f, 7);
	}

	/**
		 * runs tesseract on an image by PSM mode
		 *
		 * @param f    file to be read
		 * @param mode mode to use; see https://github.com/tesseract-ocr/tesseract/wiki/Command-Line-Usage
	     * @return
		 */
		public static String doOcr(File f, int mode) {
		if (instance == null) {
			instance = new Tesseract1();
			}
	
	//		instance.setTessVariable("psm", Integer.toString(mode));
			try {
			return (instance.doOCR(f));
			} catch (TesseractException e) {
			logger.error("ocr error", e);
				return null;
			}
	
		}

}
