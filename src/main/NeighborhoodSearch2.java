package main;

import java.util.ArrayList;
import java.util.List;

public class NeighborhoodSearch2 {

    public Solution improveSolution(Solution solution) {

        boolean improvementFound = true;

        while (improvementFound) {
            improvementFound = false;

            int currentProfit = solution.getProfit();
            int bestProfit = currentProfit;

            Sack bestSource = null;
            Sack bestTarget = null;
            Item bestItem = null;

            Sack swapSackA = null;
            Sack swapSackB = null;
            Item swapItemA = null;
            Item swapItemB = null;

            boolean isSwap = false;

            List<Sack> sacks = solution.getSacks();
            List<Item> allItems = solution.getItems();

            //Single-item move
            for (Sack sourceSack : sacks) {
                for (Item item : new ArrayList<>(sourceSack.getItems())) {
                    for (Sack targetSack : sacks) {
                        if (sourceSack == targetSack) continue;

                        if (solution.canItemFitInSack(item, targetSack)) {

                            solution.removeItemFromSack(item, sourceSack);
                            solution.addItemToSack(item, targetSack);

                            int newProfit = solution.getProfit();
                            if (newProfit > bestProfit) {
                                bestProfit = newProfit;
                                bestSource = sourceSack;
                                bestTarget = targetSack;
                                bestItem = item;
                                isSwap = false;
                            }

                            // rollback
                            solution.removeItemFromSack(item, targetSack);
                            solution.addItemToSack(item, sourceSack);
                        }
                    }
                }
            }

            // Two-item swap
            for (int i = 0; i < sacks.size(); i++) {
                Sack sackA = sacks.get(i);
                for (int j = i + 1; j < sacks.size(); j++) {
                    Sack sackB = sacks.get(j);

                    for (Item itemA : new ArrayList<>(sackA.getItems())) {
                        for (Item itemB : new ArrayList<>(sackB.getItems())) {

                            solution.removeItemFromSack(itemA, sackA);
                            solution.removeItemFromSack(itemB, sackB);

                            if (solution.canItemFitInSack(itemA, sackB)
                                    && solution.canItemFitInSack(itemB, sackA)) {

                                solution.addItemToSack(itemA, sackB);
                                solution.addItemToSack(itemB, sackA);

                                int newProfit = solution.getProfit();
                                if (newProfit > bestProfit) {
                                    bestProfit = newProfit;
                                    swapSackA = sackA;
                                    swapSackB = sackB;
                                    swapItemA = itemA;
                                    swapItemB = itemB;
                                    isSwap = true;
                                }

                                // rollback add
                                solution.removeItemFromSack(itemA, sackB);
                                solution.removeItemFromSack(itemB, sackA);
                            }

                            // rollback remove
                            solution.addItemToSack(itemA, sackA);
                            solution.addItemToSack(itemB, sackB);
                        }
                    }
                }
            }

            // --------------------------
            // New improvement: insert unused items
            // --------------------------
            for (Item item : allItems) {
                if (item.getAssignedSack() == -1) { // unused
                    for (Sack sack : sacks) {
                        if (solution.canItemFitInSack(item, sack)) {
                            solution.addItemToSack(item, sack);
                            int newProfit = solution.getProfit();
                            if (newProfit > bestProfit) {
                                bestProfit = newProfit;
                                bestItem = item;
                                bestSource = null; // item was unused
                                bestTarget = sack;
                                isSwap = false;
                                improvementFound = true;
                            } else {
                                // rollback if not better
                                solution.removeItemFromSack(item, sack);
                            }
                        }
                    }
                }
            }

            // Best found move
            if (bestProfit > currentProfit) {
                improvementFound = true;

                if (isSwap) {
                    solution.removeItemFromSack(swapItemA, swapSackA);
                    solution.removeItemFromSack(swapItemB, swapSackB);
                    solution.addItemToSack(swapItemA, swapSackB);
                    solution.addItemToSack(swapItemB, swapSackA);
                } else {
                    if (bestSource != null) {
                        solution.removeItemFromSack(bestItem, bestSource);
                    }
                    solution.addItemToSack(bestItem, bestTarget);
                }
            }
        }
        return solution;
    }
}