package village;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {
    private Dice dice;
    @BeforeEach
    void DiceTest() {
        dice = new Dice();
    }
    @Test
    void rollsOnlyValidSides() {
        for (int i = 0; i < 10000; i++) {
            int roll = dice.roll();
            assertTrue(roll >= 1 && roll <= 6);
        }
    }

    @Test
    void rollsAllSidesEventually() {
        boolean[] sides = new boolean[7];
        boolean allSidesRolled = false;
        int times = 0;
        while (!allSidesRolled) {
            int roll = dice.roll();
            sides[roll] = true;
            allSidesRolled = true;
            for (int i = 1; i <= 6; i++) {
                allSidesRolled = allSidesRolled && sides[i];
            }
            times++;
            if (times == 1000000){
                System.out.println("Your function is probably not working.");
            }
        }
    }

    @Test
    void correctlyAddsTwoRolls() {
        int[] test = dice.rollTwice();
        assertEquals(test[0] + test[1], test[2]);
    }

    @Test
    void sameSeedGetsSameDice() {
        Dice dice1 = new Dice(1);
        Dice dice2 = new Dice(1);
        for (int i = 0; i < 1000; i++) {
            assertEquals(dice1.roll(), dice2.roll());
        }
    }

    @Test
    void differentSeedsGetsDifferentDice() {
        Dice dice1 = new Dice(1);
        Dice dice2 = new Dice(2);
        ArrayList<Integer> dice1Rolls = new ArrayList<>();
        ArrayList<Integer> dice2Rolls = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            dice1Rolls.add(dice1.roll());
            dice2Rolls.add(dice2.roll());
        }
        assertNotEquals(dice1Rolls, dice2Rolls);
    }
}
