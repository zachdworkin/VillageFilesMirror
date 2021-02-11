package village;

import java.util.Random;

public class Dice {
    private Random random;
    private int seed;
    public Dice(int seed){
        random = new Random();
        random.setSeed(seed);
        this.seed = seed;
    }
    public Dice(){
        random = new Random();
        this.seed = random.nextInt(10000);
        random.setSeed(this.seed);
    }
    public int getSeed(){
        return seed;
    }
    /**
     * Rolls a number from 1 to 6
     **/
    public int roll() {
        return random.nextInt(6) + 1;
    }

    /**
     * Returns both dice and their sum
     **/
    public int[] rollTwice() {
        int dice1 = roll();
        int dice2 = roll();
        return new int[]{dice1, dice2, dice1 + dice2};
    }
}
