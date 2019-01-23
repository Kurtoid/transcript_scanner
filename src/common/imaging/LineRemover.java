package common.imaging;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class LineRemover {
	public static Mat removeLines(Mat src) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		// Load the image
//		Mat src = Imgcodecs.imread(f.getAbsolutePath());
		// Check if image is loaded fine
		if (src.empty())
			System.err.println("image not loaded!");
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
		Mat horizontalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(horizontalsize, 1));
		// Apply morphology operations
		Imgproc.erode(horizontal, horizontal, horizontalStructure, new Point(-1, -1));
		Imgproc.dilate(horizontal, horizontal, horizontalStructure, new Point(-1, -1));
		Core.subtract(gray, horizontal, horizontal);
		Core.bitwise_not(horizontal, horizontal);

		Imgcodecs.imwrite("removed.png", horizontal);

		Core.bitwise_not(horizontal, horizontal);
		return horizontal;
	}

//	public static void main(String[] args) {
//		removeLines(new File("rot.jpg"));
//	}
}
