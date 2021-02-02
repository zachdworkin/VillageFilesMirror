package village;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    private static char[] projects = {'#', 'H', '^', 'O', 'H', '^', 'O'};

    private Board board;
    private Set<Character> bonusProjects;

    public Main(){
        bonusProjects = new TreeSet<>();
        bonusProjects.add('^');
        bonusProjects.add('H');
        bonusProjects.add('O');

        board = new Board();
    }

    public static void main(String[] args) {
        Main main = new Main();
        for (int turn = 1; turn < 10; turn++) {
            main.takeTurn(turn);
        }
    }

    public void takeTurn(int turn) {
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
        int row;
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

        if(turn % 3 == 0){
            bonusPhase();
        }
    }

    private void bonusPhase() {
        System.out.println("Bonus phase. You get to draw an additional project in any (empty) space");
        Scanner input = new Scanner(System.in);
        char project;
        do {
            System.out.print("Enter what project you want to build: ");
            project = input.next().charAt(0);
        } while(!bonusProjects.contains(project));
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
        board.addProject(location, project);
    }
}
