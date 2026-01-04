package main.Greedy.TerminationCondition;

import main.Item;
import main.Solution;

import java.util.List;

/**
 * Interface for defining termination conditions for the greedy algorithm.
 */
public interface TerminationCondition {
    boolean shouldTerminate(Solution solution, List<Item> items, int iteration);
}
