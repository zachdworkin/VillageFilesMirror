package village;

public class Board {

    private int[][] points = {{3, 0, 2, 2, 0, 3},
                              {0, 1, 0, 0, 1, 0},
                              {2, 0, 1, 1, 0, 2},
                              {0, 1, 0, 0, 1, 0},
                              {3, 0, 2, 2, 0, 3}};

    private char[][] pieces;

    public Board() {
        pieces = new char[5][6];
        for (int row = 0; row < 5; row ++) {
            for (int col = 0; col < 6; col++) {
                pieces[row][col] = '-';
            }
        }
    }

    /**
     * "[-      1  2  3  4  5  6]\n" +
     * "[3/4   -3 -0 -2 -2 -0 -3]\n" +
     * "[5/6   -0 -1 -0 -0 -1 -0]\n" +
     * "[7     -2 -0 -1 -1 -0 -2]\n" +
     * "[8/9   -0 -1 -0 -0 -1 -0]\n" +
     * "[10/11 -3 -0 -2 -2 -0 -3]\n";
     */
    public String printASCII() {
        StringBuilder builtBoard = new StringBuilder();
        String[] startingChars = {"3/4  ", "5/6  ",
                                  "7    ", "8/9  ",
                                  "10/11"};

        builtBoard.append("[-      1  2  3  4  5  6]\n");
        for (int row = 0; row < 5; row++) {
            builtBoard.append('[' + startingChars[row]);
            for (int col = 0; col < 6; col++) {
                builtBoard.append(" " + pieces[row][col] +
                                  points[row][col]);
            }

            builtBoard.append("]\n");
        }

        return builtBoard.toString();
    }
}
