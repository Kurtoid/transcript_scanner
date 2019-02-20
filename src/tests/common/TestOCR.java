package tests.common;

import main.common.GradeReport;
import main.common.imaging.ImagePreprocessor;
import main.common.tesseract.OCRReader;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestOCR {
    final static Logger logger = LoggerFactory.getLogger(TestOCR.class);

    @Test
    public void TestOCROnImage() {
        URL imgPath = TestOCR.class.getResource("image.jpg");
        File f = new File(imgPath.getPath());
        logger.info(" file {} exists: {}", f.getAbsolutePath(), f.exists());
        f = ImagePreprocessor.alignImage(f);
        double nameColumnLeft = 0.1681027868852459;
        double nameColumnRight = 0.337431693989071;
        double gradeColumnLeft = 0.4620879120879121;
        double gradeColumnRight = 0.4800693989071038;

        assertEquals(15, OCRReader.scanImage(new GradeReport(f), nameColumnLeft, nameColumnRight, gradeColumnLeft, gradeColumnRight).getCourses().size());
    }

}
