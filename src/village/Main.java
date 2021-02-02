package village;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private Board board = new Board();
    private static char[] projects = {'#', 'H', '^', 'O', 'H', '^', 'O'};
    private int[] rolls = new int[2];

    public static void main(String[] args) {
        Main main = new Main();
        main.placeInitialProjects();
        while (true) {
            main.takeTurn();
        }
    }

    private void getRolls() {
        rolls = Dice.rollTwice();
        System.out.println("First roll: " + rolls[0]);
        System.out.println("Second roll: " + rolls[1]);
        System.out.println("Sum of rolls: " + rolls[2]);
    }

    private void pickRowAndPlaceProject(int col, char proj) {
        System.out.println("In which row do want to build " + proj + "?");
        String row = checkInput(getValidRowInput(col-1));
        System.out.println(Integer.parseInt(row));
        Location project1 = new Location(Integer.parseInt(row) - 1, col - 1);
        board.addProject(project1, proj);
        System.out.println(board.toString());
    }

    /**
     * Returns playable rows in given column
     * @param col integer column
     * @return arraylist of rows in string form
     */
    private ArrayList<String> getValidRowInput(int col){
        ArrayList<String> validRows = new ArrayList<>();
        for (int row = 0; row < 5; row++) {
            if (board.isEmpty(new Location(row, col))){
                validRows.add(Integer.toString(row+1));
            }
        }
        return validRows;
    }

    /**
     * Returns playable columns
     * @return arraylist of columns in string form
     */
    private ArrayList<String> getValidColInput(){
        ArrayList<String> validColumns = new ArrayList<>();
        for (int col = 0; col < 6; col++) {
            ArrayList<Integer> playableColumns = board.findPlayableColumns(col);
            if (playableColumns.size() == 1 && playableColumns.contains(col)){
                validColumns.add(Integer.toString(col+1));
            }
        }
        return validColumns;
    }
    /**
     * helper function for checkInput
     * prints valid inputs
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
            } else{
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

    public void takeTurn() {
        System.out.println("--------------------------------");
        getRolls();
        boolean square = rolls[0] == rolls[1];
        ArrayList<Integer> playableCol = board.findPlayableColumns(rolls[1] - 1);
        int selectedCol = rolls[1];
        int selectedCol2 = rolls[0];
        if (!playableCol.contains(rolls[1] - 1)){
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

        pickRowAndPlaceProject(selectedCol, projects[rolls[1]]);
        if (!square) {
            playableCol = board.findPlayableColumns(rolls[0] - 1);
            if (!playableCol.contains(rolls[0] - 1)){
                System.out.print("Column " + rolls[0] + " is full. ");
            }

            if (playableCol.size() == 1) {
                System.out.println("You get to place a " + projects[rolls[0]] + " in column " +
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
            pickRowAndPlaceProject(selectedCol2, projects[rolls[0]]);
        }
    }
}
