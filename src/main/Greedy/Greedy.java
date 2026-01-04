package main.Greedy;

import main.Greedy.SackSelectionStrategy.SelectSackForItem;
import main.Greedy.TerminationCondition.TerminationCondition;
import main.Item;
import main.Sack;
import main.Solution;

import java.util.List;

/**
 * Greedy.Greedy class implementing a greedy algorithm to solve the knapsack filling problem.
 *
 */
public class Greedy {
    private final SelectSackForItem sackSelectionStrategy;
    private final TerminationCondition terminationCondition;

    public Greedy(SelectSackForItem sackSelectionStrategy, TerminationCondition terminationCondition) {
        this.sackSelectionStrategy = sackSelectionStrategy;
        this.terminationCondition = terminationCondition;
    }

    //Solve the sack filling problem using a greedy approach
    public Solution solve(List<Sack> sacks, List<Item> items) {
        Solution solution = new Solution(sacks, items);

        //Sort items by profit in descending order
        items.sort((a, b) -> Double.compare(b.getProfit(), a.getProfit()));

        int iterations = 0;

        while(!terminationCondition.shouldTerminate(solution, items, iterations)) {
            boolean addedItem = false;
            for (Item item : items) {
                if (item.getAssignedSack() == -1) {
                    Sack sack = sackSelectionStrategy.selectSackForItem(item, sacks, solution);
                    if (sack != null) {
                        solution.addItemToSack(item, sack);
                        addedItem = true;
                    }
                }
            }
            if (!addedItem) break;
            iterations++;
        }

        return solution;
    }
}
