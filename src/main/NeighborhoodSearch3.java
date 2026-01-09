package main;

import java.util.ArrayList;
import java.util.List;

/**
 * NeighborhoodSearch3 - An advanced neighborhood search implementation for the Knapsack Problem.
 * 
 * This implementation explores the solution space by considering multiple types of moves:
 * 1. Single-item swaps between sacks
 * 2. Adding unused items to sacks
 * 3. Multi-item swaps (exchange multiple items between two sacks)
 * 4. Replace operations (remove items from a sack to make room for a better item)
 * 
 * The algorithm uses a best-improvement strategy: it explores all possible neighbors in each
 * iteration and selects the move that provides the maximum profit improvement.
 */
public class NeighborhoodSearch3 {

    /**
     * Improves the given solution using neighborhood search.
     * The algorithm iteratively explores the neighborhood of the current solution and moves
     * to a better neighbor until no further improvement can be found (local optimum).
     * 
     * @param solution The initial solution to improve
     * @return The improved solution
     */
    public Solution improveSolution(Solution solution) {
        boolean improved = true;
        int iterations = 0;
        
        System.out.println("\n=== Starting Neighborhood Search ===");
        System.out.println("Initial profit: " + solution.getProfit());
        
        while (improved) {
            iterations++;
            int currentProfit = solution.getProfit();
            
            // Explore all possible moves and find the best one
            Move bestMove = findBestMove(solution);
            
            // If we found an improving move, apply it
            if (bestMove != null && bestMove.profitGain > 0) {
                applyMove(solution, bestMove);
                int newProfit = solution.getProfit();
                System.out.println("Iteration " + iterations + ": " + bestMove.description + 
                                   " -> Profit: " + newProfit + " (+" + (newProfit - currentProfit) + ")");
                improved = true;
            } else {
                improved = false;
                System.out.println("No improving move found. Local optimum reached.");
            }
        }
        
        System.out.println("Final profit: " + solution.getProfit());
        System.out.println("Total iterations: " + iterations);
        System.out.println("=== Neighborhood Search Complete ===\n");
        
        return solution;
    }

    /**
     * Explores all possible moves and returns the one with the highest profit gain.
     * 
     * @param solution The current solution
     * @return The best move found, or null if no improving move exists
     */
    private Move findBestMove(Solution solution) {
        Move bestMove = null;
        int currentProfit = solution.getProfit();
        
        // Try all types of moves and keep track of the best one
        
        // 1. Try moving single items between sacks
        Move bestSingleMove = findBestSingleItemMove(solution, currentProfit);
        if (bestSingleMove != null && (bestMove == null || bestSingleMove.profitGain > bestMove.profitGain)) {
            bestMove = bestSingleMove;
        }
        
        // 2. Try adding unused items to sacks
        Move bestAddMove = findBestAddUnusedItem(solution, currentProfit);
        if (bestAddMove != null && (bestMove == null || bestAddMove.profitGain > bestMove.profitGain)) {
            bestMove = bestAddMove;
        }
        
        // 3. Try swapping two items between different sacks
        Move bestSwapMove = findBestTwoItemSwap(solution, currentProfit);
        if (bestSwapMove != null && (bestMove == null || bestSwapMove.profitGain > bestMove.profitGain)) {
            bestMove = bestSwapMove;
        }
        
        // 4. Try replacing items in a sack with an unused item
        Move bestReplaceMove = findBestReplaceMove(solution, currentProfit);
        if (bestReplaceMove != null && (bestMove == null || bestReplaceMove.profitGain > bestMove.profitGain)) {
            bestMove = bestReplaceMove;
        }
        
        return bestMove;
    }

