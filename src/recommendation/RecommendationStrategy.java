package recommendation;

import model.Patron;

import java.util.List;

/**
 * Strategy interface to provide book recommendations.
 * Implementations will return a list of ISBNs recommended for the given patron.
 */
public interface RecommendationStrategy {
    List<String> recommendFor(Patron patron);
}
