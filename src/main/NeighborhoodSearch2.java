package main;

import java.util.ArrayList;
import java.util.List;

public class NeighborhoodSearch2 {

    public Solution improveSolution(Solution solution) {
        boolean improvementFound = true;

        while (improvementFound) {
            improvementFound = false;
            int currentProfit = solution.getProfit();

            // Best move in this iteration
            int bestProfit = currentProfit;
            Sack bestSource = null;
            Sack bestTarget = null;
            Item bestItemToAdd = null;
            List<Item> itemsToRemove = new ArrayList<>();
            // destination sacks aligned by index with itemsToRemove
            List<Sack> destSacksForRemoved = new ArrayList<>();

            List<Sack> sacks = solution.getSacks();
            List<Item> allItems = solution.getItems();

            // --- 1. Single-item move between sacks ---
            for (Sack source : sacks) {
                for (Item item : new ArrayList<>(source.getItems())) {
                    for (Sack target : sacks) {
                        if (source == target) continue;

                        if (solution.canItemFitInSack(item, target)) {
                            solution.removeItemFromSack(item, source);
                            solution.addItemToSack(item, target);

                            int newProfit = solution.getProfit();
                            if (newProfit > bestProfit) {
                                bestProfit = newProfit;
                                bestSource = source;
                                bestTarget = target;
                                bestItemToAdd = item;
                                itemsToRemove.clear();
                                destSacksForRemoved.clear();
                            }

                            // rollback
                            solution.removeItemFromSack(item, target);
                            solution.addItemToSack(item, source);
                        }
                    }
                }
            }

            // --- 2. Insert unused items (with rotation) ---
            for (Item unused : allItems) {
                if (unused.getAssignedSack() != -1) continue; // skip used items

                for (Sack target : sacks) {
                    if (solution.canItemFitInSack(unused, target)) {
                        solution.addItemToSack(unused, target);
                        int newProfit = solution.getProfit();
                        if (newProfit > bestProfit) {
                            bestProfit = newProfit;
                            bestSource = null;
                            bestTarget = target;
                            bestItemToAdd = unused;
                            itemsToRemove.clear();
                            destSacksForRemoved.clear();
                        }
                        solution.removeItemFromSack(unused, target);
                    } else {
                        // Try rotations: remove 1–3 items to fit
                        List<Item> sackItems = new ArrayList<>(target.getItems());
                        for (int i = 1; i <= Math.min(3, sackItems.size()); i++) {
                            List<List<Item>> combos = Utils.getCombinations(sackItems, i);
                            for (List<Item> combo : combos) {
                                int totalWeight = combo.stream().mapToInt(Item::getWeight).sum();
                                if (unused.getWeight() <= totalWeight + target.getRemainingCapacity()) {
                                    // Try to move removed items elsewhere
                                    boolean canMoveAll = true;
                                    List<Sack> originalSacks = new ArrayList<>();
                                    for (Item rem : combo) {
                                        Sack movedTo = findSackForItem(solution, rem, target);
                                        if (movedTo == null) {
                                            canMoveAll = false;
                                            break;
                                        } else {
                                            originalSacks.add(movedTo);
                                        }
                                    }

                                    if (canMoveAll) {
                                        // Apply move: remove combo
                                        combo.forEach(it -> solution.removeItemFromSack(it, target));
                                        // Move removed items elsewhere
                                        for (int k = 0; k < combo.size(); k++) {
                                            solution.addItemToSack(combo.get(k), originalSacks.get(k));
                                        }
                                        // Add unused item
                                        solution.addItemToSack(unused, target);

                                        int newProfit = solution.getProfit();
                                        if (newProfit > bestProfit) {
                                            bestProfit = newProfit;
                                            bestSource = null;
                                            bestTarget = target;
                                            bestItemToAdd = unused;
                                            itemsToRemove = new ArrayList<>(combo);
                                            // store actual moved sacks for applying later (aligned by index)
                                            destSacksForRemoved = new ArrayList<>(originalSacks);
                                        }

                                        // rollback
                                        solution.removeItemFromSack(unused, target);
                                        for (int k = 0; k < combo.size(); k++) {
                                            solution.removeItemFromSack(combo.get(k), originalSacks.get(k));
                                            solution.addItemToSack(combo.get(k), target);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- 3. Apply best move ---
            if (bestProfit > currentProfit) {
                improvementFound = true;

                // Case A: rotation-based insertion (remove some from bestTarget and move them elsewhere, then add bestItem)
                if (bestSource == null && !itemsToRemove.isEmpty()) {
                    // remove selected items from the target sack first
                    for (Item it : itemsToRemove) {
                        solution.removeItemFromSack(it, bestTarget);
                    }
                    // move them to their recorded destination sacks
                    for (int i = 0; i < itemsToRemove.size(); i++) {
                        Item it = itemsToRemove.get(i);
                        Sack dest = destSacksForRemoved.get(i);
                        solution.addItemToSack(it, dest);
                    }
                    // finally insert the chosen best item into the freed target sack
                    solution.addItemToSack(bestItemToAdd, bestTarget);
                } else {
                    // Case B: single-item move or simple insertion
                    if (bestSource != null) {
                        // move from source to target
                        solution.removeItemFromSack(bestItemToAdd, bestSource);
                        solution.addItemToSack(bestItemToAdd, bestTarget);
                    } else {
                        // simple insertion into target (previously unused item)
                        solution.addItemToSack(bestItemToAdd, bestTarget);
                    }
                }
            }
        }

        return solution;
    }

    // Find a sack (other than excludeSack) that can fit the item
    private Sack findSackForItem(Solution solution, Item item, Sack excludeSack) {
        for (Sack sack : solution.getSacks()) {
            if (sack != excludeSack && solution.canItemFitInSack(item, sack)) {
                return sack;
            }
        }
        return null;
    }
}
