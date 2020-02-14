package main.client.core;

import main.common.GradeReport;

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
    final public static ArrayList<GradeReport> scannedImages = new ArrayList<>();
}
