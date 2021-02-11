package village;

import java.util.Objects;

public class Location {
    private int row;
    private int col;

    public Location(int row, int col) {
        if(row >= 0  && row < 5) this.row = row;
        else throw new ArrayIndexOutOfBoundsException();

        if(col >= 0 && col < 6) this.col = col;
        else throw new ArrayIndexOutOfBoundsException();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return row == location.row &&
                col == location.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
