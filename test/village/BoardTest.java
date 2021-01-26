package village;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void printASCII() {
        String correct = "[-      1  2  3  4  5  6]\n" +
                         "[3/4   -3 -0 -2 -2 -0 -3]\n" +
                         "[5/6   -0 -1 -0 -0 -1 -0]\n" +
                         "[7     -2 -0 -1 -1 -0 -2]\n" +
                         "[8/9   -0 -1 -0 -0 -1 -0]\n" +
                         "[10/11 -3 -0 -2 -2 -0 -3]\n";
        Board board = new Board();
//        System.out.println(correct);
//        System.out.println();
//        System.out.println(board.printASCII());
        assertEquals(board.printASCII(), correct);
    }

    @Test
    void findsAvailableProjects(){
        Board board = new Board();
        assertEquals(board.availableProjectsInColumn(3), 5);
    }
}