    /**
     * Find the best move that involves moving a single item from one sack to another.
     */
    private Move findBestSingleItemMove(Solution solution, int currentProfit) {
        Move bestMove = null;
        List<Sack> sacks = solution.getSacks();
        
        for (Sack sourceSack : sacks) {
            List<Item> itemsCopy = new ArrayList<>(sourceSack.getItems());
            
            for (Item item : itemsCopy) {
                for (Sack targetSack : sacks) {
                    if (sourceSack.getId() == targetSack.getId()) continue;
                    
                    // Check if item can fit in target sack
                    if (solution.canItemFitInSack(item, targetSack)) {
                        // Simulate the move
                        solution.removeItemFromSack(item, sourceSack);
                        solution.addItemToSack(item, targetSack);
                        
                        int newProfit = solution.getProfit();
                        int profitGain = newProfit - currentProfit;
                        
                        if (profitGain > 0 && (bestMove == null || profitGain > bestMove.profitGain)) {
                            bestMove = new Move();
                            bestMove.type = MoveType.SINGLE_MOVE;
                            bestMove.item1 = item;
                            bestMove.sourceSack = sourceSack;
                            bestMove.targetSack = targetSack;
                            bestMove.profitGain = profitGain;
                            bestMove.description = "Move " + item.getName() + " from Sack " + 
                                                  sourceSack.getId() + " to Sack " + targetSack.getId();
                        }
                        
                        // Undo the move
                        solution.removeItemFromSack(item, targetSack);
                        solution.addItemToSack(item, sourceSack);
                    }
                }
            }
        }
        
        return bestMove;
    }

    /**
     * Find the best move that involves adding an unused item to a sack.
     */
    private Move findBestAddUnusedItem(Solution solution, int currentProfit) {
        Move bestMove = null;
        List<Item> allItems = solution.getItems();
        List<Sack> sacks = solution.getSacks();
        
        for (Item item : allItems) {
            // Only consider unused items
            if (item.getAssignedSack() != -1) continue;
            
            for (Sack sack : sacks) {
                if (solution.canItemFitInSack(item, sack)) {
                    // Simulate adding the item
                    solution.addItemToSack(item, sack);
                    
                    int newProfit = solution.getProfit();
                    int profitGain = newProfit - currentProfit;
                    
                    if (profitGain > 0 && (bestMove == null || profitGain > bestMove.profitGain)) {
                        bestMove = new Move();
                        bestMove.type = MoveType.ADD_UNUSED;
                        bestMove.item1 = item;
                        bestMove.targetSack = sack;
                        bestMove.profitGain = profitGain;
                        bestMove.description = "Add unused item " + item.getName() + " to Sack " + sack.getId();
                    }
                    
                    // Undo the addition
                    solution.removeItemFromSack(item, sack);
                }
            }
        }
        
        return bestMove;
    }

    /**
     * Find the best move that involves swapping two items between different sacks.
     * This can help when moving a single item doesn't work due to capacity constraints.
     */
    private Move findBestTwoItemSwap(Solution solution, int currentProfit) {
        Move bestMove = null;
        List<Sack> sacks = solution.getSacks();
        
        for (int i = 0; i < sacks.size(); i++) {
            Sack sack1 = sacks.get(i);
            List<Item> items1 = new ArrayList<>(sack1.getItems());
            
            for (int j = i + 1; j < sacks.size(); j++) {
                Sack sack2 = sacks.get(j);
                List<Item> items2 = new ArrayList<>(sack2.getItems());
                
                for (Item item1 : items1) {
                    for (Item item2 : items2) {
                        // Check if both items can be swapped
                        int sack1NewWeight = sack1.getCurrentWeight() - item1.getWeight() + item2.getWeight();
                        int sack2NewWeight = sack2.getCurrentWeight() - item2.getWeight() + item1.getWeight();
                        
                        if (sack1NewWeight <= sack1.getCapacity() && sack2NewWeight <= sack2.getCapacity()) {
                            // Simulate the swap
                            solution.removeItemFromSack(item1, sack1);
                            solution.removeItemFromSack(item2, sack2);
                            solution.addItemToSack(item2, sack1);
                            solution.addItemToSack(item1, sack2);
                            
                            int newProfit = solution.getProfit();
                            int profitGain = newProfit - currentProfit;
                            
                            if (profitGain > 0 && (bestMove == null || profitGain > bestMove.profitGain)) {
                                bestMove = new Move();
                                bestMove.type = MoveType.SWAP;
                                bestMove.item1 = item1;
                                bestMove.item2 = item2;
                                bestMove.sourceSack = sack1;
                                bestMove.targetSack = sack2;
                                bestMove.profitGain = profitGain;
                                bestMove.description = "Swap " + item1.getName() + " (Sack " + sack1.getId() + 
                                                      ") with " + item2.getName() + " (Sack " + sack2.getId() + ")";
                            }
                            
                            // Undo the swap
                            solution.removeItemFromSack(item2, sack1);
                            solution.removeItemFromSack(item1, sack2);
                            solution.addItemToSack(item1, sack1);
                            solution.addItemToSack(item2, sack2);
                        }
                    }
                }
            }
        }
        
        return bestMove;
    }

