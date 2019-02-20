package tests;

import common.GradeReport;
import common.imaging.ImagePreprocessor;
import common.tesseract.OCRReader;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OCRTests {
    @Test
    public void TestOCROnImage() {
        String imgPath = "image.jpg";
        File f = new File(imgPath);

        f = ImagePreprocessor.alignImage(f);
        double nameColumnLeft = 0.1681027868852459;
        double nameColumnRight = 0.337431693989071;
        double gradeColumnLeft = 0.4620879120879121;
        double gradeColumnRight = 0.4800693989071038;

        assertTrue(OCRReader.scanImage(new GradeReport(f), nameColumnLeft, nameColumnRight, gradeColumnLeft, gradeColumnRight).getCourses().size() == 15);
    }

}
