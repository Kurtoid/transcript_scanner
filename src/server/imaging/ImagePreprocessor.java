package server.imaging;

import common.FileManager;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImagePreprocessor {
	public static void main(String[] args) {
		System.out.println(getFileName("test.jpg"));
		splitImage(new File("temp/image.jpg"));
		/*
		 * System.loadLibrary(Core.NATIVE_LIBRARY_NAME); File imageFile = new
		 * File("image.jpg"); System.out.println(imageFile.exists()); Mat img =
		 * Imgcodecs.imread(imageFile.getAbsolutePath()); Mat gray = new Mat();
		 * Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY); Core.bitwise_not(gray,
		 * gray);
		 * 
		 * Mat thresh = new Mat(); Imgproc.threshold(gray, thresh, 0, 255,
		 * Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
		 * 
		 * Imgcodecs.imwrite("thresh.png", thresh); // HashSet<Point> coords = new
		 * HashSet<>(); ArrayList<Point> coords = new ArrayList<>(); for (int i = 0; i <
		 * thresh.rows(); i++) { for (int j = 0; j < thresh.cols(); j++) { //
		 * System.out.println(thresh.get(i, j)[0]); if (thresh.get(i, j)[0] > 0) {
		 * coords.add(new Point(i, j)); } } } // Imgproc.cv MatOfPoint2f points = new
		 * MatOfPoint2f(); points.fromList(coords); RotatedRect angle =
		 * Imgproc.minAreaRect(points); if (angle.angle < -45) angle.angle =
		 * -(angle.angle + 90); else angle.angle *= -1; System.out.println(angle.angle);
		 * 
		 * double w, h; h = thresh.size().height; w = thresh.size().width;
		 * 
		 * Point center = new Point(w / 2, h / 2); Mat rotMat =
		 * Imgproc.getRotationMatrix2D(center, angle.angle, 1.0); Mat rotated = new
		 * Mat(); Imgproc.warpAffine(gray, rotated, rotMat, new Size(w, h),
		 * Imgproc.INTER_CUBIC, Core.BORDER_REPLICATE);
		 * 
		 * Mat horiz_proj = new Mat(); Core.reduce(rotated, horiz_proj, 1,
		 * Core.REDUCE_AVG); // Core.bitwise_not(horiz_proj, horiz_proj);
		 * Imgcodecs.imwrite("line.png", horiz_proj);
		 * 
		 * // System.out.println(horiz_proj.dump()); Scalar th = new Scalar(10); Mat
		 * filtered_hist = new Mat(); Core.compare(horiz_proj, th, filtered_hist,
		 * Core.CMP_LE); // System.out.println(filtered_hist.dump());
		 * 
		 * // Get mean coordinate of white white pixels groups ArrayList<Integer>
		 * ycoords = new ArrayList<>(); int y = 0; int count = 0; boolean isSpace =
		 * false; for (int i = 0; i < rotated.rows(); ++i) { if (!isSpace) { if
		 * (filtered_hist.get(i, 0)[0] != 0) { isSpace = true; count = 1; y = i; } }
		 * else { if (filtered_hist.get(i, 0)[0] == 0) { isSpace = false; ycoords.add(y
		 * / count); } else { y += i; count++; } } } //
		 * System.out.println(ycoords.toString());
		 * 
		 * Imgproc.cvtColor(rotated, rotated, Imgproc.COLOR_GRAY2BGR); for (int i = 0; i
		 * < ycoords.size(); ++i) { Imgproc.line(rotated, new Point(0, ycoords.get(i)),
		 * new Point(rotated.cols(), ycoords.get(i)), new Scalar(0, 255, 0)); }
		 * Imgcodecs.imwrite("rot.png", rotated); //
		 */
	}

	public static double getImageAngle(File imageFile) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        File imageFile = new File("image.jpg");
//        System.out.println(imageFile.exists());
		Mat img = Imgcodecs.imread(imageFile.getAbsolutePath());
		Mat gray = new Mat();
		Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
		Core.bitwise_not(gray, gray);

		Mat thresh = new Mat();
		Imgproc.threshold(gray, thresh, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

//        Imgcodecs.imwrite("thresh.png", thresh);
//        HashSet<Point> coords = new HashSet<>();
		ArrayList<Point> coords = new ArrayList<>();
		for (int i = 0; i < thresh.rows(); i++) {
			for (int j = 0; j < thresh.cols(); j++) {
//                System.out.println(thresh.get(i, j)[0]);
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
//        System.out.println(angle.angle);
		return angle.angle;
		/*
		 * double w, h; h = thresh.size().height; w = thresh.size().width;
		 * 
		 * Point center = new Point(w / 2, h / 2); Mat rotMat =
		 * Imgproc.getRotationMatrix2D(center, angle.angle, 1.0); Mat rotated = new
		 * Mat(); Imgproc.warpAffine(gray, rotated, rotMat, new Size(w, h),
		 * Imgproc.INTER_CUBIC, Core.BORDER_REPLICATE);
		 * 
		 * Mat horiz_proj = new Mat(); Core.reduce(rotated, horiz_proj, 1,
		 * Core.REDUCE_AVG); // Core.bitwise_not(horiz_proj, horiz_proj);
		 * Imgcodecs.imwrite("line.png", horiz_proj);
		 * 
		 * // System.out.println(horiz_proj.dump()); Scalar th = new Scalar(10); Mat
		 * filtered_hist = new Mat(); Core.compare(horiz_proj, th, filtered_hist,
		 * Core.CMP_LE); // System.out.println(filtered_hist.dump());
		 * 
		 * // Get mean coordinate of white white pixels groups ArrayList<Long> ycoords =
		 * new ArrayList<>(); long y = 0; int count = 0; boolean isSpace = false; for
		 * (int i = 0; i < img.rows(); ++i) { if (!isSpace) { if (filtered_hist.get(i,
		 * 0)[0] != 0) { isSpace = true; count = 1; y = i; } } else { if
		 * (filtered_hist.get(i, 0)[0] == 0) { isSpace = false; ycoords.add(y / count);
		 * } else { y += i; count++; } }
		 * 
		 * } return ycoords;
		 * 
		 * Imgproc.cvtColor(rotated, rotated, Imgproc.COLOR_GRAY2BGR); for (int i = 0; i
		 * < ycoords.size(); ++i) { Imgproc.line(rotated, new Point(0, ycoords.get(i)),
		 * new Point(rotated.cols(), ycoords.get(i)), new Scalar(0, 255, 0)); }
		 * Imgcodecs.imwrite("rot.png", rotated); //
		 */
	}

	public static File alignImage(File imageFile) {
		System.out.println(imageFile.getName());
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        File imageFile = new File("image.jpg");
//        System.out.println(imageFile.exists());
		Mat img = Imgcodecs.imread(imageFile.getAbsolutePath());
		Mat gray = new Mat();
		Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);

		Mat thresh = new Mat();
		Core.bitwise_not(gray, thresh);

		Imgproc.threshold(thresh, thresh, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

//        Imgcodecs.imwrite("thresh.png", thresh);
//        HashSet<Point> coords = new HashSet<>();
		ArrayList<Point> coords = new ArrayList<>();
		for (int i = 0; i < thresh.rows(); i++) {
			for (int j = 0; j < thresh.cols(); j++) {
//                System.out.println(thresh.get(i, j)[0]);
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
		System.out.println(angle.angle);
//        return angle.angle;
//*
		double w, h;
		h = thresh.size().height;
		w = thresh.size().width;

		Point center = new Point(w / 2, h / 2);
		Mat rotMat = Imgproc.getRotationMatrix2D(center, angle.angle, 1.0);
		Mat rotated = new Mat();
		Imgproc.warpAffine(gray, rotated, rotMat, new Size(w, h), Imgproc.INTER_CUBIC, Core.BORDER_REPLICATE);

		Imgcodecs.imwrite(FileManager.temp.getAbsolutePath() + File.separator + imageFile.getName(), rotated);
		File outDir = new File(FileManager.temp.getAbsolutePath() + File.separator + imageFile.getName());
		return outDir;
//        Imgcodecs.imwrite("rot.png", rotated);

	}

	static public File splitImage(File image) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//      File imageFile = new File("image.jpg");
//      System.out.println(imageFile.exists());
		Mat img = Imgcodecs.imread(image.getAbsolutePath());
		Mat gray = new Mat();
		Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
		Core.bitwise_not(gray, gray);
		Mat horiz_proj = new Mat();
		Core.reduce(gray, horiz_proj, 1, Core.REDUCE_AVG);
//        Core.bitwise_not(horiz_proj, horiz_proj);
//		Imgcodecs.imwrite("line.png", horiz_proj);

//        System.out.println(horiz_proj.dump());
		Scalar th = new Scalar(10);
		Mat filtered_hist = new Mat();
		Core.compare(horiz_proj, th, filtered_hist, Core.CMP_LE);
//        System.out.println(filtered_hist.dump());

		// Get mean coordinate of white white pixels groups
		ArrayList<Long> ycoords = new ArrayList<>();
		long y = 0;
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
					ycoords.add(y / count);
				} else {
					y += i;
					count++;
				}
			}

		}
//		System.out.println(ycoords.toString());

		Imgproc.cvtColor(gray, gray, Imgproc.COLOR_GRAY2BGR);
		for (int i = 0; i < ycoords.size(); ++i) {
			Imgproc.line(gray, new Point(0, ycoords.get(i)), new Point(gray.cols(), ycoords.get(i)),
					new Scalar(0, 255, 0));
		}

		File folder = new File(
				FileManager.temp.getAbsolutePath() + File.separator + getFileName(image.getName()));
		folder.mkdirs();

		File f = new File(folder.getAbsolutePath() + File.separator + image.getName());
		System.out.println("saving new images as " + f.getAbsolutePath());


		Imgcodecs.imwrite(f.getAbsolutePath(), gray);
		return f;

	}

	private static String getFileName(String line) {
		String expression = "^(.*)\\.";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(line);
		matcher.find();
		return matcher.group(1);
	}
}
