package village;

import edu.princeton.cs.algs4.StdDraw;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    private static char[] projects = {'#', 'H', '^', 'O', 'H', '^', 'O'};
    private static int[] rowFromSum = {-1, -1, -1, 0, 0, 1, 1, 2, 3, 3, 4, 4, -1};

    private int[] rolls = new int[2];
    public Board board;
    private Set<Character> bonusProjects;

    public Main() {
        board = new Board();
        bonusProjects = new TreeSet<>(Arrays.asList('H', '^', 'O'));
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.initializeGraphics();
        main.placeInitialProjects();
        for (int turn = 1; turn < 10; turn++) {
            main.takeTurn(turn);
            main.drawBoard();
        }
        System.out.println("Final score from squares with all three adjacent project types: "
                + main.board.finalScoring());
        System.out.println("Final score of the game: " + main.board.getScore());
    }

    private void getRolls() {
        rolls = Dice.rollTwice();
        System.out.println("First roll: " + rolls[0]);
        System.out.println("Second roll: " + rolls[1]);
        System.out.println("Sum of rolls: " + rolls[2]);
    }

    private void pickRowAndPlaceProject(int col, char proj) {
        System.out.println("In which row do want to build " + proj + "?");
        String row = checkInput(getValidRowInput(col - 1));
        System.out.println(Integer.parseInt(row));
        Location project1 = new Location(Integer.parseInt(row) - 1, col - 1);
        board.addProject(project1, proj);
        drawProject(project1.getRow(), project1.getCol(), proj);
        System.out.println(board.toString());
    }

    /**
     * Returns playable rows in given column
     *
     * @param col integer column
     * @return arraylist of rows in string form
     */
    public ArrayList<String> getValidRowInput(int col) {
        ArrayList<String> validRows = new ArrayList<>();
        for (int row = 0; row < 5; row++) {
            if (board.isEmpty(new Location(row, col))) {
                validRows.add(Integer.toString(row + 1));
            }
        }
        return validRows;
    }

    /**
     * Returns playable columns
     *
     * @return arraylist of columns in string form
     */
    public ArrayList<String> getValidColInput() {
        ArrayList<String> validColumns = new ArrayList<>();
        for (int col = 0; col < 6; col++) {
            ArrayList<Integer> playableColumns = board.findPlayableColumns(col);
            if (playableColumns.size() == 1 && playableColumns.contains(col)) {
                validColumns.add(Integer.toString(col + 1));
            }
        }
        return validColumns;
    }

    /**
     * helper function for checkInput
     * prints valid inputs
     *
     * @param selectableInputs an ArrayList of input strings to print
     */
    private void printSelectables(ArrayList<String> selectableInputs) {
        System.out.print("You may select a: ");
        for (String usrInpt : selectableInputs) {
            System.out.print(usrInpt + " ");
        }
        System.out.println();
    }

    /**
     * Checks any user input for validity
     *
     * @param validInputs an ArrayList of valid input strings
     * @return their input as a character
     */
    private String checkInput(ArrayList<String> validInputs) {
        printSelectables(validInputs);
        Scanner input = new Scanner(System.in);

        while (true) {
            String usrInpt = input.next();
            if (validInputs.contains(usrInpt)) {
                return usrInpt;
            } else {
                System.out.println("Invalid input, please select an available input");
                printSelectables(validInputs);
            }
        }
    }

    public void placeInitialProjects() {
        ArrayList<String> availablePreProjects = new ArrayList<>(Arrays.asList("H", "^", "O"));
        getRolls();
        System.out.println("You start with two projects, select the first project to " +
                "place in column " + rolls[0]);

        String proj = checkInput(availablePreProjects);
        availablePreProjects.remove(availablePreProjects.indexOf(proj));
        System.out.println("You get to place a '" + proj + "' in column " + rolls[0] +
                ". Select row to place it in.");
        System.out.println(board.toString());

        pickRowAndPlaceProject(rolls[0], proj.toCharArray()[0]);

        System.out.println("Select the second project to place in column " + rolls[1]);
        proj = checkInput(availablePreProjects);
        pickRowAndPlaceProject(rolls[1], proj.toCharArray()[0]);
    }

    public void takeTurn(int turn) {
        System.out.println("--------------------------------");
        getRolls();
        boolean square = rolls[0] == rolls[1];
        ArrayList<Integer> playableCol = board.findPlayableColumns(rolls[1] - 1);
        int selectedCol = rolls[1];
        int selectedCol2 = rolls[0];
        if (!playableCol.contains(rolls[1] - 1)) {
            System.out.print("Column " + rolls[1] + " is full. ");
        }

        if (playableCol.size() == 1) {
            System.out.println("You get to place a " + projects[rolls[0]] + " in column " +
                    (playableCol.get(0) + 1));
            selectedCol = playableCol.get(0) + 1;
        } else {
            System.out.println("You get to place a " + projects[rolls[0]] + " in column " +
                    (playableCol.get(0) + 1) + " or " + (playableCol.get(1) + 1));
            ArrayList<String> validInpts = new ArrayList();
            validInpts.add(Integer.toString(playableCol.get(0) + 1));
            validInpts.add(Integer.toString(playableCol.get(1) + 1));
            selectedCol = Integer.parseInt(checkInput(validInpts));
        }

        pickRowAndPlaceProject(selectedCol, projects[rolls[0]]);

        if (!square) {
            playableCol = board.findPlayableColumns(rolls[0] - 1);
            if (!playableCol.contains(rolls[0] - 1)) {
                System.out.print("Column " + rolls[0] + " is full. ");
            }

            if (playableCol.size() == 1) {
                System.out.println("You get to place a " + projects[rolls[1]] + " in column " +
                        (playableCol.get(0) + 1));
                selectedCol2 = playableCol.get(0) + 1;
            } else {
                System.out.println("You get to place a " + projects[rolls[1]] + " in column " +
                        (playableCol.get(0) + 1) + " or " + (playableCol.get(1) + 1));
                ArrayList<String> validInpts = new ArrayList();
                validInpts.add(Integer.toString(playableCol.get(0) + 1));
                validInpts.add(Integer.toString(playableCol.get(1) + 1));
                selectedCol2 = Integer.parseInt(checkInput(validInpts));
            }
        }

        if (square) {
            System.out.println("You get to place a # anywhere.");
            System.out.println("In which column do you want to build #?");
            String column = checkInput(getValidColInput());
            pickRowAndPlaceProject(Integer.parseInt(column), '#');
        } else {
            pickRowAndPlaceProject(selectedCol2, projects[rolls[1]]);
        }
        System.out.println(board.toString());

        if (turn % 3 == 0) {
            bonusPhase();
            System.out.println(board.toString());
        }

        int row;
        Scanner input = new Scanner(System.in);
        if (rolls[2] == 2 || rolls[2] == 12) {
            do {
                System.out.print("Enter a row (with an empty space) from 1 to 5: ");
                row = input.nextInt();
            } while (row < 1 || row > 5);
            System.out.println("Scoring row " + (row + 1) + " for this round. Score: " + board.scoreRow(row));
        } else {
            System.out.println("Scoring row " + (rowFromSum[rolls[2]] + 1) + " for this round. Score: " + board.scoreRow(rowFromSum[rolls[2]]));
        }
        System.out.println("Total score is now " + board.getScore());
    }

    private void bonusPhase() {
        System.out.println("Bonus phase. You get to draw an additional project in any (empty) space");
        Scanner input = new Scanner(System.in);
        char project;
        do {
            System.out.print("Enter what project you want to build: ");
            project = input.next().charAt(0);
        } while (!bonusProjects.contains(project));
        bonusProjects.remove(project);

        System.out.println("In which row and column do you want to build " + project + "?");
        Location location;
        int row, column;
        do {
            do {
                System.out.print("Enter the row (from 1 to 5) and column (from 1 to 6): ");
                row = input.nextInt();
                column = input.nextInt();
            } while (row < 1 || row > 5 || column < 1 || column > 6);
            location = new Location(row - 1, column - 1);
        } while (!board.isEmpty(location));
        drawProject(location.getRow(), location.getCol(), project);
        board.addProject(location, project);
    }

    void initializeGraphics() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 800);
        StdDraw.setYscale(0, 700);
        drawBoard();
    }

    static String[] rowLabels = {null, "3,4", "5,6", "7", "8,9", "10,11"};

    void drawBoard() {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setFont(new java.awt.Font("Serif", java.awt.Font.PLAIN, 24));

        // Draw grid lines
        for (int i = 0; i < 800; i += 100) {
            StdDraw.line(i, 1, i, 600);
            StdDraw.line(1, i, 700, i);
        }

        // Draw labels
        for (int column = 1; column <= board.COLS; column++) {
            StdDraw.text(column * 100 + 50, 550, "" + column);
        }
        for (int row = 1; row <= board.ROWS; row++) {
            StdDraw.text(50, 575 - 100 * row, "(" + row + ")");
            StdDraw.text(50, 525 - 100 * row, rowLabels[row]);
        }

        // Draw points and projects
        StdDraw.setPenColor(StdDraw.GRAY);
        for (int row = 1; row <= board.ROWS; row++) {
            for (int column = 1; column <= board.COLS; column++) {
                int points = board.points[row - 1][column - 1];
                if (points != 0) {
                    StdDraw.text(column * 100 + 50, 550 - 100 * row, "" + points);
                }
                drawProject(row - 1, column - 1, board.getProject(row - 1, column - 1));
            }
        }

        // Draw title and score
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(425, 625, "Rolling Village");
        StdDraw.text(75, 625, "Score: " + board.getScore());

        // Draw roll button
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledSquare(750, 250, 50);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.square(750, 250, 50);
        StdDraw.text(750, 250, "Roll");

        StdDraw.show();
    }

    /**
     * Draws a project on the graphics screen.
     */
    private static void drawProject(int row, int col, char project) {
        int xOffset = 100 + col * 100;
        int yOffset = 400 - row * 100;

        if (project == 'H') {
            double[] x = {25 + xOffset, 75 + xOffset, 75 + xOffset, 50 + xOffset, 25 + xOffset};
            double[] y = {25 + yOffset, 25 + yOffset, 60 + yOffset, 85 + yOffset, 60 + yOffset};
            StdDraw.polygon(x, y);
        }
        else if(project == '^'){
            double[] x = {25 + xOffset, 75 + xOffset, 50 + xOffset};
            double[] y = {25 + yOffset, 25 + yOffset, 85 + yOffset};
            StdDraw.polygon(x, y);
        }
        else if(project == 'O'){
            StdDraw.circle(xOffset + 50, yOffset + 50, 30);
        }
        else if (project == '#') {
            StdDraw.rectangle(xOffset + 50, yOffset + 50, 30, 30);
        }
        StdDraw.show();
    }
}

