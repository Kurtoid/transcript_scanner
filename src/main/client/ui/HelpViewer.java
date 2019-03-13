package main.client.ui;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

/**
 * adapted from https://docs.oracle.com/javafx/2/webview/jfxpub-webview.htm
 * @author Alla Redko, modified by Kurt Wilson
 */
public class HelpViewer extends Application {
    private Scene scene;
    @Override public void start(Stage stage) {
        // create the scene
        stage.setTitle("Help");
        try {
            System.out.println(new File("resources/help/help.html").toURI().toURL().toExternalForm());
            scene = new Scene(new Browser(new File("resources/help/help.html").toURI().toURL().toExternalForm()),750,500, Color.web("#666970"));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}

/**
 * javaFX embeddable html renderer
 */
class Browser extends Region {

    private final WebView browser = new WebView();
    private final WebEngine webEngine = browser.getEngine();

    public Browser(String url) {
        webEngine.load(url);
        //add the web view to the scene
        getChildren().add(browser);

    }

    private Node createSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }

    @Override protected double computePrefWidth(double height) {
        return 750;
    }

    @Override protected double computePrefHeight(double width) {
        return 500;
    }
}