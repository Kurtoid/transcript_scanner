package main.common.imaging;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;

public class Utils {
    public static int getImgWidth(File f) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img = Imgcodecs.imread(f.getAbsolutePath());
        return img.width();
    }
}
