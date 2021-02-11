package village;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Board {

    public final static int[][] points = {{3, 0, 2, 2, 0, 3},
            {0, 1, 0, 0, 1, 0},
            {2, 0, 1, 1, 0, 2},
            {0, 1, 0, 0, 1, 0},
            {3, 0, 2, 2, 0, 3}};
    public final static int ROWS = 5;
    public final static int COLS = 6;

    private int score;
    private char[][] projects;

    public Board() {
        score = 0;
        projects = new char[5][6];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                projects[row][col] = '-';
            }
        }
    }

    public int getScore(){
        return score;
    }

    /*
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
        for (int row = 0; row < ROWS; row++) {
            builtBoard.append('[');
            builtBoard.append(startingChars[row]);
            for (int col = 0; col < COLS; col++) {
                builtBoard.append(" ");
                builtBoard.append(projects[row][col]);
                builtBoard.append(points[row][col]);

            }

            builtBoard.append("]\n");
        }

        return builtBoard.toString();
    }

    /**
     * Finds the number of spaces in the given column where a project could be placed
     */
    public int availableProjectsInColumn(int col) {
        int availableProjects = 5;
        for (int i = 0; i < ROWS; i++) {
            if (!isEmpty(i, col)) availableProjects--;
        }
        return availableProjects;
    }

    /**
     * Returns the nearest column(s) with empty spaces
     */
    public ArrayList<Integer> findPlayableColumns(int col) {
        ArrayList<Integer> playable = new ArrayList<>();

        if (availableProjectsInColumn(col) != 0) {
            playable.add(col);
            return playable;
        }
        for (int offset = 1; offset < 6; offset++) {
            int availableInLeftColumn = 0;
            int availableInRightColumn = 0;

            if (col - offset >= 0) {
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
                } else{
                    playable.add(col + offset);
                }
                return playable;
            }
        }
        return playable;
    }

    /**
     * Adds a project to a space.
     *
     * @throws IllegalArgumentException if the space already has a project on it
     */
    public void addProject(Location location, char project) {
        if (!isEmpty(location)) {
            throw new IllegalArgumentException("Location already has a project");
        }
        projects[location.getRow()][location.getCol()] = project;
    }

    /**
     * Returns whether a space on the board has no project on it.
     */
    private boolean isEmpty(int row, int col) {
        return (projects[row][col] == '-');
    }

    /**
     * Returns whether a space on the board has no project on it.
     */
    public boolean isEmpty(Location location) {
        return isEmpty(location.getRow(), location.getCol());
    }


    /**
     * Returns the project at a given space on the board.
     */
    public char getProject(int row, int col) {
        return projects[row][col];
    }

    /**
     * Returns a list of the adjacent projects to a space.
     * List is in order: Row + 1, Row - 1, Col + 1, Col -1.
     */
    public List<Character> getAdjacentProjects(int row, int col) {
        List<Character> adjacentProjects = new ArrayList<>();

        if (row + 1 < ROWS) {
            adjacentProjects.add(getProject(row + 1, col));
        }
        if (row - 1 >= 0) {
            adjacentProjects.add(getProject(row - 1, col));
        }
        if (col + 1 < COLS) {
            adjacentProjects.add(getProject(row, col + 1));
        }
        if (col - 1 >= 0) {
            adjacentProjects.add(getProject(row, col - 1));
        }

        return adjacentProjects;
    }

    /**
     * Returns the score added by the final scoring of squares.
     */
    public int finalScoring() {
        int finalScore = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (getProject(row, col) == '#') {
                    List<Character> adjacentProjects = getAdjacentProjects(row, col);
                    if (adjacentProjects.contains('H') && adjacentProjects.contains('^') && adjacentProjects.contains('O')) {
                        finalScore += 10;
                    }
                }
            }
        }
        score += finalScore;
        return finalScore;
    }

    /**
     * Finds all projects connected to the project in a given space.
     * Modifies inGroup to mark connected projects.
     * inGroup must be the same size as the board
     */
    private void findGroup(int row, int col, char project, boolean[][] inGroup){
        inGroup[row][col] = true;
        if (row + 1 < ROWS) {
            if(getProject(row + 1, col) == project && !inGroup[row + 1][col]) {
                findGroup(row + 1, col, project, inGroup);
            }
        }
        if (row - 1 >= 0) {
            if(getProject(row - 1, col) == project && !inGroup[row - 1][col]) {
                findGroup(row - 1, col, project, inGroup);
            }
        }
        if (col + 1 < COLS) {
            if(getProject(row, col + 1) == project && !inGroup[row][col  + 1]) {
                findGroup(row, col + 1, project, inGroup);
            }
        }
        if (col - 1 >= 0) {
            if(getProject(row, col - 1) == project && !inGroup[row][col  - 1]) {
                findGroup(row, col - 1, project, inGroup);
            }
        }
    }

    /**
     * Finds all projects connected to the project in a given space.
     * Modifies inGroup to mark connected projects.
     * inGroup must be the same size as the board
     */
    private void findGroup(int row, int col, boolean[][] inGroup){
        if(inGroup[row][col]){
            return;
        }
        char project = getProject(row, col);

        if(project == '-' || project == '#'){
            return;
        }
        findGroup(row, col, project, inGroup);
    }

    /**
     * Returns the score for the current turn.
     */
    public int scoreRow(int scoringRow){
        int score = 0;
        boolean[][] toBeScored = new boolean[ROWS][COLS];
        for (int col = 0; col < COLS; col++) {
            findGroup(scoringRow, col, toBeScored);
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if(toBeScored[row][col]){
                    score += points[row][col];
                }
            }
        }
        this.score += score;
        return score;
    }
}
