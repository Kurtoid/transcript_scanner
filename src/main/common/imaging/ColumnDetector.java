package main.common.imaging;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

/**
 * used by 'Snap to Columns'
 */
public class ColumnDetector {

    private static final Logger logger = LoggerFactory.getLogger(ColumnDetector.class);

    static public ArrayList<Double> findColumns(File image) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.trace("reading {}", image.getAbsolutePath());
        Mat img = Imgcodecs.imread(image.getAbsolutePath());
        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
//        Rect roi = new Rect(0, (int) (gray.height() * .2), gray.width(), (int) (gray.height() * .8));
//        gray = new Mat(gray, roi);

        Core.bitwise_not(gray, gray);
        Core.transpose(gray, gray);
        Mat vert_proj = new Mat();
        Core.reduce(Lines.removeHorizontalLines(gray), vert_proj, 1, Core.REDUCE_AVG);
//		Core.reduce(gray, vert_proj, 1, Core.REDUCE_AVG);

        Core.transpose(vert_proj, vert_proj);

        Imgcodecs.imwrite("columns.png", vert_proj);

        // put everything back
        Core.transpose(vert_proj, vert_proj);
//		Core.transpose(gray, gray);

        Scalar th = new Scalar(10);
        Mat filtered_hist = new Mat();
        Core.compare(vert_proj, th, filtered_hist, Core.CMP_LE);
        logger.trace("filtered_hist", filtered_hist);

        ArrayList<Double> xcoords = new ArrayList<>();
        int y = 0;
        int count = 0;
        boolean isSpace = false;
        for (int i = 0; i < gray.rows(); ++i) {
//            if (filtered_hist.get(i, 0)[0] == 0) { // dark
//                xcoords.add((double) i);
//            }
//*
            if (isSpace) {
                if (filtered_hist.get(i, 0)[0] == 0) { // dark
                    isSpace = false;
                    xcoords.add((double) (y / count));
                } else {
                    y += i;
                    count++;
                }
            } else {
                if (filtered_hist.get(i, 0)[0] != 0) { // light
                    isSpace = true;
                    count = 1;
                    y = i;
                }
            }
//*/

        }
        for (int i = 0; i < xcoords.size(); i++) {
            xcoords.set(i, xcoords.get(i) / gray.rows());
        }
        return xcoords;

    }

}
