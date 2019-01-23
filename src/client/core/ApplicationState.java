package client.core;

import common.ScannedPaper;

import java.util.ArrayList;

/**
 * things needed throughout the lifecycle of the application
 *
 * @author Kurt Wilson
 */
public class ApplicationState {
    /**
     * images scanned
     */
    public static ArrayList<ScannedPaper> scannedImages = new ArrayList<>();
}
