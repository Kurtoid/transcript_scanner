package main.common.imaging;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.opencv.imgproc.Imgproc.MORPH_RECT;

/**
 * removes horizontal lines from the image so it can be sliced by line
 */
public class Lines {
	final static Logger logger = LoggerFactory.getLogger(Lines.class);
	/**
	 * removes horizontal lines from the image
	 * hurts letter clarity, so dont scan directly from this
	 *
	 * @param src source image
	 * @return a reference to a new formatted image
	 */
	public static Mat removeHorizontalLines(Mat src) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		// Load the image
//		Mat src = Imgcodecs.imread(f.getAbsolutePath());
		// Check if image is loaded fine
		if (src.empty())
			logger.error("image not loaded!");
		// Transform source image to gray if it is not
		Mat gray = new Mat();
		if (src.channels() == 3) {
			Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		} else {
			gray = src;
		}
		Mat bw = new Mat();
//		Core.bitwise_not(gray, gray);
		Imgproc.adaptiveThreshold(gray, bw, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -2);
		Mat horizontal = bw.clone();

		// Specify size on horizontal axis
		int horizontalsize = 4;
		// Create structure element for extracting horizontal lines through morphology
		// operations
		Mat horizontalStructure = Imgproc.getStructuringElement(MORPH_RECT, new Size(horizontalsize, 1));
		// Apply morphology operations
		Imgproc.erode(horizontal, horizontal, horizontalStructure, new Point(-1, -1));
		Imgproc.dilate(horizontal, horizontal, horizontalStructure, new Point(-1, -1));
		Core.subtract(gray, horizontal, horizontal);
		Core.bitwise_not(horizontal, horizontal);

//		Imgcodecs.imwrite("removed.png", horizontal);

		Core.bitwise_not(horizontal, horizontal);
		return horizontal;
	}

    static public File getVerticalLines(File image) {
        logger.trace("aligning {}", image.getName());
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img = Imgcodecs.imread(image.getAbsolutePath());
        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);

        Mat thresh = new Mat();
        Core.bitwise_not(gray, thresh);

        Imgproc.threshold(thresh, thresh, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        // Specify size on vertical axis
        Mat vertical = thresh.clone();

        int verticalsize = 3;
        // Create structure element for extracting vertical lines through morphology operations
        Mat verticalStructure = Imgproc.getStructuringElement(MORPH_RECT, new Size(1, verticalsize));
        // Apply morphology operations
        Imgproc.erode(vertical, vertical, verticalStructure, new Point(-1, -1));
        Imgproc.dilate(vertical, vertical, verticalStructure, new Point(-1, -1));
        // Show extracted vertical lines

        Core.subtract(thresh, vertical, vertical);
        Imgcodecs.imwrite("vertical.jpg", vertical);
        return null;
    }

    public static void main(String[] args) {
        getVerticalLines(new File("image_rot.jpg"));
    }

}
