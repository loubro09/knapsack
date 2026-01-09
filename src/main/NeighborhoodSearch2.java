package main;

import java.util.ArrayList;
import java.util.List;

public class NeighborhoodSearch2 {

    /**
     * Improves the given solution using a rotation-based neighborhood search.
     * The neighborhood considers:
     * 1. Moving an item from one sack to another.
     * 2. Evicting another item from the target sack to make room.
     * 3. Optionally inserting an unused item into the original sack.
     */
    public Solution improveSolution(Solution solution) {
        boolean improvementFound = true;

        while (improvementFound) {
            improvementFound = false;
            int currentProfit = solution.getProfit();
            int bestProfit = currentProfit;

            // Best move variables
            Item moveItem = null;          // item to move
            Sack sourceSack = null;
            Sack targetSack = null;
            Item evictItem = null;         // item to evict from target sack
            Item insertUnusedItem = null;  // item to insert into source sack

            List<Sack> sacks = solution.getSacks();
            List<Item> allItems = solution.getItems();

            // Track unused items
            List<Item> unusedItems = new ArrayList<>();
            for (Item item : allItems) {
                if (item.getAssignedSack() == -1) unusedItems.add(item);
            }

            // --- Explore all rotations ---
            for (Sack sackA : sacks) {
                for (Item itemA : new ArrayList<>(sackA.getItems())) {
                    for (Sack sackB : sacks) {
                        if (sackA == sackB) continue;

                        // Try direct move
                        if (solution.canItemFitInSack(itemA, sackB)) {
                            int trialProfit = currentProfit;

                            // compute profit if we move itemA
                            solution.removeItemFromSack(itemA, sackA);
                            solution.addItemToSack(itemA, sackB);
                            trialProfit = solution.getProfit();

                            // Check if we can insert unused item into sackA
                            Item bestInsert = null;
                            for (Item unused : unusedItems) {
                                if (solution.canItemFitInSack(unused, sackA)) {
                                    solution.addItemToSack(unused, sackA);
                                    int newProfit = solution.getProfit();
                                    if (newProfit > trialProfit) {
                                        trialProfit = newProfit;
                                        bestInsert = unused;
                                    } else {
                                        solution.removeItemFromSack(unused, sackA);
                                    }
                                }
                            }

                            if (trialProfit > bestProfit) {
                                bestProfit = trialProfit;
                                moveItem = itemA;
                                sourceSack = sackA;
                                targetSack = sackB;
                                evictItem = null;
                                insertUnusedItem = bestInsert;
                            }

                            // rollback
                            solution.removeItemFromSack(itemA, sackB);
                            solution.addItemToSack(itemA, sackA);
                            if (bestInsert != null) solution.removeItemFromSack(bestInsert, sackA);

                        } else {
                            // If item doesn't fit, try evicting one item from target sack
                            for (Item itemB : new ArrayList<>(sackB.getItems())) {
                                if (itemB.getWeight() >= itemA.getWeight() - (sackB.getCapacity() - sackB.getCurrentWeight())) {

                                    int trialProfit = currentProfit;

                                    solution.removeItemFromSack(itemA, sackA);
                                    solution.removeItemFromSack(itemB, sackB);
                                    solution.addItemToSack(itemA, sackB);
                                    trialProfit = solution.getProfit();

                                    // Check if we can insert unused item into sackA
                                    Item bestInsert = null;
                                    for (Item unused : unusedItems) {
                                        if (solution.canItemFitInSack(unused, sackA)) {
                                            solution.addItemToSack(unused, sackA);
                                            int newProfit = solution.getProfit();
                                            if (newProfit > trialProfit) {
                                                trialProfit = newProfit;
                                                bestInsert = unused;
                                            } else {
                                                solution.removeItemFromSack(unused, sackA);
                                            }
                                        }
                                    }

                                    if (trialProfit > bestProfit) {
                                        bestProfit = trialProfit;
                                        moveItem = itemA;
                                        sourceSack = sackA;
                                        targetSack = sackB;
                                        evictItem = itemB;
                                        insertUnusedItem = bestInsert;
                                    }

                                    // rollback
                                    solution.removeItemFromSack(itemA, sackB);
                                    solution.addItemToSack(itemA, sackA);
                                    solution.addItemToSack(itemB, sackB);
                                    if (bestInsert != null) solution.removeItemFromSack(bestInsert, sackA);
                                }
                            }
                        }
                    }
                }
            }

            // --- Apply best rotation found ---
            if (bestProfit > currentProfit) {
                improvementFound = true;

                // Move item
                if (moveItem != null && sourceSack != null && targetSack != null) {
                    solution.removeItemFromSack(moveItem, sourceSack);
                    solution.addItemToSack(moveItem, targetSack);
                }

                // Evict item
                if (evictItem != null && targetSack != null) {
                    solution.removeItemFromSack(evictItem, targetSack);
                }

                // Insert unused item
                if (insertUnusedItem != null && sourceSack != null) {
                    solution.addItemToSack(insertUnusedItem, sourceSack);
                }
            }
        }

        return solution;
    }
}
