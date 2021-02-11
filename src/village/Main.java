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
    private Dice dice;
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
        main.setSeed();
        main.initializeGraphics();
        main.placeInitialProjects();
        for (int turn = 1; turn < 10; turn++) {
            main.takeTurn(turn);
            main.drawBoard();
        }
        System.out.println("Final score from squares with all three adjacent project types: "
                + main.board.finalScoring());
        System.out.println("Final score of the game: " + main.board.getScore());
        main.drawBoard();
    }

    /**
     * Sets the seed. If the player does not choose a seed, they get a random seed.
     */
    public void setSeed(){
        System.out.println("Enter an integer seed. If you don't want to choose a seed, enter '-1'.");
        Scanner in = new Scanner(System.in);
        int seed = in.nextInt();
        if (seed != -1){
            dice = new Dice(seed);
        } else {
            dice = new Dice();
        }
    }

    private void getRolls() {
        rolls = dice.rollTwice();
        drawRoll();
    }

    private void pickRowAndPlaceProject(int col, char proj) {
        System.out.println("In which row do want to build " + proj + "?");
        String row = checkInput(getValidRowInput(col - 1), 1);
        System.out.println(Integer.parseInt(row));
        Location project1 = new Location(Integer.parseInt(row) - 1, col - 1);
        board.addProject(project1, proj);
        drawProject(project1.getRow(), project1.getCol(), proj);
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
     * @param inputType 0 for column input, 1 for row input, and 2 for initial project input
     * @return their input as a character
     */
    private String checkInput(ArrayList<String> validInputs, int inputType) {
        printSelectables(validInputs);
        if (inputType > 1) {
            while (true) {
                waitForClick();
                String usrInpt = "";
                if (mouseClick[0] > 625 && mouseClick[0] < 675) {
                    if (mouseClick[1] > 870 && mouseClick[1] < 905) {
                        usrInpt = "H";
                    } else if (mouseClick[1] > 770 && mouseClick[1] < 830) {
                        usrInpt = "^";
                    } else if (mouseClick[1] > 670 && mouseClick[1] < 730) {
                        usrInpt = "O";
                    }
                }
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
                String usrInpt = "" + (int) mouseClick[inputType];
                if (isClickOnBoard() && validInputs.contains(usrInpt)) {
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

        if (turn % 3 == 0) {
            bonusPhase();
        }

        int row;
        if (rolls[2] == 2 || rolls[2] == 12) {
            do {
                System.out.println("Select a row to score.");
                getAndTranslateClick();
                row = (int) mouseClick[1];
            } while (row < 1 || row > board.ROWS);
            System.out.println("Scoring row " + row + " for this round. Score: " + board.scoreRow(row - 1));
        } else {
            System.out.println("Scoring row " + (rowFromSum[rolls[2]] + 1) + " for this round. Score: " + board.scoreRow(rowFromSum[rolls[2]]));
        }
        System.out.println("Total score is now " + board.getScore());
    }

    private void bonusPhase() {
        System.out.println("Bonus phase. You get to draw an additional project in any (empty) space");
        char project = '-';
        do {
            System.out.println("Select what project you want to build.");
            waitForClick();
            if (mouseClick[1] > 825 && mouseClick[1] < 885) {
                if (mouseClick[0] > 25 && mouseClick[0] < 75) {
                    project = 'H';
                } else if (mouseClick[0] > 125 && mouseClick[0] < 175) {
                    project = '^';
                } else if (mouseClick[0] > 220 && mouseClick[0] < 280) {
                    project = 'O';
                }
            }
        } while (!bonusProjects.contains(project));


        bonusProjects.remove(project);

        System.out.println("In which row and column do you want to build " + project + "?");
        Location location;
        int row, column;
        do {
            do {
                System.out.println("Select where you want to build.");
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
        StdDraw.setXscale(0, 700);
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
        StdDraw.text(300, 700, "Seed: " + dice.getSeed());

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
                StdDraw.setPenColor(StdDraw.RED);
                double[] x = {25, 75, 75, 50, 25};
                double[] y = {25 + 800, 25 + 800, 60 + 800, 85 + 800, 60 + 800};
                StdDraw.polygon(x, y);
            }
            else if(project == '^'){
                StdDraw.setPenColor(StdDraw.GREEN);
                double[] x = {25 + 100, 75 + 100, 50 + 100};
                double[] y = {25 + 800, 25 + 800, 85 + 800};
                StdDraw.polygon(x, y);
            }
            else{
                StdDraw.setPenColor(StdDraw.BLUE);
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
     * Bread and butter click call.
     */
    private void getAndTranslateClick() {
        waitForClick();
        translateClick();
    }

    /**
     * Returns whether the user clicked on the board.
     */
    public boolean isClickOnBoard() {
                return ((mouseClick[0] <= board.COLS) && (mouseClick[0] > 0) &&
                        (mouseClick[1] <= board.ROWS) && (mouseClick[1] > 0));
            }
    /**
     * Draws the different project types along with their associated dice rolls.
     */
    private void drawProjectTypes(){

        // Lake and associated dice
       StdDraw.picture(475, 700, "3.png", 75, 75);
       StdDraw.picture(550, 700, "6.png", 75, 75);
       StdDraw.setPenColor(StdDraw.BLUE);
       StdDraw.circle(650, 700, 30);

        // Forest and associated dice
        StdDraw.picture(475, 800, "2.png", 75, 75);
        StdDraw.picture(550, 800, "5.png", 75, 75);
        double[] x = {25 + 600, 75 + 600, 50 + 600};
        double[] y = {25 + 745, 25 + 745, 85 + 745};
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.polygon(x, y);

        // House and associated dice
        StdDraw.picture(475, 900, "1.png", 75, 75);
        StdDraw.picture(550, 900, "4.png", 75, 75);
        double[] x2 = {25 + 600, 75 + 600, 75 + 600, 50 + 600, 25 + 600};
        double[] y2 = {25 + 845, 25 + 845, 60 + 845, 85 + 845, 60 + 845};
        StdDraw.setPenColor(StdDraw.RED);
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

