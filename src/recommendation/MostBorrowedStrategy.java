package recommendation;

import model.Patron;
import model.Loan;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Simple heuristic: recommend most-borrowed books from the patron's history
 * (e.g., authors or ISBNs that repeated).
 *
 * Note: In a real app this would consult global borrow frequencies and collaborative filtering.
 */
public class MostBorrowedStrategy implements RecommendationStrategy {
    private static final Logger LOGGER = Logger.getLogger(MostBorrowedStrategy.class.getName());

    @Override
    public List<String> recommendFor(Patron patron) {
        Map<String, Long> freq = patron.getBorrowingHistory().stream()
                .collect(Collectors.groupingBy(Loan::getIsbn, Collectors.counting()));

        List<String> sorted = freq.entrySet().stream()
                .sorted(Comparator.<Map.Entry<String, Long>>comparingLong(Map.Entry::getValue).reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        LOGGER.info("MostBorrowedStrategy recommendations for " + patron.getPatronId() + ": " + sorted);
        return sorted;
    }
}
