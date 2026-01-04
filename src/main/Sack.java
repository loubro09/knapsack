package main;

import java.util.List;

/**
 * Sack class representing a knapsack that can hold items up to a certain capacity.
 */
public class Sack {
    private int id;
    private int capacity;
    private int currentWeight;
    private List <Item> items;

    public Sack(int id, int capacity, List<Item> items) {
        this.id = id;
        this.capacity = capacity;
        this.currentWeight = 0;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }
    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

    public List<Item> getItems() {
        return items;
    }
}
