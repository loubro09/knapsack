package main.Greedy.SackSelectionStrategy;

import java.util.List;
import main.Item;
import main.Sack;
import main.Solution;

/**
 * Greedy.SackSelectionStrategy.LeastSpaceStrategy selects the sack that will have the least remaining capacity after adding the item.
 */
public class LeastSpaceStrategy implements SelectSackForItem {
    @Override
    public Sack selectSackForItem(Item item, List<Sack> sacks, Solution solution) {
        Sack bestSack = null;
        int minRemainingCapacity = Integer.MAX_VALUE;

        for (Sack sack : sacks) {
            if (solution.canItemFitInSack(item, sack)) {
                int remainingCapacity = sack.getCapacity() - (sack.getCurrentWeight());
                if (remainingCapacity < minRemainingCapacity) {
                    minRemainingCapacity = remainingCapacity;
                    bestSack = sack;
                }
            }
        }
        return bestSack;
    }
}
