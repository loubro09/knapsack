package main;

import java.util.ArrayList;
import java.util.List;

//TODO: write comments, improve algorithm to swap multiple items at a time and not only one?
public class NeighborhoodSearch {

    public Solution improveSolution(Solution solution) {
        boolean improvementFound = true;

        while (improvementFound) {
            improvementFound = false;
            int currentProfit = solution.getProfit();

            List<Sack> sacks = solution.getSacks();

            for (Sack sourceSack : sacks) {
                for (Item item : new ArrayList<>(sourceSack.getItems())) {
                    for (Sack targetSack : sacks) {
                        if (sourceSack == targetSack) continue;

                        if (solution.canItemFitInSack(item, targetSack)) {
                            solution.removeItemFromSack(item, sourceSack);
                            solution.addItemToSack(item, targetSack);

                            if (solution.getProfit() > currentProfit) {
                                improvementFound = true;
                                break;
                            } else {
                                solution.removeItemFromSack(item, targetSack);
                                solution.addItemToSack(item, sourceSack);
                            }
                        }
                    }
                    if (improvementFound) break;
                }
                if (improvementFound) break;
            }
        }
        return solution;
    }
}
