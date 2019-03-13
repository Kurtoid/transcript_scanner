package main.client.ui;

import javafx.scene.canvas.Canvas;

/**
 * utility javaFX element for a canvas that will shrink when necessary
 * used for rendering column selection bars in ReadingWindow
 */
public class ResizableCanvas extends Canvas {

    public ResizableCanvas() {
        // Redraw canvas when size changes.
//        widthProperty().addListener(evt -> draw());
//        heightProperty().addListener(evt -> draw());
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}


