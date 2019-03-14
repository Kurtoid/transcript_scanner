package tests.ui;

import javafx.scene.control.TableColumn;
import main.client.ui.ResultWindowController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestResultWindowController {
    @Test
    public void testMakeTableColumnsEditable() {
        TableColumn[] cols = new ResultWindowController().getColumns();
        assertTrue(cols[0].isEditable()); // course title
        assertTrue(cols[1].isEditable()); // course grade
        assertFalse(cols[2].isEditable()); // course grade image
        assertFalse(cols[3].isEditable()); //


    }
}
