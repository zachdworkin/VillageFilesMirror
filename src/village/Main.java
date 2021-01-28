package village;

import java.util.Scanner;

public class Main {
    private Board board = new Board();
    private static char[] projects = {'#', 'H', '^', 'O', 'H', '^', 'O'};

    public static void main(String[] args) {
        Main main = new Main();
        main.takeTurn();
    }

    public void takeTurn() {
        // Roll the dice
        int[] rolls = Dice.rollTwice();
        boolean square = rolls[0] == rolls[1];
        System.out.println("First roll: " + rolls[0]);
        System.out.println("Second roll: " + rolls[1]);
        System.out.println("Sum of rolls: " + rolls[2]);
        // Place two projects
        System.out.println(board.toString());
        System.out.println("You get to place a " + projects[rolls[0]] + " in column " + rolls[1]);
        if (square) {
            System.out.println("You get to place a # anywhere.");
        } else {
            System.out.println("You get to place a " + projects[rolls[1]] + " in column " + rolls[0]);
        }
        System.out.println("In which row do want to build " + projects[rolls[0]] + "?");
        Scanner input = new Scanner(System.in);
        // User chooses which row to place project
        int row = -1;
        Location location;
        // Make sure space is empty
        do {
            do {
                System.out.print("Enter a row (with an empty space) from 1 to 5: ");
                row = input.nextInt();
            } while (row < 1 || row > 5);
            location = new Location(row - 1, rolls[1] - 1);
        } while (!board.isEmpty(location));
        board.addProject(location, projects[rolls[0]]);

        System.out.println(board.toString());
        if (square) {
            System.out.println("In which row and column do you want to build #?");
            int column;
            do {
                do {
                    System.out.print("Enter the row (from 1 to 5) and column (from 1 to 6): ");
                    row = input.nextInt();
                    column = input.nextInt();
                } while (row < 1 || row > 5 || column < 1 || column > 6);
                location = new Location(row - 1, column - 1);
            } while (!board.isEmpty(location));
            board.addProject(location, '#');
        } else {
            System.out.println("In which row do want to build " + projects[rolls[1]] + "?");
            do {
                do {
                    System.out.print("Enter a row (with an empty space) from 1 to 5: ");
                    row = input.nextInt();
                } while (row < 1 || row > 5);
                location = new Location(row - 1, rolls[0] - 1);
            } while (!board.isEmpty(location));
            board.addProject(location, projects[rolls[1]]);
        }
        System.out.println(board.toString());
    }
}
