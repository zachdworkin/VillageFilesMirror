package village;

import java.util.ArrayList;

public class Board {

    private final int[][] points = {{3, 0, 2, 2, 0, 3},
                                    {0, 1, 0, 0, 1, 0},
                                    {2, 0, 1, 1, 0, 2},
                                    {0, 1, 0, 0, 1, 0},
                                    {3, 0, 2, 2, 0, 3}};

    private char[][] projects;

    public Board() {
        projects = new char[5][6];
        for (int row = 0; row < 5; row ++) {
            for (int col = 0; col < 6; col++) {
                projects[row][col] = '-';
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
    public String toString() {
        StringBuilder builtBoard = new StringBuilder();
        String[] startingChars = {"3/4  ", "5/6  ",
                                  "7    ", "8/9  ",
                                  "10/11"};

        builtBoard.append("[-      1  2  3  4  5  6]\n");
        for (int row = 0; row < 5; row++) {
            builtBoard.append('[' + startingChars[row]);
            for (int col = 0; col < 6; col++) {
                builtBoard.append(" " + projects[row][col] +
                                  points[row][col]);
            }

            builtBoard.append("]\n");
        }

        return builtBoard.toString();
    }

    public int availableProjectsInColumn(int col){
        int availableProjects = 5;
        for (int i = 0; i < 5; i++) {
            if(projects[i][col] != '-') availableProjects--;
        }
        return availableProjects;
    }

    public ArrayList<Integer> findPlayableColumns(int col) {
        ArrayList<Integer> playable = new ArrayList<>();

        if (availableProjectsInColumn(col) != 0) {
            playable.add(col);
            return playable;
        }
        for (int offset = 1; offset < 6; offset++) {
            int availableInLeftColumn = 0;
            int availableInRightColumn = 0;

            if (col - offset > 0) {
                availableInLeftColumn = availableProjectsInColumn(col - offset);
            }

            if (col + offset < 6) {
                availableInRightColumn = availableProjectsInColumn(col + offset);
            }

            if (availableInLeftColumn != 0 || availableInRightColumn != 0) {
                if (availableInLeftColumn == availableInRightColumn) {
                    playable.add(col - offset);
                    playable.add(col + offset);
                } else if (availableInLeftColumn > availableInRightColumn) {
                    playable.add(col - offset);
                } else if (availableInRightColumn > availableInLeftColumn) {
                    playable.add(col + offset);
                }
                return playable;
            }
        }
        return playable;
    }

    public void  addProject(Location location, char project){
        projects[location.getRow()][location.getCol()] = project;
    }
}
