package recommendation;

import model.Patron;

import java.util.List;
import java.util.Objects;

/**
 * Uses a pluggable strategy to produce recommendations.
 */
public class RecommendationService {
    private RecommendationStrategy strategy;

    public RecommendationService(RecommendationStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy);
    }

    public void setStrategy(RecommendationStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy);
    }

    public List<String> recommend(Patron patron) {
        return strategy.recommendFor(patron);
    }
}
