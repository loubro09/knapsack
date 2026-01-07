package main.Greedy.SackSelectionStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import main.Item;
import main.Sack;
import main.Solution;

/**
 * RandomSackStrategy selects a random sack that can accommodate the item.
 */
public class RandomSackStrategy implements SelectSackForItem {
    private Random random = new Random();

    @Override
    public Sack selectSackForItem(Item item, List<Sack> sacks, Solution solution) {
        List<Sack> fittingSacks = new ArrayList<>();
        for (Sack sack : sacks) {
            if (solution.canItemFitInSack(item, sack)) {
                fittingSacks.add(sack);
            }
        }
        if (fittingSacks.isEmpty()) {
            return null;
        }
        return fittingSacks.get(random.nextInt(fittingSacks.size()));
    }
}
