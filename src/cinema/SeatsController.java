package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class SeatsController {
    private final Theater theater = new Theater(9, 9);
    private final List<Seat> seats = Collections.synchronizedList(theater.seats());
    private final Set<Ticket> tickets = Collections.synchronizedSet(new HashSet<>());

    @GetMapping("/seats")
    public Theater seats() {
        return theater;
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> stats
            (@RequestParam(value = "password", required = false) Optional<String> password) {
        if (password.isEmpty() || !password.get().equals("super_secret"))
            return ResponseEntity.status(401)
                    .body(new Error("The password is wrong!"));

        return ResponseEntity.ok
                (new Stats (tickets.stream().mapToInt(x -> x.ticket().price()).sum(),
                seats.size(), tickets.size())) ;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseSeat(@RequestBody Seat seat) {

        if (Objects.isNull(seat)) {
            return ResponseEntity.ok().build();
        }

        if (seat.row() > theater.rows() || seat.column() > theater.columns() ||
        seat.row() < 0 || seat.column() < 0) {
            return ResponseEntity.badRequest()
                    .body(new Error("The number of a row or a column is out of bounds!"));
        }

        Seat tmp = new Seat(seat.row(), seat.column());

        if (seats.contains(tmp)) {
            seats.remove(tmp);
            Ticket ticket = new Ticket(tmp);
            tickets.add(ticket);
            return ResponseEntity.ok(ticket);
        } else {
            return ResponseEntity.badRequest()
                    .body(new Error("The ticket has been already purchased!"));
        }
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnSeat(@RequestBody Ticket ticket) {
         Optional<Ticket> yea = tickets.stream()
                 .filter(t -> ticket.token().equals(t.token())).findFirst();

        if (yea.isPresent()) {
            Ticket tmp = yea.get();
            tickets.remove(tmp);
            seats.add(tmp.ticket());
            return ResponseEntity.ok(new Dummy(tmp.ticket()));
        } else {
            return ResponseEntity.badRequest()
                    .body(new Error("Wrong token!"));
        }
    }
}