package server.tesseract;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class OCRReader {
    public static void main(String[] args) {
        ITesseract instance = new Tesseract1();
        System.out.println(System.getenv("TESSDATA_PREFIX"));
//        instance.setDatapath("../" + System.getenv("TESSDATA_PREFIX"));
        File imageFile = new File("D:/School/2018/CS/project/images/rot.png");
        System.out.println(imageFile.exists());
        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            e.printStackTrace();
        }

    }

}
