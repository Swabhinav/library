package recommendation;

import model.Patron;
import model.Loan;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Very small dummy strategy: returns ISBNs from patron history grouped by author similarity.
 * Here we only re-order patron's history by frequency, because we don't have global library matrix.
 */
public class AuthorSimilarityStrategy implements RecommendationStrategy {
    private static final Logger LOGGER = Logger.getLogger(AuthorSimilarityStrategy.class.getName());

    @Override
    public List<String> recommendFor(Patron patron) {
        // Placeholder: just reverse history ISBNs to simulate "different" strategy
        List<String> rec = patron.getBorrowingHistory().stream()
                .map(Loan::getIsbn)
                .distinct()
                .collect(Collectors.toList());
        Collections.reverse(rec);
        LOGGER.info("AuthorSimilarityStrategy recommendations for " + patron.getPatronId() + ": " + rec);
        return rec;
    }
}
