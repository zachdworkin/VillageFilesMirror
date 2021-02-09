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
    public double[] mouseClick;

    public Main() {
        board = new Board();
        bonusProjects = new TreeSet<>(Arrays.asList('H', '^', 'O'));
        mouseClick = new double[2];
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

        drawRoll();
    }

    private void pickRowAndPlaceProject(int col, char proj) {
        System.out.println("In which row do want to build " + proj + "?");
        String row = checkInput(getValidRowInput(col - 1), 1);
        System.out.println(Integer.parseInt(row));
        Location project1 = new Location(Integer.parseInt(row) - 1, col - 1);
        board.addProject(project1, proj);
        drawProject(project1.getRow(), project1.getCol(), proj);
        //System.out.println(board.toString());
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
     * @param validInputs an ArrayList of valid input string
     * @return their input as a character
     */
    private String checkInput(ArrayList<String> validInputs, int rowCol) {
        printSelectables(validInputs);
        if (rowCol > 1) {
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
        } else {
            while (true) {
                getAndTranslateClick();
                String usrInpt = "" + (int) mouseClick[rowCol];
                if (clickIsOnBoard() && validInputs.contains(usrInpt)) {
                    return usrInpt;
                } else {
                    System.out.println("Invalid input, please select an available input");
                    printSelectables(validInputs);
                }
            }
        }
    }

    public void placeInitialProjects() {
        ArrayList<String> availablePreProjects = new ArrayList<>(Arrays.asList("H", "^", "O"));
        getRolls();
        System.out.println("You start with two projects, select the first project to " +
                "place in column " + rolls[0]);

        String proj = checkInput(availablePreProjects, 2);
        availablePreProjects.remove(availablePreProjects.indexOf(proj));
        System.out.println("You get to place a '" + proj + "' in column " + rolls[0] +
                ". Select row to place it in.");
        //System.out.println(board.toString());

        pickRowAndPlaceProject(rolls[0], proj.toCharArray()[0]);

        System.out.println("Select the second project to place in column " + rolls[1]);
        proj = checkInput(availablePreProjects, 2);
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
            selectedCol = Integer.parseInt(checkInput(validInpts, 0));
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
                selectedCol2 = Integer.parseInt(checkInput(validInpts, 0));
            }
        }

        if (square) {
            System.out.println("You get to place a # anywhere.");
            System.out.println("In which column do you want to build #?");
            String column = checkInput(getValidColInput(), 0);
            pickRowAndPlaceProject(Integer.parseInt(column), '#');
        } else {
            pickRowAndPlaceProject(selectedCol2, projects[rolls[1]]);
        }
        //System.out.println(board.toString());

        if (turn % 3 == 0) {
            bonusPhase();
            //System.out.println(board.toString());
        }

        int row;
        Scanner input = new Scanner(System.in);
        if (rolls[2] == 2 || rolls[2] == 12) {
            do {
                System.out.print("Enter a row (with an empty space) from 1 to 5: ");
                getAndTranslateClick();
                row = (int) mouseClick[1];
            } while (row < 1 || row > board.ROWS || mouseClick[0] < 7);
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
                getAndTranslateClick();
                row = (int) mouseClick[1];
                column = (int) mouseClick[0];
            } while (row < 1 || row > board.ROWS || column < 1 || column > board.COLS);
            location = new Location(row - 1, column - 1);
        } while (!board.isEmpty(location));
        drawProject(location.getRow(), location.getCol(), project);
        board.addProject(location, project);
    }

    void initializeGraphics() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 800);
        StdDraw.setYscale(0, 950);
        drawBoard();
    }

    static String[] rowLabels = {null, "3,4", "5,6", "7", "8,9", "10,11"};

    void drawBoard() {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setFont(new java.awt.Font("Serif", java.awt.Font.PLAIN, 24));

        // Draw vertical grid lines
        for (int i = 0; i < 800; i += 100) {
            StdDraw.line(i, 1, i, 600);

        }
        // Draw horizontal grid lines
        for (int i = 0; i < 700; i += 100) {
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
        for (int row = 1; row <= board.ROWS; row++) {
            for (int column = 1; column <= board.COLS; column++) {
                int points = board.points[row - 1][column - 1];
                if (points != 0) {
                    StdDraw.setPenColor(StdDraw.GRAY);
                    StdDraw.text(column * 100 + 50, 550 - 100 * row, "" + points);
                }
                drawProject(row - 1, column - 1, board.getProject(row - 1, column - 1));
            }
        }

        drawBonusProjects();
        drawProjectTypes();

        // Draw title and score
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(425, 625, "Rolling Village");
        StdDraw.text(75, 625, "Score: " + board.getScore());

        // Draw roll button
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledSquare(750, 50, 50);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.square(750, 50, 50);
        StdDraw.text(750, 50, "Roll");

        StdDraw.show();
    }

    /**
     * Draws a project on the graphics screen.
     */
    private void drawProject(int row, int col, char project) {
        int xOffset = 100 + col * 100;
        int yOffset = 400 - row * 100;

        if (project == 'H') {
            StdDraw.setPenColor(StdDraw.RED);
            double[] x = {25 + xOffset, 75 + xOffset, 75 + xOffset, 50 + xOffset, 25 + xOffset};
            double[] y = {25 + yOffset, 25 + yOffset, 60 + yOffset, 85 + yOffset, 60 + yOffset};
            StdDraw.polygon(x, y);
        }
        else if(project == '^'){
            StdDraw.setPenColor(StdDraw.GREEN);
            double[] x = {25 + xOffset, 75 + xOffset, 50 + xOffset};
            double[] y = {25 + yOffset, 25 + yOffset, 85 + yOffset};
            StdDraw.polygon(x, y);
        }
        else if(project == 'O'){
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.circle(xOffset + 50, yOffset + 50, 30);
        }
        else if (project == '#') {
            StdDraw.setPenColor(StdDraw.MAGENTA);
            StdDraw.rectangle(xOffset + 50, yOffset + 50, 30, 30);
        }
        StdDraw.show();
    }

    public void waitForClick() {
        while (!StdDraw.isMousePressed()) {
            // Wait for mouse press
        }

        mouseClick[0] = StdDraw.mouseX();
        mouseClick[1] = StdDraw.mouseY();
        while (StdDraw.isMousePressed()) {
            // Wait for mouse release
        }
    }

    /**
     * Draws the currently available bonus projects
     */
    private void drawBonusProjects(){
        StdDraw.setPenColor(Color.BLACK);
        //Draw Label for available bonus project
        StdDraw.line(0, 900, 400, 900);
        StdDraw.text(200, 925, "Available Bonus Projects:");

        for(char project: bonusProjects){
            if(project == 'H'){
                double[] x = {25, 75, 75, 50, 25};
                double[] y = {25 + 800, 25 + 800, 60 + 800, 85 + 800, 60 + 800};
                StdDraw.polygon(x, y);
            }
            else if(project == '^'){
                double[] x = {25 + 100, 75 + 100, 50 + 100};
                double[] y = {25 + 800, 25 + 800, 85 + 800};
                StdDraw.polygon(x, y);
            }
            else{
                StdDraw.circle(200 + 50, 800 + 50, 30);
            }
        }
    }

    /**
     * Translate click from graphics to array indices
     */
    public void translateClick() {
        mouseClick[0] = Math.floor(mouseClick[0] / 100);
        mouseClick[1] = board.ROWS - Math.floor(mouseClick[1] / 100);
    }

    /**
     *
     * @return if a click is valid
     */
    private boolean isClickValid() {

        return true;
    }

    private void getAndTranslateClick() {
        waitForClick();
        translateClick();
    }

    /**
     * post translation call
     * @return
     */
    public boolean clickIsOnBoard() {
                return ((mouseClick[0] <= board.COLS) && (mouseClick[0] > 0) &&
                        (mouseClick[1] <= board.ROWS) && (mouseClick[1] > 0));
            }
    /**
     * Draws the different project types along with their associated dice rolls.
     */
    private void drawProjectTypes(){

        //Lake and associated dice
       StdDraw.picture(575, 700, "3.png", 75, 75);
       StdDraw.picture(650, 700, "6.png", 75, 75);
       StdDraw.circle(750, 700, 30);

        //Forest and associated dice
        StdDraw.picture(575, 800, "2.png", 75, 75);
        StdDraw.picture(650, 800, "5.png", 75, 75);
        double[] x = {25 + 700, 75 + 700, 50 + 700};
        double[] y = {25 + 745, 25 + 745, 85 + 745};
        StdDraw.polygon(x, y);

        //House and associated dice
        StdDraw.picture(575, 900, "1.png", 75, 75);
        StdDraw.picture(650, 900, "4.png", 75, 75);
        double[] x2 = {25 + 700, 75 + 700, 75 + 700, 50+ 700, 25 + 700};
        double[] y2 = {25 + 845, 25 + 845, 60 + 845, 85 + 845, 60 + 845};
        StdDraw.polygon(x2, y2);
    }

    /**
     * Draws the dice from the current roll.
     */
    private void drawRoll(){
        StdDraw.setPenColor(Color.BLACK);
        //Draw Label for available bonus project
        StdDraw.line(0, 750, 400, 750);
        StdDraw.text(105, 775, "Current Roll:");

        //Check that dice have been rolled
        if(rolls[0] == 0){
            return;
        }
        System.out.printf("%d, %d\n", rolls[0], rolls[1]);
        StdDraw.picture(50, 700, rolls[0] + ".png", 75, 75);
        StdDraw.picture(125, 700, rolls[1] + ".png", 75, 75);

        StdDraw.show();
    }
}

