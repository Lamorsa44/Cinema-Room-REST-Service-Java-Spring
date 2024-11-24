package cinema;

public record Seat(int row, int column, int price) {

    public Seat(int row, int column) {
        this(row, column, row <= 4 ? 10 : 8);
    }
}