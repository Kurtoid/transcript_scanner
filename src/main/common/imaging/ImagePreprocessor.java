package main.common.imaging;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.common.FileManager;

/**
 * convenience functions to manipulate images
 */
public class ImagePreprocessor {

	private static final Logger logger = LoggerFactory.getLogger(ImagePreprocessor.class);

	public static void main(String[] args) {
		splitImage(new File("rot.jpg"));
	}

	/**
	 * aligns an image from a file
	 * creates a temporary file, and manipulates that one
	 * returns a reference to the new file
	 *
	 * @param imageFile a skewed file fresh from the scanner
	 * @return the newly aligned file
	 */
	public static File alignImage(File imageFile) {
		logger.trace("aligning {}", imageFile.getName());
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat img = Imgcodecs.imread(imageFile.getAbsolutePath());
		Mat gray = new Mat();
		Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);

		Mat thresh = new Mat();
		Core.bitwise_not(gray, thresh);

		Imgproc.threshold(thresh, thresh, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

		ArrayList<Point> coords = new ArrayList<>();
		for (int i = 0; i < thresh.rows(); i++) {
			for (int j = 0; j < thresh.cols(); j++) {
				if (thresh.get(i, j)[0] > 0) {
					coords.add(new Point(i, j));
				}
			}
		} // Imgproc.cv
		MatOfPoint2f points = new MatOfPoint2f();
		points.fromList(coords);
		RotatedRect angle = Imgproc.minAreaRect(points);
		if (angle.angle < -45)
			angle.angle = -(angle.angle + 90);
		else
			angle.angle *= -1;
		logger.trace("angle is {}", angle.angle);
//*
		double w, h;
		h = thresh.size().height;
		w = thresh.size().width;

		Point center = new Point(w / 2, h / 2);
		Mat rotMat = Imgproc.getRotationMatrix2D(center, angle.angle, 1.0);
		Mat rotated = new Mat();
		Imgproc.warpAffine(gray, rotated, rotMat, new Size(w, h), Imgproc.INTER_CUBIC, Core.BORDER_REPLICATE);

		Imgcodecs.imwrite(FileManager.TEMP_FOLDER.getAbsolutePath() + File.separator + imageFile.getName(), rotated);
		return new File(FileManager.TEMP_FOLDER.getAbsolutePath() + File.separator + imageFile.getName());
//        Imgcodecs.imwrite("rot.png", rotated);

	}

	/**
	 * @param image already-aligned, original color image
	 * @return file folder location of split files
	 */
	static public LinkedList<File> splitImage(File image) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat img = Imgcodecs.imread(image.getAbsolutePath());
		Mat gray = new Mat();
		Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
		Core.bitwise_not(gray, gray);
		Mat horiz_proj = new Mat();
		Core.reduce(Lines.removeHorizontalLines(gray), horiz_proj, 1, Core.REDUCE_AVG);

		Scalar th = new Scalar(10);
		Mat filtered_hist = new Mat();
		Core.compare(horiz_proj, th, filtered_hist, Core.CMP_LE);
		ArrayList<Integer> ycoords = new ArrayList<>();
		int y = 0;
		int count = 0;
		boolean isSpace = false;
		for (int i = 0; i < img.rows(); ++i) {
			if (!isSpace) {
				if (filtered_hist.get(i, 0)[0] != 0) {
					isSpace = true;
					count = 1;
					y = i;
				}
			} else {
				if (filtered_hist.get(i, 0)[0] == 0) {
					isSpace = false;
					ycoords.add((y / count));
				} else {
					y += i;
					count++;
				}
			}

		}
		File folder = new File(FileManager.TEMP_FOLDER.getAbsolutePath() + File.separator + getFileName(image.getName()));
		folder.mkdirs();
		logger.debug("saving new images at " + folder.getAbsolutePath());
		Core.bitwise_not(gray, gray);
		LinkedList<File> files = new LinkedList<>();
		// for each line marked
		for (int i = 0; i < ycoords.size() - 1; i++) {
			// create cropping rectangle between this line and the next
			Rect roi = new Rect(0, Math.max(0, ycoords.get(i) + 2), gray.width(),
					Math.min(((int) ycoords.get(i + 1) - (ycoords.get(i))) - 3, gray.height()));
			Mat cropped = new Mat(gray, roi); // crop the image
			// create a temp file based off the the image name
			File f = new File(
					folder.getAbsolutePath() + File.separator + getFileName(image.getName()) + "_" + i + ".png");
			files.add(f);
			Imgcodecs.imwrite(f.getAbsolutePath(), cropped); // add and save

		}
		return files;


	}

	public static String getFileName(String line) {
		String expression = "^(.*)\\.";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(line);
		matcher.find();
		return matcher.group(1);
	}
}
