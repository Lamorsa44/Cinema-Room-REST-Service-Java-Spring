package cinema;

import java.util.UUID;

public record Ticket(UUID token, Seat ticket) {

    public Ticket(Seat ticket) {
        this(UUID.randomUUID(), ticket);
    }

    public Ticket(UUID token) {
        this(token, null);
    }
}
