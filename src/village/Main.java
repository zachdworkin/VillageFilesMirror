package village;

import java.util.List;
import java.util.Scanner;

public class Main {
    private Board board = new Board();
    private static char[] projects = {'#', 'H', '^', 'O', 'H', '^', 'O'};

    public static void main(String[] args) {
        Main main = new Main();
        while (true) {
            main.takeTurn();
        }
    }

    public void takeTurn() {
        // Roll the dice
        int[] rolls = Dice.rollTwice();
        System.out.println("First roll: " + rolls[0]);
        System.out.println("Second roll: " + rolls[1]);
        System.out.println("Sum of rolls: " + rolls[2]);

        List<Integer> columnsForRoll1 = board.findPlayableColumns(rolls[0]);
        List<Integer> columnsForRoll2 = board.findPlayableColumns(rolls[1]);

        // Place two projects
        System.out.println(board.toString());
        if (columnsForRoll1.size() != 0) {
            System.out.print("You get to place a " + projects[rolls[0]] + " in column " + columnsForRoll1.get(0));
            if (columnsForRoll1.size() == 2) {
                System.out.println(" or " + columnsForRoll1.get(1));
            }
        }

        if (columnsForRoll2.size() != 0) {
            System.out.print("You get to place a " + projects[rolls[1]] + " in column " + columnsForRoll2.get(0));
            if (columnsForRoll2.size() == 2) {
                System.out.println(" or " + columnsForRoll2.get(1));
            }
        }
        System.out.println("In which row do want to build " + projects[rolls[0]] + "?");

        // User chooses which row to place project
        Scanner input = new Scanner(System.in);
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