    /**
     * Find the best move that involves removing one or more items from a sack
     * to make room for a higher-value unused item.
     */
    private Move findBestReplaceMove(Solution solution, int currentProfit) {
        Move bestMove = null;
        List<Item> allItems = solution.getItems();
        List<Sack> sacks = solution.getSacks();
        
        for (Item unusedItem : allItems) {
            // Only consider unused items
            if (unusedItem.getAssignedSack() != -1) continue;
            
            for (Sack sack : sacks) {
                // If the item doesn't fit directly, try removing items
                if (!solution.canItemFitInSack(unusedItem, sack) && sack.getItems().size() > 0) {
                    List<Item> sackItems = new ArrayList<>(sack.getItems());
                    
                    // Try removing 1 to 3 items to make room
                    for (int removeCount = 1; removeCount <= Math.min(3, sackItems.size()); removeCount++) {
                        List<List<Item>> combinations = Utils.getCombinations(sackItems, removeCount);
                        
                        for (List<Item> itemsToRemove : combinations) {
                            int removedWeight = itemsToRemove.stream().mapToInt(Item::getWeight).sum();
                            int removedValue = itemsToRemove.stream().mapToInt(Item::getValue).sum();
                            
                            // Check if removing these items makes room for the unused item
                            if (unusedItem.getWeight() <= sack.getRemainingCapacity() + removedWeight) {
                                // Simulate the replacement
                                for (Item item : itemsToRemove) {
                                    solution.removeItemFromSack(item, sack);
                                }
                                solution.addItemToSack(unusedItem, sack);
                                
                                int newProfit = solution.getProfit();
                                int profitGain = newProfit - currentProfit;
                                
                                if (profitGain > 0 && (bestMove == null || profitGain > bestMove.profitGain)) {
                                    bestMove = new Move();
                                    bestMove.type = MoveType.REPLACE;
                                    bestMove.item1 = unusedItem;
                                    bestMove.itemsToRemove = new ArrayList<>(itemsToRemove);
                                    bestMove.targetSack = sack;
                                    bestMove.profitGain = profitGain;
                                    
                                    String removedNames = String.join(", ", 
                                        itemsToRemove.stream().map(Item::getName).toArray(String[]::new));
                                    bestMove.description = "Replace [" + removedNames + "] in Sack " + 
                                                          sack.getId() + " with " + unusedItem.getName();
                                }
                                
                                // Undo the replacement
                                solution.removeItemFromSack(unusedItem, sack);
                                for (Item item : itemsToRemove) {
                                    solution.addItemToSack(item, sack);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return bestMove;
    }

    /**
     * Apply the given move to the solution.
     */
    private void applyMove(Solution solution, Move move) {
        switch (move.type) {
            case SINGLE_MOVE:
                solution.removeItemFromSack(move.item1, move.sourceSack);
                solution.addItemToSack(move.item1, move.targetSack);
                break;
                
            case ADD_UNUSED:
                solution.addItemToSack(move.item1, move.targetSack);
                break;
                
            case SWAP:
                solution.removeItemFromSack(move.item1, move.sourceSack);
                solution.removeItemFromSack(move.item2, move.targetSack);
                solution.addItemToSack(move.item2, move.sourceSack);
                solution.addItemToSack(move.item1, move.targetSack);
                break;
                
            case REPLACE:
                for (Item item : move.itemsToRemove) {
                    solution.removeItemFromSack(item, move.targetSack);
                }
                solution.addItemToSack(move.item1, move.targetSack);
                break;
        }
    }

    /**
     * Enum representing the different types of moves in the neighborhood.
     */
    private enum MoveType {
        SINGLE_MOVE,  // Move an item from one sack to another
        ADD_UNUSED,   // Add an unused item to a sack
        SWAP,         // Swap two items between two sacks
        REPLACE       // Replace items in a sack with an unused item
    }

    /**
     * Inner class representing a potential move and its associated profit gain.
     */
    private static class Move {
        MoveType type;
        Item item1;
        Item item2;
        Sack sourceSack;
        Sack targetSack;
        List<Item> itemsToRemove;
        int profitGain;
        String description;
    }
}
