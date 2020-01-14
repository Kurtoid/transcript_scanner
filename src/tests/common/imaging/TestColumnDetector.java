package tests.common.imaging;

import main.common.imaging.ColumnDetector;
import org.junit.jupiter.api.Test;
import tests.common.TestOCR;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestColumnDetector {
    @Test
    public void testFindColumns() {
        try {
            ArrayList<Double> columns = ColumnDetector.findColumns(new File(TestOCR.class.getResource("image.jpg").toURI()));
            assertTrue(columns.size() > 10);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
