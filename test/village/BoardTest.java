package village;

import org.junit.jupiter.api.Test;

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
    void findsAvailableProjects(){
        Board board = new Board();
        assertEquals(board.availableProjectsInColumn(3), 5);
    }
}