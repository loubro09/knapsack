package main;

/**
 * Item class representing an item with id, value, weight, profit, and assigned sack.
 */
public class Item {
    private String name;
    private int value;
    private int weight;
    private double profit;
    private int assignedSack;

    public Item(String name, int value, int weight) {
        this.name = name;
        this.value = value;
        this.weight = weight;
        this.profit = (double) value/weight; //calculate profit as value/weight
        this.assignedSack = -1; // -1 indicates that the item is not assigned to any sack
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public int getWeight() {
        return weight;
    }

    public double getProfit() {
        return profit;
    }

    public int getAssignedSack() {
        return assignedSack;
    }
    public void setAssignedSack(int assignedSack) {
        this.assignedSack = assignedSack;
    }
}
