package main;

import java.util.List;

/**
 * Solution class representing the result of the sack filling problem.
 * It contains methods to manipulate the solution by adding/removing items from sacks
 * and calculating the total profit.
 * Needed for Neighborhood Search implementation.
 */
public class Solution {
    private List<Sack> sacks;
    private List<Item> items;

    public Solution(List<Sack> sacks, List<Item> items) {
        this.sacks = sacks;
        this.items = items;
    }

    public List<Sack> getSacks() {
        return sacks;
    }

    //Check if an item can fit in a sack
    public boolean canItemFitInSack(Item item, Sack sack) {
        return (sack.getCurrentWeight() + item.getWeight()) <= sack.getCapacity();
    }

    //Add item to sack and update sack's current weight and item's assigned sack
    public void addItemToSack(Item item, Sack sack) {
        sack.getItems().add(item);
        sack.setCurrentWeight(sack.getCurrentWeight() + item.getWeight());
        item.setAssignedSack(sack.getId());
    }

    //Calculate total profit of the solution
    public int getProfit() {
        int profit = 0;
        for (Sack sack : sacks) {
            for (Item item : sack.getItems()) {
                profit += item.getValue();
            }
        }
        return profit;
    }

    //Print the solution details
    public void printSolution() {
        for (Sack sack : sacks) {
            System.out.println("Sack " + sack.getId() +
                    " (" + sack.getCurrentWeight() + "kg/" + sack.getCapacity() + "kg):");
            for (Item item : sack.getItems()) {
                System.out.println("  Item: " + item.getName() +
                        "\n     Profit = " + String.format("%.2f", item.getProfit()));
            }
        }
        System.out.println("Total profit: " + getProfit());
    }
}