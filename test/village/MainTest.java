package village;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class MainTest {

    private Main main;

    @BeforeEach
    void MainTest() {
        main = new Main();
    }

    @Test
    void getValidRowInputFindsNoRowsWhenColumnIsFull() {
        main.board.addProject(new Location(0,2), '^');
        main.board.addProject(new Location(1,2), '^');
        main.board.addProject(new Location(2,2), '^');
        main.board.addProject(new Location(3,2), '^');
        main.board.addProject(new Location(4,2), '^');
        ArrayList<String> validInpt = main.getValidRowInput(2);
        assertEquals(0, validInpt.size());
    }

    @Test
    void getValidRowInputFindsOneAvailableRow() {
        main.board.addProject(new Location(0,2), '^');
        main.board.addProject(new Location(1,2), '^');
        main.board.addProject(new Location(2,2), '^');
        main.board.addProject(new Location(3,2), '^');
        ArrayList<String> validInpt = main.getValidRowInput(2);
        assertTrue(validInpt.contains("5"));
    }

    @Test
    void getValidRowInputFindsMultipleAvailableRows() {
        main.board.addProject(new Location(1,2), '^');
        main.board.addProject(new Location(3,2), '^');
        ArrayList<String> validInpt = main.getValidRowInput(2);
        // returns what the user can type (0 + 1) = row 1
        assertTrue(validInpt.contains("1"));
        assertFalse(validInpt.contains("2"));
        assertTrue(validInpt.contains("3"));
        assertFalse(validInpt.contains("4"));
        assertTrue(validInpt.contains("5"));
    }

    @Test
    void getValidColInputGetsAllEmptyColumns() {
        ArrayList<String> validInpt = main.getValidColInput();
        assertEquals(6, validInpt.size());
    }

    @Test
    void getValidColInputDoesntFindFullColumns() {
        main.board.addProject(new Location(0,2), '^');
        main.board.addProject(new Location(1,2), '^');
        main.board.addProject(new Location(2,2), '^');
        main.board.addProject(new Location(3,2), '^');
        main.board.addProject(new Location(4,2), '^');
        main.board.addProject(new Location(0,3), '^');
        main.board.addProject(new Location(1,3), '^');
        main.board.addProject(new Location(2,3), '^');
        main.board.addProject(new Location(3,3), '^');
        main.board.addProject(new Location(4,3), '^');
        ArrayList<String> validInpt = main.getValidColInput();
        assertEquals(4, validInpt.size());
        assertTrue(validInpt.contains("1"));
        assertTrue(validInpt.contains("2"));
        assertFalse(validInpt.contains("3"));
        assertFalse(validInpt.contains("4"));
        assertTrue(validInpt.contains("5"));
        assertTrue(validInpt.contains("6"));
    }
}
