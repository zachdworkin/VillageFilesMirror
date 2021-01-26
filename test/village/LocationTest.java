package village;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {
    @Test
    void takesValidInput(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                Location location = new Location(i, j);
                assertEquals(location.getRow(), i);
                assertEquals(location.getCol(), j);
            }
        }
    }

    @Test
    void rejectsNegativeRow(){
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {new Location(-1, 3);} );
    }

    @Test
    void rejectsNegativeCol(){
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {new Location(1, -3);} );
    }

    @Test
    void rejectsTooLargeRow(){
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {new Location(6, 3);} );
    }

    @Test
    void rejectsTooLargeCol(){
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {new Location(3, 7);} );
    }
}