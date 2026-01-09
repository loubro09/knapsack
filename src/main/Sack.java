package main;

import java.util.ArrayList;
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
        this.items = new ArrayList<>();
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

    public boolean canFit(Item item) {
        return currentWeight + item.getWeight() <= capacity;
    }

    public boolean addItem(Item item) {
        if (!canFit(item)) return false;
        items.add(item);
        currentWeight += item.getWeight();
        item.setAssignedSack(id);
        return true;
    }

    public boolean removeItem(Item item) {
        if (!items.contains(item)) return false;
        items.remove(item);
        currentWeight -= item.getWeight();
        item.setAssignedSack(-1); // mark as unused
        return true;
    }
    public int getRemainingCapacity() {
        return capacity - currentWeight;
    }

    public List<Item> copyItems() {
        return new ArrayList<>(items);
    }



}
