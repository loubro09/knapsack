package main.Greedy.TerminationCondition;

import main.Item;
import main.Sack;
import main.Solution;

import java.util.List;

/**
 * Termination condition that checks if no more items can fit into any sack.
 */
public class NoMoreFitCondition implements TerminationCondition {
    @Override
    public boolean shouldTerminate(Solution solution, List<Item> items, int iteration) {
        for (Item item : items) {
           if (item.getAssignedSack() == -1) {
               for (Sack sack: solution.getSacks()) {
                   if (solution.canItemFitInSack(item, sack)) {
                       return false; //Found an item that can still fit in a sack
                   }
               }
           }
        }
        return true; //No more items can fit in any sack
    }
}
