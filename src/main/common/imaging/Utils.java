package main.common.imaging;

import static main.common.imaging.ImagePreprocessor.getFileName;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
	private final static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static int getImgWidth(File f) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img = Imgcodecs.imread(f.getAbsolutePath());
        return img.width();
    }

	/**
		 * crops an image given left and right percentage values
		 * creates a new file and manipulates that
		 * @param f the file to crop
	     * @param selectedLeft
	     * @param selectedRight
	     * @return a cropped image
		 */
		static File cropImage(File f, double selectedLeft, double selectedRight) {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			Mat img = Imgcodecs.imread(f.getAbsolutePath());
	//        f.delete();
	
			Rect roi = new Rect((int) (selectedLeft * img.width()), 0, (int) ((selectedRight - selectedLeft) * img.width()),
					img.height());
	//        System.out.println(roi.toString());
	//			System.out.println(roi.toString()+"\t"+i);
			Mat cropped = new Mat(img, roi);
			File result = new File(
					f.getParent() + File.separator + getFileName(f.getName()) + "_" + ((int) (Math.random() * 1000))
							+ ".png");
			logger.trace("saving file at {}", result.getAbsolutePath());
			Imgcodecs.imwrite(result.getAbsolutePath(), cropped);
			return (result);
		}
}
