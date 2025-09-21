package service;

import model.Patron;

import java.util.*;
import java.util.logging.Logger;

/**
 * Reservation system using Observer pattern.
 * Patrons subscribe to a book (by ISBN) to be notified when a copy is available.
 */
public class ReservationService {
    private static final Logger LOGGER = Logger.getLogger(ReservationService.class.getName());

    // isbn -> queue of patrons waiting for that ISBN (FIFO)
    private final Map<String, Deque<Patron>> reservations = new HashMap<>();

    public synchronized void reserveBook(String isbn, Patron patron) {
        Deque<Patron> queue = reservations.computeIfAbsent(isbn, k -> new ArrayDeque<>());
        // For simplicity avoid duplicates
        if (queue.stream().noneMatch(p -> p.getPatronId().equals(patron.getPatronId()))) {
            queue.addLast(patron);
            LOGGER.info("Patron " + patron.getPatronId() + " reserved ISBN " + isbn);
        } else {
            LOGGER.info("Patron " + patron.getPatronId() + " already in queue for ISBN " + isbn);
        }
    }

    /**
     * Call when a copy is returned. If there are reservations, pop next and notify.
     * Returns the next Patron to notify, or empty if none.
     */
    public synchronized Optional<Patron> notifyNextPatronIfAny(String isbn) {
        Deque<Patron> queue = reservations.get(isbn);
        if (queue == null || queue.isEmpty()) return Optional.empty();
        Patron next = queue.pollFirst();
        // Notification simulated via logging; in real system we'd send email/push.
        LOGGER.info("Notifying patron " + next.getPatronId() + " that ISBN " + isbn + " is available");
        return Optional.of(next);
    }

    public synchronized List<Patron> listReservations(String isbn) {
        return reservations.getOrDefault(isbn, new ArrayDeque<>()).stream().toList();
    }

    public synchronized void cancelReservation(String isbn, Patron patron) {
        Deque<Patron> queue = reservations.get(isbn);
        if (queue == null) return;
        queue.removeIf(p -> p.getPatronId().equals(patron.getPatronId()));
        LOGGER.info("Cancelled reservation for patron " + patron.getPatronId() + " on ISBN " + isbn);
    }
}
