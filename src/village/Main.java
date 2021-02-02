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
        //TODO add a helper function that creates the arraylist given to checkInput that checks if a row can take a project for a given column
        String row = checkInput(new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5")));
        //check if row, column is empty
        System.out.println(Integer.parseInt(row));
        Location project1 = new Location(Integer.parseInt(row) - 1, col - 1);
        board.addProject(project1, proj);
        System.out.println(board.toString());
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

        //select projects to place (must be different cannot be square)
        //place first one
        //place second one
        //do not score points
    }

    public void takeTurn() {
        System.out.println("--------------------------------");
        getRolls();
        boolean square = rolls[0] == rolls[1];

        System.out.println("You get to place a " + projects[rolls[0]] + " in column " + rolls[1]);
        if (square) {
            System.out.println("You get to place a # anywhere.");
        } else {
            System.out.println("You get to place a " + projects[rolls[1]] + " in column " + rolls[0]);
        }

        pickRowAndPlaceProject(rolls[0], projects[rolls[1]]); //System.out.println("In which row do want to build " + projects[rolls[0]] + "?");
        //this is for checking valid inputs
//        // Make sure space is empty
//        do {
//            do {
//                System.out.print("Enter a row (with an empty space) from 1 to 5: ");
//                row = input.nextInt();
//            } while (row < 1 || row > 5);
//            location = new Location(row - 1, rolls[1] - 1);
//        } while (!board.isEmpty(location));
//        board.addProject(location, projects[rolls[0]]);
        if (square) {
            System.out.println("In which column do you want to build #?");
            //select column
            pickRowAndPlaceProject(rolls[1], '#'); //rolls[1] will be replaced with selected column
        } else {
            pickRowAndPlaceProject(rolls[1], projects[rolls[0]]);
        }
    }
}
