package village;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private Board board = new Board();
    private static char[] projects = {'#', 'H', '^', 'O', 'H', '^', 'O'};
    private int[] rolls = new int[2];

    public static void main(String[] args) {
        Main main = new Main();
        main.takePreTurn();
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

    private void pickRowAndPlaceProject(int col) {
        System.out.println("In which row do want to build " + projects[col] + "?");
        char row = checkInput(new ArrayList<Character>(Arrays.asList('1', '2', '3', '4', '5')));
        //check if row, column is empty
        System.out.println();
        Location project1 = new Location((int)row - 1, rolls[1] - 1);
        board.addProject(project1, projects[rolls[0]]);
        System.out.println(board.toString());
    }

    /**
     * helper function for checkInput
     * prints valid inputs
     * @param validInputs
     */
    private void printSelectables(ArrayList<Character> validInputs) {
        System.out.print("You may select a: ");
        for (char proj : validInputs) {
            System.out.print(proj + " ");
        }
        System.out.println();
    }

    /**
     * Checks any user input for validity
     * @param validInputs
     * @return their input as a character
     */
    private char checkInput(ArrayList<Character> validInputs) {
        printSelectables(validInputs);
        Scanner input = new Scanner(System.in);
        while (true) {
            char proj = input.next().charAt(0);
            if (validInputs.contains(proj)) {
                return proj;
            } else{
                System.out.println("Invalid input, please select an available input");
                printSelectables(validInputs);
            }
        }
    }

    public void takePreTurn() {
        ArrayList<Character> availablePreProjects = new ArrayList<Character>(Arrays.asList('H', '^', 'O'));
        getRolls();
        System.out.println("You start with two projects, select the first project to " +
                           "place in column " + rolls[0]);

        char proj = checkInput(availablePreProjects);
        availablePreProjects.remove(availablePreProjects.indexOf(proj));
        System.out.println("You get to place a '" + proj + "' in column " + rolls[0] +
                           ". Select row to place it in.");
        System.out.println(board.toString());

        pickRowAndPlaceProject(rolls[0]);

        System.out.println("Select the second project to place in column " + rolls[1]);
        proj = checkInput(availablePreProjects);

        //select projects to place (must be different cannot be square)
        //place first one
        //place second one
        //do not score points
    }

    public void takeTurn() {
        getRolls();
        System.out.println(board.toString());
        //placeProject;
        System.out.println("You get to place a " + projects[rolls[0]] + " in column " + rolls[1]);
        System.out.println("You get to place a " + projects[rolls[1]] + " in column " + rolls[0]);

        pickRowAndPlaceProject(rolls[0]);
        pickRowAndPlaceProject(rolls[1]);
    }
}
