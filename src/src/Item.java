public class Item {
    private int value;
    private int weight;
    private int id;
    private int assignedSack;
    private double profit; //Value/weight

    public Item(int value, int weight, int id, int assignedSack, double profit) {
        this.value = value;
        this.weight = weight;
        this.id = id;
        this.assignedSack = assignedSack;
        this.profit = profit;
    }

    public int getValue() {
        return value;
    }

    public int getWeight() {
        return weight;
    }

    public int getId() {
        return id;
    }

    public int getAssignedSack() {
        return assignedSack;
    }

    public double getProfit() {
        return profit;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAssignedSack(int assignedSack) {
        this.assignedSack = assignedSack;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    @Override
    public String toString() {
        return "Item{" +
                "value=" + value +
                ", weight=" + weight +
                ", id=" + id +
                ", assignedSack=" + assignedSack +
                ", profit=" + profit +
                '}';
    }
}
