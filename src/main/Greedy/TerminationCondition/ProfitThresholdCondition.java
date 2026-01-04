package main.Greedy.TerminationCondition;

import main.Item;
import main.Solution;

import java.util.List;

/**
 * Termination condition that checks if the total profit has reached a specified threshold.
 */
public class ProfitThresholdCondition implements TerminationCondition {
    private final double profitThreshold;

    public ProfitThresholdCondition(double profitThreshold) {
        this.profitThreshold = profitThreshold;
    }

    @Override
    public boolean shouldTerminate(Solution solution, List<Item> items, int iteration) {
        return solution.getProfit() >= profitThreshold;
    }
}
