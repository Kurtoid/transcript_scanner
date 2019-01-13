package server.tesseract;

import common.ScannedPaper;
import common.courses.Course;
import common.courses.CourseMatcher;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import server.imaging.ImagePreprocessor;

import java.io.File;
import java.util.List;
import java.util.Objects;

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

    public static void scanImage(ScannedPaper selectedImage, double selectedLeft, double selectedRight) {
        scanImage(selectedImage, selectedLeft, selectedRight, 7);
    }

    /**
     * scans an image in tesseract according to mode, with left and right boundaries. Prints result for now
     * uses a temporary image to store intermediate crop
     * TODO: crop in memory only
     * TODO: clean-up cropped images
     *
     * @param selectedImage the split image line
     * @param selectedLeft  the right start bound (in decimal from 0 to 1)
     * @param selectedRight the left start bound
     * @param mode          7 is single line, 10 is single character
     */
    public static void scanImage(ScannedPaper selectedImage, double selectedLeft, double selectedRight, int mode) {
        //TODO: move this out
        List<Course> courses = null;

        ITesseract instance = new Tesseract1();

        instance.setTessVariable("psm", Integer.toString(mode));
        System.out.println(System.getenv("TESSDATA_PREFIX"));
//        instance.setDatapath("../" + System.getenv("TESSDATA_PREFIX"));
//		instance.setDatapath(LoadLibs.extractTessResources("tessdata").getParent());
        File folder = ImagePreprocessor.splitImage(selectedImage.file);


        for (File f : Objects.requireNonNull(folder.listFiles())) {
            try {
                File cropped = cropImage(f, selectedLeft, selectedRight);
                String result = (instance.doOCR(cropped));
//                result = result.replaceAll("IB", "International Baccalaureate");
                System.out.println("Line: " + result.replace("\n", "").toLowerCase());
                System.out.println(CourseMatcher.matchCourse(result.toLowerCase(), 1).get(0).toString());
            } catch (TesseractException e) {
                e.printStackTrace();
            }

        }


    }

    public static String doOcr(File f) {
        return doOcr(f, 7);
    }

    public static String doOcr(File f, int mode) {
        // TODO: maybe keep the instance as a static variable?
        ITesseract instance = new Tesseract1();

        instance.setTessVariable("psm", Integer.toString(mode));
        try {
            return (instance.doOCR(f));
        } catch (TesseractException e) {
            e.printStackTrace();
            return null;
        }

    }

    private static File cropImage(File f, double selectedLeft, double selectedRight) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img = Imgcodecs.imread(f.getAbsolutePath());
        f.delete();

        Rect roi = new Rect((int) (selectedLeft * img.width()), 0, (int) ((selectedRight - selectedLeft) * img.width()), img.height());
//        System.out.println(roi.toString());
//			System.out.println(roi.toString()+"\t"+i);
        Mat cropped = new Mat(img, roi);
        Imgcodecs.imwrite(f.getAbsolutePath(), cropped);
        return new File(f.toURI());
    }

}
