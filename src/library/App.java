package library;

import factory.EntityFactory;
import model.*;
import repository.LibraryInventory;
import recommendation.*;
import service.LendingService;
import service.ReservationService;
import util.LoggerConfig;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Demo main to illustrate usage.
 */
public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        LoggerConfig.configure();
        LibraryInventory inventory = new LibraryInventory();
        ReservationService reservationService = new ReservationService();
        LendingService lendingService = new LendingService(inventory);

        // Create some books and add copies
        Book b1 = EntityFactory.createBook("ISBN-001", "Effective Java", "Joshua Bloch", 2018);
        Book b2 = EntityFactory.createBook("ISBN-002", "Clean Code", "Robert C. Martin", 2008);
        inventory.addBook(b1, 2); // 2 copies
        inventory.addBook(b2, 1);

        // Create two patrons
        Patron alice = EntityFactory.createPatron("Alice", "alice@example.com");
        Patron bob = EntityFactory.createPatron("Bob", "bob@example.com");

        // Checkout a copy
        Optional<Loan> loan1 = lendingService.checkout("ISBN-001", alice, 14);
        loan1.ifPresent(l -> LOGGER.info("Loan issued: " + l));

        // Bob tries to checkout same ISBN (one copy still available)
        Optional<Loan> loan2 = lendingService.checkout("ISBN-001", bob, 14);
        loan2.ifPresent(l -> LOGGER.info("Loan issued: " + l));

        // Now all copies of ISBN-001 are gone; Charlie reserves
        Patron charlie = EntityFactory.createPatron("Charlie", "charlie@example.com");
        reservationService.reserveBook("ISBN-001", charlie);

        // Alice returns her copy -> reservation service notified
        loan1.ifPresent(l -> {
            boolean returned = lendingService.returnCopy(l.getCopyId());
            if (returned) {
                reservationService.notifyNextPatronIfAny(l.getIsbn())
                        .ifPresent(p -> LOGGER.info("Notified patron: " + p.getPatronId()));
            }
        });

        // Recommendation demo
        RecommendationService recService = new RecommendationService(new MostBorrowedStrategy());
        List<String> recsForAlice = recService.recommend(alice);
        LOGGER.info("Recommendations for Alice: " + recsForAlice);
    }
}
