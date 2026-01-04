package main.Greedy.SackSelectionStrategy;

import java.util.List;
import main.Item;
import main.Sack;
import main.Solution;

/**
 * Interface for selecting a sack for a given item based on a specific strategy.
 */
public interface SelectSackForItem {
    Sack selectSackForItem(Item item, List<Sack> sacks, Solution solution);
}