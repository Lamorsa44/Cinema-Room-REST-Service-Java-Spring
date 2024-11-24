package cinema;

import java.util.ArrayList;
import java.util.List;

public record Theater(int rows, int columns, List<Seat> seats) {

    public Theater(int rows, int columns) {
        this(rows, columns, generateSeats(rows, columns));
    }

    private static List<Seat> generateSeats(int rows, int columns) {
        var seats = new ArrayList<Seat>();

        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= columns; col++) {
                seats.add(new Seat(row, col));
            }
        }

        return seats;
    }
}
