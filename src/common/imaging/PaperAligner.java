package common.imaging;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.ORB;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class PaperAligner {
    static int MAX_FEATURES = 1000;

    static File alignPaper(File source, File target) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        File imageFile = new File("image.jpg");
//        System.out.println(imageFile.exists());
        Mat source_mat = Imgcodecs.imread(source.getAbsolutePath());
        Mat target_mat = Imgcodecs.imread(target.getAbsolutePath());

        Imgproc.cvtColor(source_mat, source_mat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(target_mat, target_mat, Imgproc.COLOR_BGR2GRAY);
        MatOfKeyPoint kp1 = new MatOfKeyPoint();
        MatOfKeyPoint kp2 = new MatOfKeyPoint();

        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();

        ORB orb = ORB.create(MAX_FEATURES);
        orb.detectAndCompute(source_mat, new Mat(), kp1, descriptors1);
        orb.detectAndCompute(target_mat, new Mat(), kp2, descriptors2);
        MatOfDMatch matches = new MatOfDMatch();
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        matcher.match(descriptors1, descriptors2, matches, new Mat());
        DMatch[] m = matches.toArray();
        Arrays.sort(m, new MatchComparator());
        int numGoodMatches = (int) (m.length * 0.2);
        m = Arrays.copyOfRange(m, 0, numGoodMatches);
        matches = new MatOfDMatch(m);
        Mat out = new Mat();
        Features2d.drawMatches(source_mat, kp1, target_mat, kp2, matches, out);
        Imgcodecs.imwrite("yeet.png", out);

        Point[] p1 = new Point[(int) matches.size().height];
        Point[] p2 = new Point[(int) matches.size().height];
//    MatOfPoint2f p1 = new MatOfPoint2f();
//    MatOfPoint2f p2 = new MatOfPoint2f();


        KeyPoint[] kp1_a = kp1.toArray();
        KeyPoint[] kp2_a = kp2.toArray();

        System.out.println(matches.size().height);
        for (int i = 0; i < matches.size().height; i++) {
            p1[i] = (kp1_a[m[i].queryIdx].pt);
            p2[i] = (kp2_a[m[i].trainIdx].pt);
        }
        Mat h = Calib3d.findHomography(new MatOfPoint2f(p1), new MatOfPoint2f(p2), Calib3d.RANSAC);
        Mat result = new Mat();
        Imgproc.warpPerspective(source_mat, result, h, target_mat.size());
        Imgcodecs.imwrite("yeet2.png", result);

        return null;
    }


    public static void main(String[] args) {
        alignPaper(new File("target.jpg"), new File("source.jpg"));
    }

    public static class MatchComparator implements Comparator<DMatch> {
        @Override
        public int compare(DMatch o1, DMatch o2) {
            return Float.compare(o1.distance, o2.distance);
        }
    }
}
