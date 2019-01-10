package server.tesseract;

import common.Course;
import common.ScannedPaper;
import common.csvreader.CoursesReader;
import common.dPair;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import server.imaging.ImagePreprocessor;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

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
     * scans an image in tesseract according to mode. Prints result for now
     * 
     * @param selectedImage the split image line
     * @param selectedLeft the right start bound (in decimal from 0 to 1)
     * @param selectedRight the left start bound
     * @param mode 7 is single line, 10 is single character
     */
    public static void scanImage(ScannedPaper selectedImage, double selectedLeft, double selectedRight, int mode) {
        //TODO: move this out
        CoursesReader cr = new CoursesReader();
        List<Course> courses = null;

        try {
            //TODO: move this string to constants
            courses = CoursesReader.getCoursesFromFile(new File("resources/mathCourses.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                System.out.println("Line: " + result.replace("\n", ""));
/*
                PriorityQueue<dPair> matches = new PriorityQueue<>();
                for (int i = 0; i < courses.size(); i++) {
                    Course c = courses.get(i);
                    //TODO: use ApacheCommons.Text.Lev
                    double dist = FuzzySearch.ratio(c.courseDesc + " " + c.courseID, result);
                    matches.add(new dPair(i, dist));
                }
                for (int i = 0; i < 5; i++) {
                    dPair dp = matches.poll();
                    System.out.println(courses.get(dp.key) + "\t" + dp.value);
                }
                //*/
            } catch (TesseractException e) {
                e.printStackTrace();
            }

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
