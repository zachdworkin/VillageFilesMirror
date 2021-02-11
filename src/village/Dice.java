package village;

import java.util.Random;

public class Dice {
    /**
     * Rolls a number from 1 to 6
     **/
    public static int roll() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }

    /**
     * Returns both dice and their sum
     **/
    public static int[] rollTwice() {
        int dice1 = roll();
        int dice2 = roll();
        return new int[]{dice1, dice2, dice1 + dice2};
    }
}
