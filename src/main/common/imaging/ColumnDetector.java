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
 * detects columns by finding darker-than-average rows
 */
public class ColumnDetector {

    private static final Logger logger = LoggerFactory.getLogger(ColumnDetector.class);

    /**
     * from an aligned image, find the probable x-locations of each column
     * 
     * @param image the image file to read from. must be aligned and cropped
     * @return the list of normalized x-locations, 0 to 1
     */
    static public ArrayList<Double> findColumns(File image) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.trace("reading {}", image.getAbsolutePath());
        Mat img = Imgcodecs.imread(image.getAbsolutePath());
        // convert to B&W
        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        // invert image
        Core.bitwise_not(gray, gray);
        Core.transpose(gray, gray);
        Mat vert_proj = new Mat();

        // collapse to 1d line
        Core.reduce(Lines.removeHorizontalLines(gray), vert_proj, 1, Core.REDUCE_AVG);

        // save a debug snapshot
        Core.transpose(vert_proj, vert_proj);
        Imgcodecs.imwrite("columns.png", vert_proj);
        // put everything back
        Core.transpose(vert_proj, vert_proj);

        // create a threshold
        Scalar th = new Scalar(10);
        Mat filtered_hist = new Mat();
        Core.compare(vert_proj, th, filtered_hist, Core.CMP_LE);

        ArrayList<Double> xcoords = new ArrayList<>();
        int y = 0;
        int count = 0;
        // keep track of empty areas
        boolean isSpace = false;
        // for each light area, find it's transition to dark
        for (int i = 0; i < gray.rows(); ++i) {
            if (isSpace) {
                if (filtered_hist.get(i, 0)[0] == 0) { // dark
                    isSpace = false;
                    xcoords.add((double) (y / count)); // mark it
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
        }
        // normalize coordinates
        for (int i = 0; i < xcoords.size(); i++) {
            xcoords.set(i, xcoords.get(i) / gray.rows());
        }
        return xcoords;

    }

}
