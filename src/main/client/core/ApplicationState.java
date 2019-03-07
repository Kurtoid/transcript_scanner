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
    public final static ArrayList<GradeReport> scannedImages = new ArrayList<>();
}
