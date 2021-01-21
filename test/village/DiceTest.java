package village;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {
    @Test
    void rollsOnlyValidSides() {
        for (int i = 0; i < 10000; i++) {
            int roll = Dice.roll();
            assertTrue(roll >= 1 && roll <= 6);
        }
    }

    @Test
    void rollsAllSidesEventually() {
        boolean[] sides = new boolean[7];
        boolean allSidesRolled = false;
        int times = 0;
        while (!allSidesRolled) {
            int roll = Dice.roll();
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
}
