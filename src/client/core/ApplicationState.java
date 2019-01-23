package client.core;

import common.ScannedPaper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class ApplicationState {
    final static Logger logger = LoggerFactory.getLogger(ApplicationState.class);


    public static ArrayList<ScannedPaper> scannedImages = new ArrayList<>();


}
