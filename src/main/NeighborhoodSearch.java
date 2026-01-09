package main;

import java.util.ArrayList;
import java.util.List;

/**
 * NeighborhoodSearch class implementing a local search algorithm to improve a given solution.
 */
public class NeighborhoodSearch {

    //Improves the given solution by exploring neighboring solutions through item rotations between sacks.
    public Solution improveSolution(Solution solution) {
        boolean improvementFound = true; //Flag if any improvement was found in the last iteration

        //Loop until no further improvements can be found
        while (improvementFound) {
            improvementFound = false;
            int currentProfit = solution.getProfit();
            int bestProfit = currentProfit; //Best profit found in this iteration

            Item moveItem = null; //item to move between sacks
            Sack sourceSack = null;
            Sack targetSack = null;
            Item removeItem = null; //item to remove from target sack to make space
            Item unusedItem = null; //unused item to insert into source sack

            List<Sack> sacks = solution.getSacks();
            List<Item> allItems = solution.getItems();

            //Identify unused items
            List<Item> unusedItems = new ArrayList<>();
            for (Item item : allItems) {
                if (item.getAssignedSack() == -1) unusedItems.add(item);
            }

            //Explore all possible item moves between sacks
            for (Sack sackA : sacks) { //source sack
                for (Item itemA : new ArrayList<>(sackA.getItems())) { //item to move
                    for (Sack sackB : sacks) { //target sack
                        if (sackA == sackB) continue;

                        //Check if itemA can fit directly into sackB
                        if (solution.canItemFitInSack(itemA, sackB)) {
                            int testMoveProfit;

                            //Temporarily move item from sackA to sackB
                            solution.removeItemFromSack(itemA, sackA);
                            solution.addItemToSack(itemA, sackB);
                            testMoveProfit = solution.getProfit();

                            //Check if unused items can be added to sackA
                            Item bestUnusedItem = null; //best unused item to add
                            for (Item unused : unusedItems) {
                                //Check if unused item can fit in sackA
                                if (solution.canItemFitInSack(unused, sackA)) {
                                    //Temporarily add unused item to sackA
                                    solution.addItemToSack(unused, sackA);
                                    int newProfit = solution.getProfit();
                                    //Check if this insertion improves profit
                                    if (newProfit > testMoveProfit) {
                                        testMoveProfit = newProfit;
                                        bestUnusedItem = unused;
                                    //Undo insertion if profit not improved
                                    } else {
                                        solution.removeItemFromSack(unused, sackA);
                                    }
                                }
                            }

                            //Check if this move improves profit more than previous best
                            if (testMoveProfit > bestProfit) {
                                bestProfit = testMoveProfit;
                                moveItem = itemA;
                                sourceSack = sackA;
                                targetSack = sackB;
                                removeItem = null;
                                unusedItem = bestUnusedItem;
                            }

                            //Undo the temporary move to try other options
                            solution.removeItemFromSack(itemA, sackB);
                            solution.addItemToSack(itemA, sackA);
                            if (bestUnusedItem != null) solution.removeItemFromSack(bestUnusedItem, sackA);

                        //If itemA doesn't fit directly, try removing one item from target sack
                        } else {
                            for (Item itemB : new ArrayList<>(sackB.getItems())) {
                                //Check if removing itemB allows itemA to fit in sackB
                                if (itemB.getWeight() >= itemA.getWeight() - (sackB.getCapacity() - sackB.getCurrentWeight())) {
                                    int testMoveProfit;

                                    //Temporarily move itemA to sackB and remove itemB
                                    solution.removeItemFromSack(itemA, sackA);
                                    solution.removeItemFromSack(itemB, sackB);
                                    solution.addItemToSack(itemA, sackB);
                                    testMoveProfit = solution.getProfit();

                                    //Check if unused items can be added to sackA
                                    Item bestUnusedItem = null;
                                    for (Item unused : unusedItems) {
                                        if (solution.canItemFitInSack(unused, sackA)) {
                                            solution.addItemToSack(unused, sackA);
                                            int newProfit = solution.getProfit();
                                            if (newProfit > testMoveProfit) {
                                                testMoveProfit = newProfit;
                                                bestUnusedItem = unused;
                                            } else {
                                                solution.removeItemFromSack(unused, sackA);
                                            }
                                        }
                                    }

                                    //Check if this move improves profit more than previous best
                                    if (testMoveProfit > bestProfit) {
                                        bestProfit = testMoveProfit;
                                        moveItem = itemA;
                                        sourceSack = sackA;
                                        targetSack = sackB;
                                        removeItem = itemB;
                                        unusedItem = bestUnusedItem;
                                    }

                                    //Undo the temporary move and removal to try other options
                                    solution.removeItemFromSack(itemA, sackB);
                                    solution.addItemToSack(itemA, sackA);
                                    solution.addItemToSack(itemB, sackB);
                                    if (bestUnusedItem != null) solution.removeItemFromSack(bestUnusedItem, sackA);
                                }
                            }
                        }
                    }
                }
            }

            //Apply the best found move if it improves the profit
            if (bestProfit > currentProfit) {
                improvementFound = true;

                //Move item from source sack to target sack
                solution.removeItemFromSack(moveItem, sourceSack);
                solution.addItemToSack(moveItem, targetSack);

                //Remove item from target sack if needed
                if (removeItem != null) {
                    solution.removeItemFromSack(removeItem, targetSack);
                }

                //Insert unused item into source sack if possible
                if (unusedItem != null) {
                    solution.addItemToSack(unusedItem, sourceSack);
                }
            }
        }

        return solution;
    }
}
