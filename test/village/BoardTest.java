package village;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void toStringWorks() {
        String correct = "[-      1  2  3  4  5  6]\n" +
                         "[3/4   -3 -0 -2 -2 -0 -3]\n" +
                         "[5/6   -0 -1 -0 -0 -1 -0]\n" +
                         "[7     -2 -0 -1 -1 -0 -2]\n" +
                         "[8/9   -0 -1 -0 -0 -1 -0]\n" +
                         "[10/11 -3 -0 -2 -2 -0 -3]\n";
        Board board = new Board();
        assertEquals(board.toString(), correct);
    }

    @Test
    void addProjectWorksWithOneProject(){
        Board board = new Board();
        String correct =    "[-      1  2  3  4  5  6]\n" +
                            "[3/4   -3 -0 -2 -2 -0 -3]\n" +
                            "[5/6   -0 ^1 -0 -0 -1 -0]\n" +
                            "[7     -2 -0 -1 -1 -0 -2]\n" +
                            "[8/9   -0 -1 -0 -0 -1 -0]\n" +
                            "[10/11 -3 -0 -2 -2 -0 -3]\n";
        board.addProject(new Location(1, 1), '^');
        assertEquals(board.toString(), correct);
    }

    @Test
    void addProjectWorksWithMultipleProjects(){
        Board board = new Board();
        String correct =    "[-      1  2  3  4  5  6]\n" +
                            "[3/4   -3 -0 -2 -2 -0 -3]\n" +
                            "[5/6   -0 ^1 -0 -0 -1 -0]\n" +
                            "[7     -2 -0 -1 #1 -0 -2]\n" +
                            "[8/9   -0 -1 O0 -0 -1 -0]\n" +
                            "[10/11 -3 H0 -2 -2 -0 -3]\n";
        board.addProject(new Location(1, 1), '^');
        board.addProject(new Location(2, 3), '#');
        board.addProject(new Location(3, 2), 'O');
        board.addProject(new Location(4, 1), 'H');
        assertEquals(board.toString(), correct);
    }

    @Test
    void findsNoProjectsOffBoard() {
        Board board = new Board();
        board.addProject(new Location(0,0), '^');
        board.addProject(new Location(1,0), '^');
        board.addProject(new Location(2,0), '^');
        board.addProject(new Location(3,0), '^');
        board.addProject(new Location(4,0), '^');

        board.addProject(new Location(0,1), '^');
        board.addProject(new Location(1,1), '^');
        board.addProject(new Location(2,1), '^');
        board.addProject(new Location(3,1), '^');
        board.addProject(new Location(4,1), '^');

        ArrayList<Integer> contents = board.findPlayableColumns(0);
        assertEquals(contents.size(), 1);
        assertEquals(contents.get(0), 2);
    }

    @Test
    void findsAvailableProjects(){
        Board board = new Board();
        assertEquals(board.availableProjectsInColumn(3), 5);
        board.addProject(new Location(0,1), '^');
        board.addProject(new Location(1,1), '^');
        board.addProject(new Location(2,1), '^');
        board.addProject(new Location(3,1), '^');
        board.addProject(new Location(4,1), '^');
        assertEquals(board.availableProjectsInColumn(1), 0);
    }

    @Test
    void findsCorrectAvailableColumns() {
        Board board = new Board();

        board.addProject(new Location(0,3), '^');
        board.addProject(new Location(1,3), '^');
        board.addProject(new Location(2,3), '^');
        board.addProject(new Location(3,3), '^');
        board.addProject(new Location(4,3), '^');
        board.addProject(new Location(0,4), '^');

        ArrayList<Integer> contents = board.findPlayableColumns(3);
        assertEquals(contents.size(), 1);
        assertEquals(contents.get(0), 2);
    }

    @Test
    void findsMultipleAvailableColumns() {
        Board board = new Board();

        board.addProject(new Location(0,3), '^');
        board.addProject(new Location(1,3), '^');
        board.addProject(new Location(2,3), '^');
        board.addProject(new Location(3,3), '^');
        board.addProject(new Location(4,3), '^');
        board.addProject(new Location(0,4), '^');
        board.addProject(new Location(0,2), '^');

        ArrayList<Integer> contents = board.findPlayableColumns(3);
        assertEquals(contents.size(), 2);
        assertEquals(contents.get(0), 2);
        assertEquals(contents.get(1), 4);
    }

    @Test
    void findsNoAvailableColumns() {
        Board board = new Board();

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 6; col++) {
                board.addProject(new Location(row, col), '^');
            }
        }

        ArrayList<Integer> contents = board.findPlayableColumns(3);
        assertEquals(contents.size(), 0);
    }

    @Test
    void findsColumnFartherThanOneAway() {
        Board board = new Board();

        board.addProject(new Location(0,3), '^');
        board.addProject(new Location(1,3), '^');
        board.addProject(new Location(2,3), '^');
        board.addProject(new Location(3,3), '^');
        board.addProject(new Location(4,3), '^');

        board.addProject(new Location(0,4), '^');
        board.addProject(new Location(1,4), '^');
        board.addProject(new Location(2,4), '^');
        board.addProject(new Location(3,4), '^');
        board.addProject(new Location(4,4), '^');

        board.addProject(new Location(0,2), '^');
        board.addProject(new Location(1,2), '^');
        board.addProject(new Location(2,2), '^');
        board.addProject(new Location(3,2), '^');
        board.addProject(new Location(4,2), '^');

        board.addProject(new Location(0,5), '^');

        ArrayList<Integer> contents = board.findPlayableColumns(3);
        assertEquals(contents.size(), 1);
        assertEquals(contents.get(0), 1);

        contents = board.findPlayableColumns(4);
        assertEquals(contents.size(), 1);
        assertEquals(contents.get(0), 5);
    }

    @Test
    void isEmptyWorksOnEmptyWithLocation(){
        Board board = new Board();
        Location location = new Location(3,3);
        assertTrue(board.isEmpty(location));
    }

    @Test
    void isEmptyWorksOnNotEmptyWithLocation(){
        Board board = new Board();
        Location location = new Location(3,3);
        board.addProject(location, '^');
        assertFalse(board.isEmpty(location));
    }
}