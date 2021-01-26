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
        System.out.println("First roll: " + rolls[0]);
        System.out.println("Second roll: " + rolls[1]);
        System.out.println("Sum of rolls: " + rolls[2]);
        // Place two projects
        System.out.println(board.toString());
        System.out.println("You get to place a " + projects[rolls[0]] + " in column " + rolls[1]);
        System.out.println("You get to place a " + projects[rolls[1]] + " in column " + rolls[0]);
        System.out.println("In which row do want to build " + projects[rolls[0]] + "?");
        Scanner input = new Scanner(System.in);
        // User chooses which row to place project
        int row = input.nextInt();
        Location project1 = new Location(row - 1, rolls[1] - 1);
        board.addProject(project1, projects[rolls[0]]);
        System.out.println(board.toString());
        System.out.println("In which row do want to build " + projects[rolls[1]] + "?");
        row = input.nextInt();
        Location project2 = new Location(row - 1, rolls[0] - 1);
        board.addProject(project2, projects[rolls[1]]);
        System.out.println(board.toString());
    }
}
