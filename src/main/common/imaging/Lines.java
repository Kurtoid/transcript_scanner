package main.common.imaging;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
