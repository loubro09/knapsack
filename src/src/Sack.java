import java.util.List;

public class Sack {
    private int capacity;
    private int currentWeight;
    private int id;
    private List <Item> items;

    public Sack(int capacity, int currentWeight, int id, List<Item> items) {
        this.capacity = capacity;
        this.currentWeight = currentWeight;
        this.id = id;
        this.items = items;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public int getId() {
        return id;
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean canFit(Item item) {
        return item.getWeight() + currentWeight <= capacity;
    }
    public void addItem(Item item) {
        items.add(item);
        currentWeight += item.getWeight();
        item.setAssignedSack(id);
    }

    public void removeItem(Item item) {
        items.remove(item);
        currentWeight -= item.getWeight();
        item.setAssignedSack(-1);
    }
}
