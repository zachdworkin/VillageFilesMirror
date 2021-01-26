package village;

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
}
